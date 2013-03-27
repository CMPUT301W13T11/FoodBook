package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.os.StrictMode;
/**
 * Communicates with the server to perform searches, upload recipes and upload photos to recipes.
 * Implements the singleton design pattern.
 * 
 * Code largely based on:
 * ESDemo with HTTP Client and GSON
 * 
 * LICENSE:
 * 
 * CC0 http://creativecommons.org/choose/zero/
 * 
 * To the extent possible under law, Abram Hindle and Chenlei Zhang has waived all copyright and related or neighboring rights to this work. This work is published from: Canada.
 * 
 * 
 * getThreadSafeClient() method cribbed from
 * http://tech.chitgoks.com/2011/05/05/fixing-the-invalid-use-of-singleclientconnmanager-connection-still-allocated-problem/
 * (last accessed March 10, 2013)
 *
 * @author Abram Hindle, Chenlei Zhang, Marko Babic
 */
public class ServerClient {	

	private static ServerClient instance = null;
	private static ResultsDbManager dbManager = null;
	static private final Logger logger = Logger.getLogger(ServerClient.class.getName());
	static private final long TIMEOUT_PERIOD = 10;
	static private final long UPLOAD_PHOTO_GRACE_PERIOD = 5;
	static private String test_server_string = "http://cmput301.softwareprocess.es:8080/testing/cmput301w13t11/";
	private static HttpClient httpclient = null;
	private static ClientHelper helper = null;
	private ArrayList<Recipe> results;
	public static enum ReturnCode
	{
		SUCCESS, ALREADY_EXISTS,NO_RESULTS, NOT_FOUND, ERROR, BUSY;
	}

	/**
	 * Empty constructor.
	 */
	private ServerClient()
	{

	}

	/**
	 * Returns the instance of ServerClient, or generates it if has not yet be instantiated.
	 * @return The instance of the ServerClient
	 */
	public static ServerClient getInstance()
	{
		if (instance == null) {
			/* Allow networking on main thread. Will be changed later so networking tasks are asynchronous. */
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 

			instance = new ServerClient();
			dbManager = ResultsDbManager.getInstance();
			httpclient = ServerClient.getThreadSafeClient();
			helper = new ClientHelper();
		}
		return instance;
	}

	/**
	 * Returns true if a connection can be established to the given url.
	 * Else, returns false
	 * @return True if connection is established, false otherwise.
	 */
	public boolean hasConnection(String url)
	{
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpclient.execute(get);
			int ret = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "Connection test return status: " + Integer.toString(ret));
			if (ret == HttpStatus.SC_OK) {
				return true;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception occured when testing connection:" + e.getMessage());
		}
		/* Something above failed, so we return false. */

