package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
	//recipesTable = "UserRecipes";
	//public String ingredsTable = "RecipeIngredients";
	//public String photosTable = "RecipePhotos";
	//public String getSQL = "SELECT * FROM " + recipesTable;

	public RecipesDbManager(Context context)
	{
		super(context);
		recipesTable = "UserRecipes";
		ingredsTable = "RecipeIngredients";
		photosTable = "RecipePhotos";
		getSQL = "SELECT * FROM " + recipesTable;
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
	
}
