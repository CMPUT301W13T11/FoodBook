package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
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

import com.google.gson.reflect.TypeToken;

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
 *-----------------------------------------------------------------------------------------------------
 *
 * NOTE: IN THIS IMPLEMENTATION WE ALLOW NETWORKING TASKS TO BE PERFORMED IN THE MAIN THREAD.
 * 		THIS WILL NOT BE PRESENT IN THE FINAL IMPLEMENTATION TO ENSURE THAT UI RESPONSIVENESS
 * 		IS NOT AFFECTED BY NETWORKING TASKS.
 * ----------------------------------------------------------------------------------------------------
 */
public class ServerClient {	
	
	private static ServerClient instance = null;
	private static ResultsDbManager dbManager = null;
	static private final Logger logger = Logger.getLogger(ServerClient.class.getName());
	static private String test_server_string = "http://cmput301.softwareprocess.es:8080/testing/cmput301w13t11/";
	private static HttpClient httpclient = null;
	private static ClientHelper helper = null;
	public static enum ReturnCode
	{
		SUCCESS, ALREADY_EXISTS,NO_RESULTS, NOT_FOUND, ERROR;
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
	 * Uploads the given recipe to the server.
	 * @param recipe The recipe to be uploaded.
	 * ReturnCode.ERROR if anything goes wrong, ReturnCode.ALREADY_EXISTS if a recipe
	 * by that name already exists on the server (this will eventually be modified to check
	 * against URI instead of Recipe title), ReturnCode.SUCCESS if the recipe was successfully
	 * uploaded.
	 */
	public ReturnCode uploadRecipe(Recipe recipe) throws IllegalStateException, IOException
	{
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
	
	/**
	 * Performs a search of online recipes by keywords.
	 * @param str The string of keywords we wish to search by.
	 * @return ReturnCode.ERROR if anything goes wrong, ReturnCode.NO_RESULTS if
	 * the search returned no results, ReturnCode.SUCCESS if the search was successful,
	 * in which case the results are written to the database and the observing views
	 * are notified.
	 */
	public ReturnCode searchByKeywords(String str) throws ClientProtocolException, IOException
	{
		ArrayList<Recipe> search_results = null;
		HttpResponse response = null;
		try {
			logger.log(Level.SEVERE, "Search string passed:" + str);

			HttpGet search_request = new HttpGet(test_server_string+"_search?q=" + 
												java.net.URLEncoder.encode(str, "UTF-8"));
			search_request.setHeader("Accept", "application/json");

			response = httpclient.execute(search_request);
			String status = response.getStatusLine().toString();
			logger.log(Level.INFO, "search response: " + status);
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
		
		HttpEntity entity = response.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		String out, json = "";
		
		while ((out = br.readLine()) != null) {
			json += out;
		}
		search_results = helper.toRecipeList(json);
		
		/* 
		 * If no results were found, inform the caller by setting ReturnCode to 
		 * NO_RESULTS -- do NOT attempt to clear/write these results to ServerDb.
		 */
		if (search_results.isEmpty()) {
			logger.log(Level.SEVERE, "NO RESULTS");
			return ReturnCode.NO_RESULTS;

		}
		
		/* TODO: stores these results in the "SearchResults" db and notify that db's views. */
		dbManager.storeRecipes(search_results);
		logger.log(Level.SEVERE, "GOT RESULTS");
		logger.log(Level.SEVERE, "First result: " + search_results.get(0).getTitle());

		return ReturnCode.SUCCESS;
		
		
	}
	
	/**
	 * Search the server for recipes which are composed of ingredients 
	 * @param ingredients The list of ingredients by which to search.
	 * @return An ArrayList of the Recipes found.
	 */
	public ReturnCode searchByIngredients(ArrayList<Ingredient> ingredients)
	{
		ArrayList<Recipe> search_results = null;
		/* 
		 * We first translate the list of ingredients into a string which can be 
		 * used to query the server.  For now we will simply add the logical operator
		 * OR between the ingredients -- this could be changed later for better
		 * functionality.
		 */
		
		String ingredients_str = "";
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

		/* We next form the HTTP query string itself. */
		HttpPost searchRequest = new HttpPost(test_server_string + "_search?pretty=1");
		String query = "{\"query\" : {\"query_string\" : {\"default_field\" : \"ingredients\", \"query\" : \"" + ingredients_str + "\"}}}";
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
		
		/* Else, our search returned results; we write them to the Results Db and return success code. */
		dbManager.storeRecipes(search_results);
		return ReturnCode.SUCCESS;
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
		/* 
		 * We first must determine if the Recipe to which we wish to upload the photo
		 * actually exists on the server.  To do so, we attempt to retrieve the Recipe
		 * from the server and examine the HTTP return code we receive.
		 */
		try {
			HttpGet get = new HttpGet(test_server_string + uri);
			get.addHeader("Accept","application/json");
			HttpResponse response = httpclient.execute(get);
			int retcode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "HttpGet server response: " + response.getStatusLine().toString());

			if (retcode == HttpStatus.SC_NOT_FOUND) {
				logger.log(Level.SEVERE, "Recipe not on server. Response code: " + retcode);
				return ReturnCode.NOT_FOUND;
			}
			
			if (retcode == HttpStatus.SC_OK) {
				/* Else, the Recipe exists on the server and we should try to get it. */
				Recipe recipe = getRecipe(uri);
			}
			
		} catch (ClientProtocolException cpe) {
			logger.log(Level.SEVERE, "ClientProtocolException when executing HttpGet : " + cpe.getMessage());
			cpe.printStackTrace();
			return ReturnCode.ERROR;
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, "IOException when executing HttpGet : " + ioe.getMessage());
			ioe.printStackTrace();
			return ReturnCode.ERROR;
		}
		return ReturnCode.ERROR;
	}
	
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

	