package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;

/**
 * Controller for the DbManager.
 * @author Mark Tupala, Marko Babic
 *
 */
public class DbController {
        
    // name of tables
    protected String UserRecipes = "UserRecipes";
    protected String ResultsRecipes = "ResultsRecipes";
    protected String UserIngredients = "UserIngredients";
    
    // SQL queries
    private String getUserRecipesSQL = "SELECT * FROM " + UserRecipes;
    
    private String getResultRecipesSQL = "SELECT * FROM " + ResultsRecipes;
    private String getUserIngredientsSQL = "SELECT * FROM " + UserIngredients;
    
    private static DbManager db;
    private static RecipesDbManager recipesManager;
    private static ResultsDbManager resultsManager;
    private static IngredientsDbManager ingredsManager;
     
        
    // singleton pattern implementation
    private static DbController instance = null;
        
    /**
     * Constructor - instantiates the managers with a context.
     */
    private DbController(Context context) {
    	db = DbManager.getInstance(context);
    	recipesManager = RecipesDbManager.getInstance(context);
    	ingredsManager = IngredientsDbManager.getInstance(context);
    	resultsManager = ResultsDbManager.getInstance(context);
    }
        
    /**
     * Get instance of the singleton DbController.
     * @return The instance of the class.
     */
    public static DbController getInstance(Context context, FView<DbManager> view) {
        if (instance == null) {
                // db instance doesn't exist, create new one
                instance = new DbController(context);
        }
        db.addView(view);
        return instance;
    }

    /**
     * Remove given view from the DbManagers list of observers.
     * @param view The view to be removed.
     */
    public void deleteView(FView<DbManager> view) {
        db.deleteView(view);
    }
    
    /* *****************************************************************
    USE THESE METHODS FOR ADDING, EDITING, RETRIEVING, AND DELETING
    THE USER'S PERSONAL RECIPES
   ********************************************************************* */
    
    /**
     * @return Returns an ArrayList containing all the Recipes the user has stored on their device.
     */
    public ArrayList<Recipe> getUserRecipes() {
    	return recipesManager.getRecipes();
    }
    
    /**
     * @return Returns a Recipe the user has stored on their device, given recipe uri
     */
    public Recipe getUserRecipe(long uri) {
    	return recipesManager.getRecipe(uri);
    }

    /**
     * Adds the given Recipe to the database.
     * @param recipe The recipe to add.
     */
    public void addRecipe(Recipe recipe) {
    	recipesManager.insertRecipe(recipe);
    	db.notifyViews();
    }

    /**
     * Deletes the given Recipe from the database.
     * @param recipe The recipe to delete.
     */
    public boolean deleteRecipe(Recipe recipe)
    {	
    	boolean success = recipesManager.removeRecipe(recipe);
    	db.notifyViews();
    	return success;
    }
    
    /**
     * Updates the Recipe with the new information passed by the Recipe parameter.
     * CAUTION: does no error checking, may cause error in Recipe not already in Db, call with caution.
     * @param recipe The Recipe to be updated.
     */
    public void updateRecipe(Recipe recipe)
    {
    	recipesManager.updateRecipe(recipe);
    	db.notifyViews();
    }   
    
    /* *****************************************************************
    USE THESE METHODS FOR RETRIEVING/SAVING/DELETING PHOTOS TO USER'S RECIPES
   ********************************************************************* */
    /**
     * Associates the Photo argument correctly with the given Recipe in the database.
     * @param photo The photo to add to the given Recipe.
     * @param recipe The Recipe to which the photo is to be added.
     */
    public boolean addPhotoToRecipe(Photo photo, Bitmap bitmap,long uri)
    {
    	boolean status = recipesManager.insertRecipePhotos(photo, bitmap, uri);
    	db.notifyViews();
    	return status;
    }
    
    
    /**
     * @return Returns an ArrayList of Photos for the recipe specified by URI.
     */
    public ArrayList<Photo> getRecipePhotos(long uri) {
    	return recipesManager.getRecipePhotos(uri);
    }
    
    /**
     * Deletes the given photo from the recipePhotos db.
     * @param photo The photo to be deleted.
     * @return True on success, False on failure.
     */
    public boolean deleteRecipePhoto(Photo photo)
    {
    	boolean success = recipesManager.removeRecipePhoto(photo);
    	db.notifyViews();
    	return success;
    }

