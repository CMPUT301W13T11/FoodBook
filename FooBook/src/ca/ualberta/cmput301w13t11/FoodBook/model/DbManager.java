package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * Singleton class that manages the application's database.
 * @author Mark Tupala
 *
 */
public class DbManager extends FModel<FView> {

	// the 
    private SQLiteDatabase db;
    
    // singleton pattern implementation
    private static DbManager instance = null;
    
    // create table SQL statements
    private String createRecipeTable = "CREATE TABLE UserRecipes (URI text, title text, author text, instructions text)";
    private String createUserIngredientTable = "CREATE TABLE UserIngredients (name text, unit text, quantity text)";
    private String createRecipeIngredientTable = "CREATE TABLE RecipeIngredients (recipeURI text, name text, unit text, quantity text)";

    // SQL queries
    private String getUserRecipesSQL = "FROM UserRecipes SELECT *";
    private String getUserIngredientsSQL = "FROM UserIngredients SELECT *";
    
    // for the controllers
    public void save() {
    }
    
    public void load() {
      
    }
    
    /**
     * Get instance of the singleton DbManager.
     * @return The instance of the class.
     */
    public static DbManager getInstance()
    {
    	if (instance == null)
    			instance = new DbManager();
    	return instance;
    }
    
    public ArrayList<Recipe> getUserRecipes() {
    	Cursor cursor = db.rawQuery(getUserRecipesSQL, null);
    	return CursorToRecipes(cursor);
    }

    public void insert(Recipe recipe) {
      ContentValues values = RecipeToMap(recipe);
      db.insert("UserRecipes", null, values);
      for (Ingredient ingred : recipe.getIngredients()) {
          insert(ingred, recipe.getUri());
      }
    }
    
    public void delete(Recipe recipe) {
      db.delete("UserRecipes", "URI = " + recipe.getUri(), null);
    }

    public void insert(Ingredient ingred) {
      ContentValues values = IngredientToMap(ingred);
      db.insert("UserIngredients", null, values);
    }
    
    public void delete(Ingredient ingred) {
      db.delete("UserIngredients", "name = " + ingred.getName(), null);
    }

    private void insert(Ingredient ingred, long recipeURI) {
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

    /**
     * Converts an Ingredient object to a ContentValues object to be stored in the database.
     * @param ingred The ingredient to be transformed.
     * @return An appropriately transformed cop of the Ingredient for database storage.
     */
    private ContentValues IngredientToMap(Ingredient ingred) {
        ContentValues values = new ContentValues();
        values.put("name", ingred.getName());
        values.put("quantity", ingred.getQuantity());
        values.put("unit", ingred.getUnit());
        return values;
    }
    
}
    
