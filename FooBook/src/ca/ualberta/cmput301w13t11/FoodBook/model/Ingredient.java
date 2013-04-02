package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.content.ContentValues;
import java.util.Collection;

/**
 * Class which models an ingredient (as part of a recipe, or in the User's MyIngredients Db).
 * @author Marko Babic
 *
 */
public class Ingredient {

	private String name;
	private String unit;
	private float quantity;
	
	/**
	 * Constructor -- creates an Ingredient object from the given parameters.
	 * @param name The name of the Ingredient.
	 * @param unit The unit type of the Ingredient.
	 * @param quantity The quantity of this Ingredient.
	 */
	public Ingredient(String name, String unit, float quantity)
	{
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
	}
	
	/**
	 * @return The name of the ingredient.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @return The unit of the ingredient.
	 */
	public String getUnit()
	{
		return this.unit;
	}
	
	/**
	 * @return The quantity attribute of the ingredient.
	 */
	public float getQuantity()
	{
		return this.quantity;
	}
	
	/**
	 * @param name The new name for the ingredient; this.name = name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @param unit The new unit for the ingredient; this.unit = unit
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	
	/**
	 * @param quantity The new quantity for the ingredient; this.quantity = quantity
	 */
	public void setQuantity(float quantity)
	{
		this.quantity = quantity;
	}
	
    /**
     * Converts an Ingredient object to a ContentValues object to be stored in the database.
     * @param ingred The ingredient to be transformed.
     * @return An appropriately transformed cop of the Ingredient for database storage.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("unit", unit);
        values.put("quantity", quantity);
        return values;
    }
	
    /**
     * @return a string representation of the object
     */
    public String toString(){
    	return name + " " + quantity + " " + unit ;
    }

}
