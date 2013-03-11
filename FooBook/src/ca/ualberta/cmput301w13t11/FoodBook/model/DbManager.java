package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * Singleton class that manages the application's database.
 * @author Mark Tupala
 *
 */
public class DbManager extends FModel<FView> {

	// the 
    protected SQLiteDatabase db;
    protected DbOpenHelper dbHelper;
    
    // singleton pattern implementation
    private static DbManager instance = null;
    
    // name of database file
    private String dbFileName = "RecipeApplicationDb.sqlite";
    
    // SQL queries
    private String getUserRecipesSQL = "FROM UserRecipes SELECT *";
    private String getResultRecipesSQL = "FROM ResultRecipes SELECT *";
    private String getUserIngredientsSQL = "FROM UserIngredients SELECT *";
    
    // for the controllers
    public void save() {
    }
    
    public void load() {
      
    }
    
    /**
     * Protected constructor because we're using the singleton pattern.
     */
    protected DbManager(Context context) {
    	// open or create the sqlite db accordingly
    	dbHelper = new DbOpenHelper(context, dbFileName);
    	db = dbHelper.getWritableDatabase();
    }
    
    /**
     * Get instance of the singleton DbManager.
     * @return The instance of the class.
     */
    public static DbManager getInstance(Context context) {
    	if (instance == null) {
    		// db instance doesn't exist, create new one
    		// check to see if sqlite database exists on local, create it if not
    		//db = openOrCreateDatabase(DbPath);
    		instance = new DbManager(context);
    	}
    			
    	return instance;
    }

    /**
     * Get instance of the singleton DbManager, when we
     * know that it exists
     * @return The instance of the class.
     */
    public static DbManager getInstance() {	
    	return instance;
    }
    
    /**
     * store results from server.
     * @return should i return boolean for success?
     */
    public void storeResults(ArrayList<Recipe> recipes) {
    	db.delete("ResultRecipes", null, null);
    	for (Recipe recipe : recipes) {
    		insert(recipe, "ResultRecipes");
    	}
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
     * Inserts a recipe into the database.
     * @param recipe The Recipe to be stored in the database.
     * @param The name of the table into which the recipe is to be stored.
     */
    public void insert(Recipe recipe, String tableName) {
      ContentValues values = RecipeToMap(recipe);
      db.insert(tableName, null, values);
      for (Ingredient ingred : recipe.getIngredients()) {
          insert(ingred, recipe.getUri());
      }
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
    
