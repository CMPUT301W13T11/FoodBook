package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

import android.content.Context;
/**
 * Implements the functionality required to manage the Results database, which
 * stores the results of the latest search query.  Mostly, it will keep track of views
 * which are observing the Results database.
 * <<Singleton>>
 * @author Mark Tupala, Marko Babic
 */
public class ResultsDbManager extends DbManager {
	
	private static ResultsDbManager instance = null;

	/**
	 * Constructor -- calls the DbManager constructor to execute SQL table creation statements if necessary.
	 * @param context The context to be passed to the DbManager constructor.
	 */
	public ResultsDbManager(Context context)
	{
		super(context);
		recipesTable = "ResultsRecipes";
		ingredsTable = "ResultsIngredients";
		photosTable = "ResultsPhotos";
		getSQL = "SELECT * FROM " + recipesTable;
	}

	/**
	 * Store the given list of Recipes to the ResultsDb -- overwrites the previous results with the new one.
	 * @param The list of Recipes to be stored to the Results Db.
	 */
	public void storeRecipes(ArrayList<Recipe> recipes) {
	    db.delete(ingredsTable, null, null);
	    db.delete(photosTable, null, null);
	    db.delete(recipesTable, null, null);
	    for (Recipe recipe : recipes) {
	        insertRecipe(recipe);
	    }
	    notifyViews();
	}
	
	/**
	 * Gets the instance of the IngredientsDbManager -- or creates it, if necessary.
	 * @param context Context needed to execute database creation statements (if necessary).
	 * @return The instance of IngredientsDbManager.
	 */
	public static ResultsDbManager getInstance(Context context)
	{
		// call super to create Db if necessary
		DbManager.getInstance(context);
		if (instance == null)
			instance = new ResultsDbManager(context);
		return instance;
	}
	
	/**
	 * @return The instance of the RecipesDbManager.
	 */
	public static ResultsDbManager getInstance()
	{
		return instance;
	}
	
	
}
