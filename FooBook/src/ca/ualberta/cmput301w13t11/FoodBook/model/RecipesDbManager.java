package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Implements the functionality required to manage the table of Recipes for the application.
 * @author Marko
 *
 */
public class RecipesDbManager extends DbManager {
	
	private static RecipesDbManager instance = null;
    private String getUserRecipesSQL = "FROM UserRecipes SELECT *";

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
     * Returns an ArrayList of all the Recipes stored in the database.
     * @return An ArrayList of all the Recipes stored in the database.
     */
    public ArrayList<Recipe> getUserRecipes() {
    	Cursor cursor = db.rawQuery(getUserRecipesSQL, null);
    	return CursorToRecipes(cursor);
    }
    

    /**
     * Deletes the given Recipe from the database.
     * @param recipe The Recipe to be deleted.
     */
    public void delete(Recipe recipe) {
      db.delete("UserRecipes", "URI = " + recipe.getUri(), null);
    }
    

}
