package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/**
 * Implements the functionality required to manage the Ingredients database, which
 * is the User maintained database of ingredients meant to reflect the contents of their
 * kitchen.
 * <<Singleton>>
 * @author Mark Tupala, Marko Babic
 */
public class IngredientsDbManager extends DbManager {
	
	private static IngredientsDbManager instance = null;
	private String tableName = "UserIngredients";
	private String getSQL = "SELECT * FROM " + tableName;
	
	public IngredientsDbManager(Context context)
	{
		super(context);
	}

	/**
	 * Gets the instance of the IngredientsDbManager -- or creates it, if necessary.
	 * @param context Context needed to execute database creation statements (if necessary).
	 * @return The instance of IngredientsDbManager.
	 */
	public static IngredientsDbManager getInstance(Context context)
	{
		// call super to create Db if necessary
		DbManager.getInstance(context);
		if (instance == null)
			instance = new IngredientsDbManager(context);
		return instance;
	}
	
	/**
	 * @return The instance of the RecipesDbManager.
	 */
	public static IngredientsDbManager getInstance()
	{
		return instance;
	}
	
    /**
     * Inserts the given Ingredient into the database.
     * @param ingred The ingredient to be stored.
     */
    public void insert(Ingredient ingred) {
      ContentValues values = ingred.toContentValues();
      db.insert(tableName, null, values);
    }

    /**
     * Retrieves the user's Ingredients from the database.
     * @return ArrayList of Ingredients
     */
    public ArrayList<Ingredient> get() {
	    Cursor cursor = db.rawQuery(getSQL, null);
	    return cursorToIngredients(cursor);
    }
    
    /**
     * Deletes the given Ingredient from the database.
     * @param ingred the ingredient to be deleted.
     */
    public void delete(Ingredient ingred) {
      db.delete(tableName, "name = " + ingred.getName(), null);
    }  
	
}
