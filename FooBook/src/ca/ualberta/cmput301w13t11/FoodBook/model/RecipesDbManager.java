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
	 * store results from server.
	 * @return should i return boolean for success?
	 */
	public void storeRecipes(ArrayList<Recipe> recipes) {
	    db.delete(ResultsRecipes, null, null);
	    for (Recipe recipe : recipes) {
	        insertRecipe(recipe, ResultsRecipes);
	    }
	    notifyViews();
	}
	
	/**
	 * Inserts a recipe into the table.
	 * @param recipe The Recipe to be stored in the table.
	 * @param The name of the table into which the recipe is to be stored.
	 */
	public void insertRecipe(Recipe recipe, String tableName) {
	    ContentValues values = RecipeToMap(recipe);
	    db.insert(tableName, null, values);
	    for (Ingredient ingred : recipe.getIngredients()) {
	        insert(ingred, recipe.getUri());
	    }
	}
	
	/**
	 * Returns an ArrayList of all the Recipes stored in the table.
	 * @return An ArrayList of all the Recipes stored in the table.
	 */
	public ArrayList<Recipe> getRecipes(String query) {
	    Cursor cursor = db.rawQuery(query, null);
	    cursor.moveToFirst();
	    return CursorToRecipes(cursor);
	}

	/**
	 * Deletes the given Recipe from the database.
	 * @param recipe The Recipe to be deleted.
	 */
	public void deleteRecipe(Recipe recipe) {
	    db.delete(UserRecipes, "URI = " + recipe.getUri(), null);
	}

	// PRIVATE METHODS *********************************************************
        /**
         * Converts a Recipe to a ContentValues object to be stored in the database.
         * @param recipe The recipe to be converted.
         * @return An appropriately transformed copy of the Recipe for database storage.
         */
        protected ContentValues RecipeToMap(Recipe recipe) {
            ContentValues values = new ContentValues();
            values.put("URI", recipe.getUri());
            values.put("author", recipe.getAuthor().getName());
            values.put("title", recipe.getTitle());
            values.put("instructions", recipe.getInstructions());
            return values;
        }

        /**
         * Given a cursor, convert it to an ArrayList of Recipes.
         * @param cursor The cursor over which we will iterate to get recipes from.
         * @return An ArrayList of Recipes.
         */
        protected ArrayList<Recipe> CursorToRecipes(Cursor cursor) {
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
            while (!cursor.isAfterLast()) {
                long uri = cursor.getLong(0);
                User author = new User(cursor.getString(2));
                String title = cursor.getString(1);
                String instructions = cursor.getString(3);
                ArrayList<Ingredient> ingredients = getRecipeIngredients(uri);
                Recipe recipe = new Recipe(uri, author, title, instructions, ingredients);
                recipes.add(recipe);
                cursor.moveToNext();
            }
            return recipes;
        }
        
}
