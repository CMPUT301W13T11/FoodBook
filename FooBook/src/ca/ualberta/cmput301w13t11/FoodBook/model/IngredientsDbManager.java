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
      ContentValues values = IngredientToMap(ingred);
      db.insert(tableName, null, values);
    }

    /**
     * Retrieves the user's Ingredients from the database.
     * @return ArrayList of Ingredients
     */
    public ArrayList<Ingredient> get() {
	    Cursor cursor = db.rawQuery(getSQL, null);
	    cursor.moveToFirst();
	    return CursorToIngredients(cursor);
    }
    
    /**
     * Deletes the given Ingredient from the database.
     * @param ingred the ingredient to be deleted.
     */
    public void delete(Ingredient ingred) {
      db.delete(tableName, "name = " + ingred.getName(), null);
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

    /**
     * Given a cursor, convert it to an ArrayList of Ingredients.
     * @param cursor The cursor over which we will iterate to get ingredients from.
     * @return An ArrayList of Ingredients.
     */
    protected ArrayList<Ingredient> CursorToIngredients(Cursor cursor) {
        ArrayList<Ingredient> ingreds = new ArrayList<Ingredient>();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(0);
            String unit = cursor.getString(1);
            float quantity = cursor.getFloat(2);
            Ingredient ingred = new Ingredient(name, unit, quantity);
            ingreds.add(ingred);
            cursor.moveToNext();
        }
        return ingreds;
    }
	
	
}
