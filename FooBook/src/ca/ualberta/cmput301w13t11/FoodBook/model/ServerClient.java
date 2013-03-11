package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 *
 */
public class ServerClient {	
	
	private static ServerClient instance = null;
	private static DbManager dbManager = null;
	static private final Logger logger = Logger.getLogger(ServerClient.class.getName());
	static private String test_server_string = "http://cmput301.softwareprocess.es:8080/testing/cmput301w13t11/";
	private static HttpClient httpclient = null;
	private static ClientHelper helper = null;
	public static enum ReturnCode
	{
		SUCCESS, ALREADY_EXISTS,NO_RESULTS, ERROR;
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
			instance = new ServerClient();
			dbManager = DbManager.getInstance();
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
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(test_server_string+recipe.getTitle());
		StringEntity se = null;
		
		se = helper.toJSON(recipe);
		
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
		logger.log(Level.INFO, "upload request server response: " + response);

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
		if (search_results.isEmpty())
			return ReturnCode.NO_RESULTS;
		
		/* TODO: stores these results in the "SearchResults" db and notify that db's views. */
		dbManager.storeResults(search_results);
		return ReturnCode.SUCCESS;
		
		
	}
	
	/**
	 * Search the server for recipes which are composed of ingredients 
	 * @param ingredients The list of ingredients by which to search.
	 * @return An ArrayList of the Recipes found.
	 */
	public ArrayList<Recipe> searchByIngredients(ArrayList<Ingredient> ingredients)
	{
		//TODO: implement this code
		return new ArrayList<Recipe>();
	}
	
	/**
	 * Upload the given Photo to the appropriate Recipe.
	 * @param photo The photo to be added to the server-side version of the Recipe.
	 * @param recipe The Recipe to be updated.
	 */
	public ReturnCode uploadPhotoToRecipe(Photo photo, Recipe recipe)
	{
		//TODO: implement
		return ReturnCode.ERROR;
	}
	
	public String getServerUrl()
	{
		return test_server_string;
	}
}
