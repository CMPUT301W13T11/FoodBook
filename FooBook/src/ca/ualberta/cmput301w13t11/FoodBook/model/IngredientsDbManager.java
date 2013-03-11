package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.content.ContentValues;
import android.content.Context;
/**
 * Implements the functionality required to manage the Ingredients database, which
 * is the User maintained database of ingredients meant to reflect the contents of their
 * kitchen.
 * <<Singleton>>
 * @author Mark Tupala, Marko Babic
 */
public class IngredientsDbManager extends DbManager {
	
	private static IngredientsDbManager instance = null;
	
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
      db.insert("UserIngredients", null, values);
    }
    
    /**
     * Deletes the given Ingredient from the database.
     * @param ingred the ingredient to be deleted.
     */
    public void delete(Ingredient ingred) {
      db.delete("UserIngredients", "name = " + ingred.getName(), null);
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
