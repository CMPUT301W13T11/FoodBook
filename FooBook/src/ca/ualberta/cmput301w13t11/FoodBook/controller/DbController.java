package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.util.ArrayList;

import android.content.ContentValues;

import ca.ualberta.cmput301w13t11.FoodBook.RecipeApplication;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

public class DbController {
	
	/* created by Activities and ServerClient to interact with db
	 * 
	 */
	public DbController() {
		
	}

	public ArrayList<Recipe> getUserRecipes() {
		DbManager db = RecipeApplication.getDbManager();
		return db.getUserRecipes();
	}
	
	public void addRecipe(Recipe recipe) {
		
	}
}
