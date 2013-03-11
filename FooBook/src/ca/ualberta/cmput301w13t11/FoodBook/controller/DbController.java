package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import ca.ualberta.cmput301w13t11.FoodBook.RecipeApplication;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

public class DbController {
	
	private DbManager db;
	
	// singleton pattern implementation
    private static DbController instance = null;
	
	/* 
	 * private constructor because we're using the singleton pattern.
	 */
	private DbController(Context context) {
		db = DbManager.getInstance(context);
	}
	
    /**
     * Get instance of the singleton DbController.
     * @return The instance of the class.
     */
    public static DbController getInstance(Context context) {
    	if (instance == null) {
    		// db instance doesn't exist, create new one
    		instance = new DbController(context);
    	}
    			
    	return instance;
    }

	public ArrayList<Recipe> getUserRecipes() {
		//DbManager db = RecipeApplication.getDbManager();
		return db.getUserRecipes();
	}
	
	public void addRecipe(Recipe recipe) {
		db.insert(recipe);
	}
	
	public void deleteRecipe(Recipe recipe)
	{
		db.delete(recipe);
	}
}
