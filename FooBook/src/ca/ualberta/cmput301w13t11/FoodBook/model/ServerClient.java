package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.IOException;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Communicates with the server to perform searches, upload recipes and upload photos to recipes.
 * Implements the singleton design pattern.
 * TODO: write tests for these methods
 * @author Marko Tomislav Babic
 *
 */
public class ServerClient {	
	
	private static ServerClient instance = null;
	static private final Logger logger = Logger.getLogger(ServerClient.class.getName());
	static private String test_server_string = "http://cmput301.softwareprocess.es:8080/testing/cmput301w13t11/";
	private HttpClient httpclient = new DefaultHttpClient();
	private ClientHelper helper = new ClientHelper();
	private static enum ReturnCode
	{
		SUCCESS, NO_RESULTS, ERROR;
	}
	
	/**
	 * Empty constructor.
	 */
	public ServerClient()
	{
		
	}
	
	/**
	 * Returns the instance of ServerClient, or generates it if has
	 * not yet be instantiated.
	 * @return
	 */
	public static ServerClient getInstance()
	{
		if (instance == null) {
			instance = new ServerClient();
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
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			int ret = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "Connection test return status: ");
			if (ret == HttpStatus.SC_OK)
				return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception occured when testing connection:" + e.getMessage());
		}
		/* Something above failed, so we return false. */
		return false;
	}
	
	
	/**
	 * Uploads the given recipe to the server.
	 * TODO: should return error codes????
	 * @param recipe The recipe to be uploaded.
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void uploadRecipe(Recipe recipe) throws IllegalStateException, IOException
	{
		HttpEntity entity;
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(test_server_string+recipe.getTitle());
		StringEntity se = null;
		String status;
		
		se = helper.toJSON(recipe);
		
		httpPost.setHeader("Accept","application/json");
		httpPost.setEntity(se);
		
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException cpe) {
			logger.log(Level.SEVERE, cpe.getMessage());
			cpe.printStackTrace();
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, ioe.getMessage());
			ioe.printStackTrace();
		}
		
		status = response.getEntity().toString();
		logger.log(Level.INFO, "upload request server response: " + response);
	}
	
	/**
	 * Performs a search of online recipes by keywords.
	 * @param str The string of keywords we wish to search by.
	 * @return True on successful connection + return of 
	 */
	public ReturnCode searchByKeywords(String str) throws ClientProtocolException, IOException
	{
		ArrayList<Recipe> search_results = null;
		HttpResponse response = null;
		try {
			HttpGet search_request = new HttpGet(test_server_string+"_search&q=" + 
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
		
		search_results = helper.toRecipeList(response);
		
		/* 
		 * If no results were found, inform the caller by setting ReturnCode to 
		 * NO_RESULTS -- do NOT attempt to clear/write these results to ServerDb.
		 */
		if (search_results.isEmpty())
			return ReturnCode.NO_RESULTS;
		
		/* TODO: stores these results in the "SearchResults" db and notify that db's views. */
		return ReturnCode.SUCCESS;
		
		
	}
	
	
	public void attachPhotoToRecipe(Photo photo, Recipe recipe)
	{
		//TODO: implement
	}
}
