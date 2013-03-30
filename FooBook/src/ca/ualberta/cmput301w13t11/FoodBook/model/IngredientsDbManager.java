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
		return cursorToMyIngredients(cursor);
	}

	/**
	 * Deletes the given Ingredient from the database.
	 * @param ingred the ingredient to be deleted.
	 */
	public void delete(Ingredient ingred) {
		db.delete(tableName, "name = '" + ingred.getName()+"'", null);
	}

	// *********************************************
	// UPDATE INGREDIENT
	// *********************************************

	/**
	 * Update the entry in the Db with the uri given the Recipe parameter with the values
	 * given by the Recipe parameter.
	 * @param recipe The updated recipe to be stored in the database.
	 */
	public void updateIngredient(Ingredient ingred, String oldName) {
		updateField(oldName, "quantity", Float.toString(ingred.getQuantity()));
		updateField(oldName, "unit", ingred.getUnit());
		updateField(oldName, "name", ingred.getName());
	}

	/**
	 * Updates the title of the Ingredient with the given name.
	 * TODO: consider making this a private part of an "update recipe function"
	 * @param uri The URI of the Recipe to be updated.
	 * @param tableName The name of the table in which the Recipe resides.
	 * @param newTitle The new title of the Recipe.
	 */
	private void updateField(String name, String field, String value) {
		String filter = "name='" + name + "'";
		ContentValues args = new ContentValues();
		args.put(field, value);
		db.update(tableName, args, filter, null);
	}

	/**
	 * Converts the cursor, retrieved by querying the MyIngredientsTable, and converts it to an ArrayList of ingredients.
	 * @param cursor The cursor over which we will iterate to get the ingredients from.
	 * @return An ArrayList of ingredients.
	 */
	private ArrayList<Ingredient> cursorToMyIngredients(Cursor cursor) {
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String name = cursor.getString(0);
			String unit = cursor.getString(1);
			float quantity = cursor.getFloat(2);
			Ingredient ingredient = new Ingredient(name, unit, quantity);
			ingredients.add(ingredient);
			cursor.moveToNext();
		}
		return ingredients;
	}
}
