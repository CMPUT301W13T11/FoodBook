package ca.ualberta.cmput301w13t11.FoodBook.controller;

import java.util.ArrayList;

import android.content.Context;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;

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
    private static IngredientsDbManager ingredsManager;
        
    // singleton pattern implementation
    private static DbController instance = null;
        
    /* 
     * private constructor because we're using the singleton pattern.
     */
    private DbController(Context context) {
    	db = DbManager.getInstance(context);
    	recipesManager = RecipesDbManager.getInstance(context);
    	ingredsManager = IngredientsDbManager.getInstance(context);
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
     * delete view from model's array of views
     * @return void
     */
    public void deleteView(FView<DbManager> view) {
        db.deleteView(view);
    }
    
    /**
     * @return Returns an ArrayList containing all the Recipes the user has stored on their device.
     */
        public ArrayList<Recipe> getUserRecipes() {
                return recipesManager.getRecipes(getUserRecipesSQL);
        }

    /**
     * @return Returns an ArrayList containing all the Recipes stored from search
     */
        public ArrayList<Recipe> getStoredRecipes() {
                return recipesManager.getRecipes(getResultRecipesSQL);
        }
        
        /**
         * Adds the given Recipe to the database.
         * @param recipe The recipe to add.
         */
        public void addRecipe(Recipe recipe) {
                recipesManager.insertRecipe(recipe, "UserRecipes");
                db.notifyViews();
        }
        
        /**
         * Deletes the given Recipe from the database.
         * @param recipe The recipe to delete.
         */
        public void deleteRecipe(Recipe recipe)
        {
                recipesManager.deleteRecipe(recipe);
                db.notifyViews();
        }
        
        /**
         * Associates the Ingredient argument correctly with the given Recipe in the database.
         * @param ingredient The ingredient to add to the given Recipe.
         * @param recipe The Recipe to which the ingredient is to be added.
         */
        public void addIngredientToRecipe(Ingredient ingredient, Recipe recipe)
        {
                db.insert(ingredient, recipe.getUri());
                db.notifyViews();
        }
        
        /* *****************************************************************
         USE THESE METHODS FOR ADDING, EDITING, RETRIEVING, AND DELETING
         THE USER'S PERSONAL INGREDIENTS
        ********************************************************************* */
        
        /**
         * Adds the given Ingredient to the database.
         * @param ingredient The Ingredient to add.
         */
        public void addIngredient(Ingredient ingredient) {
                ingredsManager.insert(ingredient);
                db.notifyViews();
        }
        
        /**
         * @return Returns an ArrayList containing all the Recipes the user has stored on their device.
         */
        public ArrayList<Ingredient> getUserIngredients() {
        	return ingredsManager.get();
        }
        
        /**
         * Deletes the given Ingredient from the database.
         * @param ingredient The Ingredient to delete.
         */
        public void deleteIngredient(Ingredient ingred) {
                ingredsManager.delete(ingred);
                db.notifyViews();
        }
}