		return false;
	}


	/**
	 * Gets a thread safe client - that is, a client which can be used in a multithreaded
	 * program and protects against certain errors that exist even in single threaded programs.
	 * @return A Object of type DefaultHttpClient which will
	 */
	public static DefaultHttpClient getThreadSafeClient()
	{
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager manager = client.getConnectionManager();
		HttpParams params = client.getParams();

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				manager.getSchemeRegistry()), params);
		return client;
	}


	public void writeResultsToDb()
	{
		dbManager = ResultsDbManager.getInstance();
		if (dbManager == null)
		{
			logger.log(Level.SEVERE, "ResultsDbManager null!!!");
		}
		dbManager.storeRecipes(results);
		DbManager dbm = DbManager.getInstance();
		dbm.notifyViews();
		logger.log(Level.SEVERE, "GOT RESULTS");
		logger.log(Level.SEVERE, "First result: " + results.get(0).getTitle());
	}

	/**
	 * Query the server for the Recipe with the given uri and return a ReturnCode based on the 
	 * server response.
	 * @param uri The uri of the Recipe for which we wish to search.
	 * @return SUCCESS if recipe was found, NOT_FOUND if we received a 404,
	 * ERROR if a problem occurred during the query.
	 */
	private ReturnCode checkForRecipe(long uri)
	{
		HttpResponse response = null;
		int retcode = -1;
		try {
			HttpGet get = new HttpGet(test_server_string + uri);
			get.addHeader("Accept","application/json");
			response = httpclient.execute(get);
			retcode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "HttpGet server response: " + response.getStatusLine().toString());
		} catch (Exception e) {
			return ReturnCode.ERROR;
		}

		if (retcode == HttpStatus.SC_NOT_FOUND) {
			logger.log(Level.SEVERE, "Recipe not on server. Response code: " + retcode);
			return ReturnCode.NOT_FOUND;
		}

		if (retcode != HttpStatus.SC_OK) {
			logger.log(Level.SEVERE, "Recipe to upload photo to could not be found.  Response code: " + retcode);
			return ReturnCode.ERROR;
		}

		/* Else the recipe was found and we return success. */
		return ReturnCode.SUCCESS;
	}


	/****************************************************************************************************************/
	/***************************************<Upload Recipe>**********************************************************/
	/****************************************************************************************************************/

	/**
	 * The task which implements the code necessary to upload a Recipe to the server --
	 * it is implemented as a Callable object such that the executing thread can be cancelled 
	 * should the operation be taking too long (ie. the network is down, spotty connection, etc.)
	 * @author mbabic
	 *
	 */
	private class UploadRecipeTask implements Callable<ReturnCode> {

		private Recipe recipe;

		public UploadRecipeTask(Recipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public ReturnCode call() {
			ReturnCode checkForRecipe = checkForRecipe(recipe.getUri());
			if (checkForRecipe == ReturnCode.SUCCESS) {
				return ReturnCode.ALREADY_EXISTS;
			}

			/* We are using the Recipe's URI as its _id on the server */
			HttpResponse response = null;
			HttpPost httpPost = new HttpPost(test_server_string+recipe.getUri());
			StringEntity se = null;

			se = helper.recipeToJSON(recipe);

			httpPost.setHeader("Accept","application/json");
			httpPost.setEntity(se);
			try {
				response = httpclient.execute(httpPost);
			} catch (ClientProtocolException cpe) {

				logger.log(Level.SEVERE, cpe.getMessage());
				cpe.printStackTrace();
				return ReturnCode.ERROR;

			} catch (IOException ioe) {

				logger.log(Level.SEVERE, ioe.getMessage());
				ioe.printStackTrace();
				return ReturnCode.ERROR;

			} 

			String status = response.getEntity().toString();
			int retcode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "upload request server response: " + response.getStatusLine().toString());

			if (retcode == HttpStatus.SC_CREATED)
				return ReturnCode.SUCCESS;
			else
				return ReturnCode.ALREADY_EXISTS;
		}
	}

	/**
	 * Uploads the given recipe to the server.
	 * @param recipe The recipe to be uploaded.
	 * ReturnCode.ERROR if anything goes wrong, ReturnCode.ALREADY_EXISTS if a recipe
	 * by that name already exists on the server (this will eventually be modified to check
	 * against URI instead of Recipe title), ReturnCode.SUCCESS if the recipe was successfully
	 * uploaded, or ReturnCode.BUSY if the network is not responding or the operation is 
	 * taking too long.
	 */
	public ReturnCode uploadRecipe(Recipe recipe) throws IllegalStateException, IOException
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<ReturnCode> future = executor.submit(new UploadRecipeTask(recipe));
		ReturnCode ret = ReturnCode.ERROR;
		try {
			ret = future.get(TIMEOUT_PERIOD, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			logger.log(Level.SEVERE, "Upload Recipe operation timed out.");
			return ReturnCode.BUSY;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during upload recipe operation.");
			return ReturnCode.ERROR;
		}
		/* Got here so the operation finished. */
		executor.shutdownNow();
		return ret;
	}

	/**************************************</Upload Recipe>**********************************************************/

	/****************************************************************************************************************/
	/**************************************<Search by Keywords>******************************************************/
	/****************************************************************************************************************/

	/**
	 * The task which implements the code necessary to query the server using keywords to the server --
	 * it is implemented as a Callable object such that the executing thread can be cancelled 
	 * should the operation be taking too long (ie. the network is down, spotty connection, etc.)
	 * @author mbabic
	 */
	private class SearchByKeywordsTask implements Callable<ReturnCode> {

		private String str;

		public SearchByKeywordsTask(String str)
		{
			this.str = str;
		}

		@Override
		public ReturnCode call() 
		{
			ArrayList<Recipe> search_results = new ArrayList<Recipe>();
			HttpResponse response = null;
			logger.log(Level.SEVERE, "Search string passed:" + str);


			try {

				HttpGet search_request = new HttpGet(test_server_string+"_search?q=" + 
						java.net.URLEncoder.encode(str, "UTF-8"));

				search_request.setHeader("Accept", "application/json");

				response = httpclient.execute(search_request);

				String status = response.getStatusLine().toString();
				logger.log(Level.INFO, "search response: " + status);

				String json = helper.responseToString(response);
				search_results = helper.toRecipeList(json);
				results = search_results;

			} catch (IllegalArgumentException iae) {

				logger.log(Level.SEVERE, "HttpGet failed: " + iae.getMessage());
				return ReturnCode.ERROR;

			} catch (ClientProtocolException cpe) {

				logger.log(Level.SEVERE, cpe.getMessage());
				return ReturnCode.ERROR;

			} catch (IOException ioe) {

				logger.log(Level.SEVERE, "execute failed" + ioe.getMessage());
				return ReturnCode.ERROR;
			}

			/* 
			 * If no results were found, inform the caller by setting ReturnCode to 
			 * NO_RESULTS -- do NOT attempt to clear/write these results to ServerDb.
			 */
			if (search_results.isEmpty()) {
				logger.log(Level.SEVERE, "Search by keywords \"" + str + "\" yielded no results.");
				return ReturnCode.NO_RESULTS;
			}

			return ReturnCode.SUCCESS;	
		}
	}


	/**
	 * Performs a search of online recipes by keywords.
	 * @param str The string of keywords we wish to search by.
	 * @return ReturnCode.ERROR if anything goes wrong, ReturnCode.NO_RESULTS if
	 * the search returned no results, ReturnCode.SUCCESS if the search was successful,
	 * in which case the results are written to the database and the observing views
	 * are notified, ReturnCode.BUSY if the server was busy or the operation took
	 * longer than TIME_PERIOD seconds.
	 */
	public ReturnCode searchByKeywords(String str) throws ClientProtocolException, IOException
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<ReturnCode> future = executor.submit(new SearchByKeywordsTask(str));
		ReturnCode ret = ReturnCode.ERROR;
		try {
			ret = future.get(TIMEOUT_PERIOD, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			logger.log(Level.SEVERE, "Search by Keywords operation timed out.");
			return ReturnCode.BUSY;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during Search by Keywords operation.");
			return ReturnCode.ERROR;
		}
		/* Got here so the operation finished. */
		executor.shutdownNow();
		return ret;	
	}


	/*************************************</Search by Keywords>****************************************************/


	/****************************************************************************************************************/
	/**************************************<Search by Ingredients>***************************************************/
	/****************************************************************************************************************/

	/**
	 * The task which implements the code necessary to query the server by a list of Ingredients --
	 * it is implemented as a Callable object such that the executing thread can be cancelled 
	 * should the operation be taking too long (ie. the network is down, spotty connection, etc.)
	 * @author mbabic
	 *
	 */
	private class SearchByIngredientsTask implements Callable<ReturnCode> {

		private ArrayList<Ingredient> ingredients;

		public SearchByIngredientsTask(ArrayList<Ingredient> ingredients)
		{
			this.ingredients = ingredients;
		}

		@Override
		public ReturnCode call() {
			ArrayList<Recipe> search_results = null;

			String ingredients_str = ingredientsToString(ingredients);	

			/* We next form the HTTP query string itself. */
			HttpPost searchRequest = new HttpPost(test_server_string + "_search?pretty=1");
			String query = "{\"query\" : {\"query_string\" : {\"default_field\" : \"ingredients.name\", \"query\" : \"" + ingredients_str + "\"}}}";
			logger.log(Level.INFO, "query string = " + query);

			try {
				StringEntity str_entity = new StringEntity(query);
				searchRequest.setHeader("Accept","application/json");
				searchRequest.setEntity(str_entity);

				HttpResponse response = httpclient.execute(searchRequest);
				logger.log(Level.SEVERE, "Http response (searchByIngredients search attempt): " + response.getStatusLine().toString());
				String response_str = helper.responseToString(response);

				search_results = helper.toRecipeList(response_str);

			} catch (UnsupportedEncodingException uee) {

				logger.log(Level.SEVERE, "Failed to create StringEntity from query : " + uee.getMessage());
				uee.printStackTrace();
				return ReturnCode.ERROR;

			} catch (ClientProtocolException cpe) {

				logger.log(Level.SEVERE, "ClientProtocolException in execution of search request: " + cpe.getMessage());
				cpe.printStackTrace();
				return ReturnCode.ERROR;

			} catch (IOException ioe) {

				logger.log(Level.SEVERE, "IOException in execution of search request: " + ioe.getMessage());
				ioe.printStackTrace();
				return ReturnCode.ERROR;
			}


			/* 
			 * If our search returned no results, we do not write anything to the Results Db
			 * and we return the code no_results.
			 */
			if (search_results.isEmpty()) {
				return ReturnCode.NO_RESULTS;
			}

			/* Else, our search returned results; we update "results" and return success code. */
			results = search_results;
			return ReturnCode.SUCCESS;
		}
	}

	/**
	 * Converts the given list of ingredients to a string appropriate for use in a server query.
	 * @param ingredients The list of ingredients to convert to a search appropriate string.
	 * @return A string consisting of the string names with " OR " inserted between.
	 */
	private String ingredientsToString(ArrayList<Ingredient> ingredients)
	{
		String ingredients_str = "";	/* The list of ingredients with the logical OR between each item. */
		for (int i = 0; i < ingredients.size(); i++) {
			if (i != 0) {
				/* 
				 * If it is not our first go around, we do not need to 
				 * concatenate "OR" to the string
				 */
				ingredients_str += " OR ";
			}
			ingredients_str += ingredients.get(i).getName();
		}
		logger.log(Level.INFO, "ingredients_str = " + ingredients_str);
		return ingredients_str;
	}


	/**
	 * Query the server for Recipes which contains at subset of the given ingredients list.
	 * @param ingredients The list of ingredients by which to search.
	 * @return TODO
	 */
	public ReturnCode searchByIngredients(ArrayList<Ingredient> ingredients)
	{

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<ReturnCode> future = executor.submit(new SearchByIngredientsTask(ingredients));
		ReturnCode ret = ReturnCode.ERROR;
		try {
			ret = future.get(TIMEOUT_PERIOD, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			logger.log(Level.SEVERE, "Search by Ingredients operation timed out.");
			return ReturnCode.BUSY;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during Search by Ingredients operation.");
			return ReturnCode.ERROR;
		}
		/* Got here so the operation finished. */
		executor.shutdownNow();
		return ret;
	}


	/*************************************</Search by Ingredients>***************************************************/


	/****************************************************************************************************************/
	/**************************************<Upload Photo>************************************************************/
	/****************************************************************************************************************/

	/**
	 * The task which implements the code necessary to upload a photo to the Server --
	 * it is implemented as a Callable object such that the executing thread can be cancelled 
	 * should the operation be taking too long (ie. the network is down, spotty connection, etc.)
	 * @author mbabic
	 *
	 */
	private class UploadPhotoTask implements Callable<ReturnCode> {

		private Photo photo;
		private long uri;
		public UploadPhotoTask(Photo photo, long uri)
		{
			this.photo = photo;
			this.uri = uri;
		}

		@Override
		public ReturnCode call() {
			int maxTries = 10;
			int tries = 0;

			ReturnCode searchForRecipe = checkForRecipe(uri);
			if (searchForRecipe != ReturnCode.SUCCESS) {
				/* 
				 * We couldn't find the recipe online or we encountered an error, so we cannot upload the recipe
				 * at this time.
				 */
				return searchForRecipe;
			}


			/* Else, the Recipe exists online and we try to upload the given photo to it. */
			int retcode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			HttpResponse response = null;
			while (tries < maxTries && retcode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				tries ++;
				try {

					httpclient = getThreadSafeClient();
					/* We first must convert the given Photo to a ServerPhoto. */
					ServerPhoto serverPhoto = new ServerPhoto(photo);

					/* Now we must construct a suitable JSON style string for the ServerPhoto. */
					String sp_str = helper.serverPhotoToJSON(serverPhoto);
					logger.log(Level.INFO, "serverPhotoToJson() result: " + sp_str);

					HttpPost updateRequest = new HttpPost(test_server_string + uri + "/_update");
					String query = 	"{\"script\":\"ctx._source.photos += xxx\", \"params\" : " +
							"{ \"xxx\" : " + sp_str + "}}";
					logger.log(Level.INFO, "stringQuery = " + query);

					StringEntity stringentity = new StringEntity(query);

					updateRequest.setHeader("Accept","application/json");
					updateRequest.setEntity(stringentity);
					response = httpclient.execute(updateRequest);
					retcode = response.getStatusLine().getStatusCode();
					logger.log(Level.SEVERE, "Upload request return code: " + response.getStatusLine().toString());

					if (retcode == HttpStatus.SC_OK)
						break;

				} catch (ClientProtocolException cpe) {
					logger.log(Level.SEVERE, "ClientProtocolException when executing HttpGet : " + cpe.getMessage());
					cpe.printStackTrace();
					return ReturnCode.ERROR;
				} catch (IOException ioe) {
					logger.log(Level.SEVERE, "IOException when executing HttpGet : " + ioe.getMessage());
					ioe.printStackTrace();
					return ReturnCode.ERROR;
				}
			}
			if (tries < maxTries)
				return ReturnCode.SUCCESS;
			else
				return ReturnCode.ERROR;

		}
	}






	/**
	 * Upload the given Photo to the appropriate Recipe.
	 * @param (Photo) photo The photo to be added to the server-side version of the Recipe.
	 * @param (long) uri The uri of the Recipe to be updated.
	 * @return NOT_FOUND if the Recipe cannot be found on the server,
	 * 			ERROR on any other error occurred while attempting to upload,
	 * 			SUCCESS on successful upload.
	 */
	public ReturnCode uploadPhotoToRecipe(Photo photo, long uri)
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<ReturnCode> future = executor.submit(new UploadPhotoTask(photo, uri));
		ReturnCode ret = ReturnCode.ERROR;
		try {
			ret = future.get(TIMEOUT_PERIOD + UPLOAD_PHOTO_GRACE_PERIOD, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			logger.log(Level.SEVERE, "Upload photo operation timed out.");
			return ReturnCode.BUSY;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during upload photo operation.");
			return ReturnCode.ERROR;
		}
		/* Got here so the operation finished. */
		executor.shutdownNow();
		return ret;
		
//		int maxTries = 10;
//		int tries = 0;
//
//		ReturnCode searchForRecipe = checkForRecipe(uri);
//		if (searchForRecipe != ReturnCode.SUCCESS) {
//			/* 
//			 * We couldn't find the recipe online or we encountered an error, so we cannot upload the recipe
//			 * at this time.
//			 */
//			return searchForRecipe;
//		}
//
//
//		/* Else, the Recipe exists online and we try to upload the given photo to it. */
//		int retcode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
//		HttpResponse response = null;
//		while (tries < maxTries && retcode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
//			tries ++;
//			try {
//
//				httpclient = getThreadSafeClient();
//				/* We first must convert the given Photo to a ServerPhoto. */
//				ServerPhoto serverPhoto = new ServerPhoto(photo);
//
//				/* Now we must construct a suitable JSON style string for the ServerPhoto. */
//				String sp_str = helper.serverPhotoToJSON(serverPhoto);
//				logger.log(Level.INFO, "serverPhotoToJson() result: " + sp_str);
//
//				HttpPost updateRequest = new HttpPost(test_server_string + uri + "/_update");
//				String query = 	"{\"script\":\"ctx._source.photos += xxx\", \"params\" : " +
//						"{ \"xxx\" : " + sp_str + "}}";
//				logger.log(Level.INFO, "stringQuery = " + query);
//
//				StringEntity stringentity = new StringEntity(query);
//
//				updateRequest.setHeader("Accept","application/json");
//				updateRequest.setEntity(stringentity);
//				response = httpclient.execute(updateRequest);
//				retcode = response.getStatusLine().getStatusCode();
//				logger.log(Level.SEVERE, "Upload request return code: " + response.getStatusLine().toString());
//
//				if (retcode == HttpStatus.SC_OK)
//					break;
//
//			} catch (ClientProtocolException cpe) {
//				logger.log(Level.SEVERE, "ClientProtocolException when executing HttpGet : " + cpe.getMessage());
//				cpe.printStackTrace();
//				return ReturnCode.ERROR;
//			} catch (IOException ioe) {
//				logger.log(Level.SEVERE, "IOException when executing HttpGet : " + ioe.getMessage());
//				ioe.printStackTrace();
//				return ReturnCode.ERROR;
//			}
//		}
//		if (tries < maxTries)
//			return ReturnCode.SUCCESS;
//		else
//			return ReturnCode.ERROR;

	}


	/**************************************</Upload Photo>************************************************************/



	/**
	 * Retrieves the recipe associated with the given URI from the server;
	 * only called when we are guaranteed that such a Recipe exists, and 
	 * thus performs no error checking.
	 * @param (long) uri The URI of the recipe to be retrieved.
	 * @return The Recipe with the given URI.
	 */
	private Recipe getRecipe(long uri)
	{
		try{
			HttpGet getRequest = new HttpGet(test_server_string + Long.toString(uri));
			logger.log(Level.INFO, "getRequest : " + getRequest.toString());

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String response_str = helper.responseToString(response);
			logger.log(Level.INFO, "Server response string: " + response_str);
			return helper.responseStringToRecipe(response_str);

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public String getServerUrl()
	{
		return test_server_string;
	}
}

