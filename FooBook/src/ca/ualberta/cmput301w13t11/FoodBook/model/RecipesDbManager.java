package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Implements the functionality required to manage the table of Recipes for the application.
 * @author Marko Babic, Marko Tupala
 *
 *------------------------------------------------------------------------------------------------------------
 * NOTE: REFACTORTING IS NOT COMPLETE AS OF THIS ITERATION.  IN THE FUTURE, MORE FUNCTIONALITY FROM DbManager
 * WILL APPEAR IN THIS CLASS.
 *------------------------------------------------------------------------------------------------------------
 */
public class RecipesDbManager extends DbManager {
	
	private static RecipesDbManager instance = null;
	protected String recipesTable = "UserRecipes";
	private String getUserRecipesSQL = "SELECT * FROM " + recipesTable;

	public RecipesDbManager(Context context)
	{
		super(context);
	}
	
	/**
	 * 
	 * @param context Context needed to execute database creation statements (if necessary).
	 * @return The instance of RecipesDbManager.
	 */
	public static RecipesDbManager getInstance(Context context)
	{
		// call super to create Db if necessary
		DbManager.getInstance(context);
		if (instance == null)
			instance = new RecipesDbManager(context);
		return instance;
	}
	
	/**
	 * 
	 * @return The instance of the RecipesDbManager.
	 */
	public static RecipesDbManager getInstance()
	{
		return instance;
	}
	
	/**
	 * Returns an ArrayList of all the Recipes stored in the table.
	 * @return An ArrayList of all the Recipes stored in the table.
	 */
	public ArrayList<Recipe> getRecipes(String query) {
	    Cursor cursor = db.rawQuery(query, null);
	    return cursorToRecipes(cursor);
	}

	/**
	 * Deletes the given Recipe from the database.
	 * @param recipe The Recipe to be deleted.
	 */
	public void deleteRecipe(Recipe recipe) {
	    db.delete(recipesTable, "URI = " + recipe.getUri(), null);
	}

	// PRIVATE METHODS *********************************************************
        
}
