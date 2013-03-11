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
    
    /**
     * Inserts the given Ingredient into the database such that it is associated with the
     * recipe identified by recipeURI.
     * @param ingred The ingredient to be inserted.
     * @param recipeURI The URI of the Recipe with which to associate the Ingredient.
     */
    public void insert(Ingredient ingred, long recipeURI) {
        ContentValues values = new ContentValues();
        values.put("recipeURI", recipeURI);
        values.put("name", ingred.getName());
        values.put("unit", ingred.getUnit());
        values.put("quantity", ingred.getQuantity());
        db.insert("RecipeIngredients", null, values);
    }
       
    
    /**
     * Given a cursor, convert it to an ArrayList of Recipes.
     * @param cursor The cursor over which we will iterate to get recipes from.
     * @return An ArrayList of Recipes.
     */
    private ArrayList<Recipe> CursorToRecipes(Cursor cursor) {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		while (!cursor.isAfterLast()) {
			long uri = cursor.getLong(0);
			User author = new User(cursor.getString(1));
			String title = cursor.getString(2);
			String instructions = cursor.getString(3);
			ArrayList<Ingredient> ingredients = getRecipeIngredients(uri);
			Recipe recipe = new Recipe(uri, author, title, instructions, ingredients);
			recipes.add(recipe);
			cursor.moveToNext();
		}
		return recipes;
    }
    
    /**
     * Gets all the Ingredients associated with the recipe identified by its URI.
     * @param uri The URI of the recipe whose ingredients we are fetching.
     * @return An ArrayList of the Ingredients associated with the recipe.
     */
    private ArrayList<Ingredient> getRecipeIngredients(long uri) {
    	Cursor cursor = db.rawQuery("From RecipeIngredients Select * Where uri = " + uri, null);
    	ArrayList<Ingredient> ingreds = new ArrayList<Ingredient>();
    	while (!cursor.isAfterLast()) {
    		String name = cursor.getString(0);
    		String units = cursor.getString(1);
    		float quantity = cursor.getFloat(2);
    		Ingredient ingred = new Ingredient(name, units, quantity);
    		ingreds.add(ingred);
    		cursor.moveToNext();
    	}
    	return ingreds;
    }
    
    /**
     * Converts a Recipe to a ContentValues object to be stored in the database.
     * @param recipe The recipe to be converted.
     * @return An appropriately transformed copy of the Recipe for database storage.
     */
    private ContentValues RecipeToMap(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put("uri", recipe.getUri());
        values.put("author", recipe.getAuthor().getName());
        values.put("title", recipe.getTitle());
        values.put("instructions", recipe.getInstructions());
        return values;
    }

}
