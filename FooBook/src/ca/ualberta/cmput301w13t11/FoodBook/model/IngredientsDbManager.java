package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
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

	public IngredientsDbManager(Context context) {
		super(context);
	}

	/**
	 * Gets the instance of the IngredientsDbManager -- or creates it, if necessary.
	 * @param context Context needed to execute database creation statements (if necessary).
	 * @return The instance of IngredientsDbManager.
	 */
	public static IngredientsDbManager getInstance(Context context) {
		// call super to create Db if necessary
		DbManager.getInstance(context);
		if (instance == null)
			instance = new IngredientsDbManager(context);
		return instance;
	}

	/**
	 * @return The instance of the RecipesDbManager.
	 */
	public static IngredientsDbManager getInstance() {
		return instance;
	}

	/**
	 * Inserts the given Ingredient into the database.
	 * @param ingred The ingredient to be stored.
	 * @return True on success, False on failure
	 */
	public boolean insert(Ingredient ingred) {
		ContentValues values = ingred.toContentValues();
		try {
			db.insert(tableName, null, values);
        } catch (SQLiteException sqle) {
    		sqle.printStackTrace();
    		return false;
    	}
		return true;
	}

	/**
	 * Retrieves the user's Ingredients from the database.
	 * @return ArrayList of Ingredients
	 */
	public ArrayList<Ingredient> get() {
		Cursor cursor;
		try {
			cursor = db.rawQuery(getSQL, null);
        } catch (SQLiteException sqle) {
    		sqle.printStackTrace();
    		return new ArrayList<Ingredient>();
    	}
		return cursorToMyIngredients(cursor);
	}

	/**
	 * Deletes the given Ingredient from the database.
	 * @param ingred the ingredient to be deleted.
	 * @return True on success, False on failure
	 */
	public boolean delete(Ingredient ingred) {
		try {
			db.delete(tableName, "name = '" + ingred.getName()+"'", null);
        } catch (SQLiteException sqle) {
    		sqle.printStackTrace();
    		return false;
    	}
		return true;
	}

	/**
	 * Update the entry in the Db with the uri given the Recipe parameter with the values
	 * given by the Recipe parameter.
	 * @param recipe The updated recipe to be stored in the database.
	 * @return True on success, False on failure
	 */
	public boolean updateIngredient(Ingredient ingred, Ingredient oldIngred) {
		String filter = "name='" + oldIngred.getName() 
		        + "' and unit='" + oldIngred.getUnit()
		        + "' and quantity='" + oldIngred.getQuantity() + "'";
		ContentValues args = new ContentValues();
		args.put("name", ingred.getName());
		args.put("unit", ingred.getUnit());
		args.put("quantity", ingred.getQuantity());
		try {
			db.update(tableName, args, filter, null);
        } catch (SQLiteException sqle) {
    		sqle.printStackTrace();
    		return false;
    	}
		return true;
	}

    /**
     * Given a cursor, convert it to an ArrayList of Ingredients.
     * @param cursor The cursor over which we will iterate to get ingredients from.
     * @return An ArrayList of Ingredients.
     */
    private ArrayList<Ingredient> cursorToMyIngredients(Cursor cursor) {
        ArrayList<Ingredient> ingreds = new ArrayList<Ingredient>();
        cursor.moveToFirst();
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
	
	public String getTableName() {
		return this.tableName;
	}


}
