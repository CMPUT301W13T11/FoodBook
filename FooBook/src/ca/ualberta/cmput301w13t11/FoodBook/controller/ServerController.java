package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
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
	private static ServerController instance = null;
	
	/**
	 * Constructor -- simply need to get the instance of the ServerClient.
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
	 * TODO: is this code dead?
	 * @return true if connected, false otherwise
	 */
	public boolean hasConnection()
	{
		// stubbed, return true for now
		return true;
	}
	
	/**
	 * Upload the given Recipe to the server.
	 * @param recipe The Recipe to be uploaded.
	 * @return ReturnCode.ERROR if anything goes wrong, ReturnCode.ALREADY_EXISTS if a recipe
	 * by that name already exists on the server (this will eventually be modified to check
	 * against URI instead of Recipe title), ReturnCode.SUCCESS if the recipe was successfully
	 * uploaded, ReturnCode.BUSY if the server does not respond within a fixed time.
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
	 * Performs a search using the passed list of Ingredients as the search terms --
	 * will return recipes which contain a subset of the passed ingredients.
	 * @param ingredients The ingredients which we would like to include in the search term.
	 * @return NO_RESULTS on a search that yields no results, BUSY if the server does respond
	 * in time, ERROR should something go wrong during the attempt to search, SUCCESS
	 * on a successful search.
	 */
	public ReturnCode searchByIngredients(ArrayList<Ingredient> ingredients)
	{
			return sc.searchByIngredients(ingredients);
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
	
	/**
	 * Calls the uploadPhotoToRecipe() method of ServerClient.
	 * @param photo The photo to be uploaded.
	 * @param uri the URI of the Recipe to which the photo is to be uploaded.
	 * @return ReturnCode.ERROR if anything goes wrong, ReturnCode.NOT_FOUND if he 
	 * Recipe does not exist on the server, ReturnCode.SUCCESS if everything went okay.
	 */
	public ReturnCode uploadPhotoToRecipe(Photo photo, long uri)
	{
		try {
			return sc.uploadPhotoToRecipe(photo, uri);
		} catch (Exception e) {
			return ReturnCode.ERROR;
		}
	}
}
