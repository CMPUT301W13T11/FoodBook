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

    // the actual database 
    protected static SQLiteDatabase db;
    protected static DbOpenHelper dbHelper;
    
    // singleton pattern implementation
    private static DbManager instance = null;
    
    //Used to ensure that multiple connections to the database are not opened and
    //also to ensure that multiple database helpers do not open.
    private static boolean has_executed = false;
    
    // name of database file
    private String dbFileName = "RecipeApplicationDb";
    
    // name of tables
    protected String UserRecipes = "UserRecipes";
    protected String ResultsRecipes = "ResultsRecipes";
    
    // SQL queries
    private String getUserRecipesSQL = "SELECT * FROM " + UserRecipes;
    private String getResultRecipesSQL = "SELECT * FROM " + ResultsRecipes;
    
    
    // for the controllers
    /**
     * Protected constructor because we're using the singleton pattern.
     */
    protected DbManager(Context context) {
    	super();
    	// open or create the sqlite db accordingly
    	if (DbManager.has_executed == false) {
    		// Use the has_executed flag to ensure we do not open
    		// multiple connections to the database and we do not
    		// create multiple dbHelper objects.
    		dbHelper = new DbOpenHelper(context, dbFileName);
    		db = dbHelper.getWritableDatabase();
    		DbManager.has_executed = true;
    	}
    }
    
    /**
     * @return True if DbManager constructor has been called before, false if not
     */
    public static boolean getHasExecuted()
    {
    	return has_executed;
    }
    
    /**
     * Get instance of the singleton DbManager.
     * @return The instance of the class.
     */
    public static DbManager getInstance(Context context) {
    	if (instance == null) {
    		// db instance doesn't exist, create new one
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
     * Gets all the Ingredients associated with the recipe identified by its URI.
     * @param uri The URI of the recipe whose ingredients we are fetching.
     * @return An ArrayList of the Ingredients associated with the recipe.
     */
    protected ArrayList<Ingredient> getRecipeIngredients(long uri) {
    	Cursor cursor = db.rawQuery("Select * From RecipeIngredients Where recipeURI = " + uri, null);
    	cursor.moveToFirst();
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
    
}
    
