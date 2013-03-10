package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.RecipeApplication;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

public class DbController {
	
	public DbController() {
		
	}

	public ArrayList<Recipe> getUserRecipes() {
		DbManager db = RecipeApplication.getDbManager();
		return db.getUserRecipes();
	}
}
