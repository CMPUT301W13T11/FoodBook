package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.IOException;

import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

/**
 * Controller for UI objects that wish to utilize the application's online
 * functionality.
 * @author Marko
 *
 */
public class ServerController {

	private ServerClient sc = null;
	
	/**
	 * Construct -- simply need to get the instance of the ServerClient.
	 */
	public ServerController()
	{
		sc = ServerClient.getInstance();
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
	 * Uploads the given recipe to the server.
	 * @param recipe The recipe to be uploaded.
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
