package ca.ualberta.cmput301w13t11.FoodBook.model;

/**
 * Class which models an ingredient object. (TODO: better class description)
 * @author Marko
 *
 */
public class Ingredient {

	private String name;
	private String unit;
	private float quantity;
	
	public Ingredient(String name, String unit, float quantity)
	{
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getUnit()
	{
		return this.unit;
	}
	
	public float getQuantity()
	{
		return this.quantity;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	
	public void setQuantity(float quantitu)
	{
		this.quantity = quantity;
	}
	
}