    /* *****************************************************************
    USE THESE METHODS FOR RETRIEVING/SAVING/DELETING INGREDIENTS TO USER'S RECIPES
   ********************************************************************* */
    /**
     * Associates the Ingredient argument correctly with the given Recipe in the database.
     * @param ingredient The ingredient to add to the given Recipe.
     * @param recipe The Recipe to which the ingredient is to be added.
     */
    public boolean addIngredientToRecipe(Ingredient ingred, long uri)
    {
        boolean status = recipesManager.insertRecipeIngredients(ingred, uri);
        db.notifyViews();
        return status;
    }
    
    
    /**
     * Replace the old ingredients associated with the Recipe specified by the URI parameters
     * with the given list of ingredients.  Returns success/failure.
     * @param ingredients The new list of ingredients with wish to be associated with the specified Recipe.
     * @param uri The URI of the Recipe whose ingredients we wish to replace.
     * @return true on success, false on failure
     */
    public boolean storeRecipeIngredients(ArrayList<Ingredient> ingredients, long uri)
    {
    	boolean success = true;
    	boolean temp = true;
   		recipesManager.removeRecipeIngredients(uri);
    	if (ingredients == null)
    		return false;
    	for (int i = 0; i < ingredients.size(); i++) {
    		temp = recipesManager.insertRecipeIngredients(ingredients.get(i), uri);
    		if (temp = false) {
    			success = false;
    			break;
    		}
    	}
    	return success;
    }
    
    /**
     * @param uri The URI of the Recipe whose Ingredients we wish to retrieve.
     * @return Returns an ArrayList of Ingredients for a given recipe.
     */
    public ArrayList<Ingredient> getRecipeIngredients(long uri) {
        return recipesManager.getRecipeIngredients(uri);
    }
    
    
//    /**
//     * Delete the given ingredient from the Recipe specified by Recipe.
//     * @param ingred The ingredient to be deleted.
//     * @param recipe The Recipe from which we wish to delete the Recipe.
//     * @return true on success, false on failure
//     */
//    public boolean deleteRecipeIngredient(Ingredient ingred, Recipe recipe)
//    {
//        boolean success = recipesManager.removeRecipeIngredient(ingred.getName(), recipe.getUri());
//        db.notifyViews();
//        return success;
//    }
    
    /* *****************************************************************
    USE THESE METHODS FOR RETRIEVING/DELETING RECIPES STORED FROM SEARCH
   ********************************************************************* */
    
    /** 
     * Returns the recipes stores in the ResultsDb.
     * @return Returns an ArrayList of Recipes which contains the Recipes in the ResultsDb.
     */
    public ArrayList<Recipe> getStoredRecipes() {
    	return resultsManager.getRecipes();
    }

    /**
     * Deletes the given Recipe from the ResultsDb.
     * @param recipe The recipe to delete.
     */
    public boolean deleteStoredRecipe(Recipe recipe)
    {   
        boolean success = resultsManager.removeRecipe(recipe);
        db.notifyViews();
        return success;
    }

    /* *****************************************************************
    USE THESE METHODS FOR RETRIEVING PHOTOS/INGREDIENTS FROM SEARCH RESULTS RECIPES
   ********************************************************************* */
    /**
     * Returns a list of the Photos from the recipe specified by the given Uri in the ResultsDb.
     * @param The URI of the Recipe we wish to retrieve.
     * @return Returns an ArrayList of Photos for a given recipe
     */
    public ArrayList<Photo> getStoredRecipePhotos(long uri) {
        return resultsManager.getRecipePhotos(uri);
    }
    
    /**
     * Returns the Ingredients associated with the Recipe in the ResultsDb specified by the given URI.
     * @param The URI of the recipe whose ingredients we wish to recieve.
     * @return An ArrayList of the Ingredients associated with the specified Recipe in the Results Db.
     */
    public ArrayList<Ingredient> getStoredRecipeIngredients(long uri) {
        return resultsManager.getRecipeIngredients(uri);
    }
    
    /* *****************************************************************
         USE THESE METHODS FOR ADDING, EDITING, RETRIEVING, AND DELETING
         THE USER'S PERSONAL INGREDIENTS
     ********************************************************************* */
    /**
     * Adds the given Ingredient to the IngredientsDb.
     * @param ingredient The Ingredient to be stored.
     */
    public void addIngredient(Ingredient ingredient) {
    	ingredsManager.insert(ingredient);
    	db.notifyViews();
    }

    /**
     * Returns a list of the Ingredients currently store in the IngredientsDb.
     * @return An ArrayList of the Ingredients in the IngredientsDb.
     */
    public ArrayList<Ingredient> getUserIngredients() {
    	return ingredsManager.get();
    }

    /**
     * Deletes the given Ingredient from the IngredientsDb.
     * @param ingredient The Ingredient to be deleted.
     */
    public void deleteIngredient(Ingredient ingred) {
    	ingredsManager.delete(ingred);
    	db.notifyViews();
    }
    
    /**
     * Updates the ingredient in the IngredientDb specified by oldName with the new information
     * stored in the ingred parameter.
     * @param ingred The new ingredient information to be stored in the IngredientsDb.
     * @param oldName The name of the Ingredient whose information we wish to overwrite.
     */
    public void updateIngredient(Ingredient ingred, String oldName) {
        ingredsManager.updateIngredient(ingred, oldName);
        db.notifyViews();
    }
}
