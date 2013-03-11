package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.io.IOException;

import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

/**
 * Controller for UI objects that wish to utilize the application's online
 * functionality.
 * @author Marko
 *
 */
public class ServerController {

	private ServerClient sc = null;
	private ServerController instance = null;
	
	/**
	 * Construct -- simply need to get the instance of the ServerClient.
	 */
	public ServerController()
	{
		sc = ServerClient.getInstance();
	}
	
	
	/**
	 * Singleton method.
	 * @return The instance of this class
	 */
	public ServerController getInstance()
	{
		if (instance == null) {
			instance = new ServerController();
		}
		return instance;
	}
	
	/**
	 * Check if the application is connected to the server.
	 * @return true if connected, false otherwise
	 */
	public boolean hasConnection()
	{
		// stubbed, return true for now
		return true;
	}
	
	/**
	 * 
	 * @param recipe
	 * @return ReturnCode.ERROR if anything goes wrong, ReturnCode.ALREADY_EXISTS if a recipe
	 * by that name already exists on the server (this will eventually be modified to check
	 * against URI instead of Recipe title), ReturnCode.SUCCESS if the recipe was successfully
	 * uploaded.
	 */
	public ReturnCode uploadRecipe(Recipe recipe)
	{
		try {
			return sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			return ReturnCode.ERROR;
		}
	}
	
	/**
	 * Calls the searchByKeywords() method of ServerClient.
	 * @param keywords The keywords by which the search should be performed.
	 * @return ReturnCode.ERROR if anything goes wrong, ReturnCode.NO_RESULTS if
	 * the search returned no results, ReturnCode.SUCCESS if the search was successful,
	 * in which case the results are written to the database and the observing views
	 * are notified.
	 */
	public ReturnCode searchByKeywords(String keywords)
	{
		try {
			return sc.searchByKeywords(keywords);
		} catch (IOException ioe) {
			return ReturnCode.ERROR;
		}
	}
}
