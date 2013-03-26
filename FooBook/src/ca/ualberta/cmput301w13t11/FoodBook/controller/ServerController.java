package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.io.IOException;

import android.os.AsyncTask;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.tasks.SearchByKeywordsTask;

/**
 * Controller for UI objects that wish to utilize the application's online
 * functionality.
 * @author Marko
 *
 */
public class ServerController {

	private ServerClient sc = null;
	private static ServerController instance = null;
	
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
	public static ServerController getInstance(FView<DbManager> view)
	{
		if (instance == null) {
			instance = new ServerController();
		}
		DbManager temp = DbManager.getInstance();
		temp.addView(view);
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
		
		//new SearchByKeywordsTask().execute(keywords);
		return ReturnCode.SUCCESS;
	}
}
