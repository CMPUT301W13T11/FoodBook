package ca.ualberta.cmput301w13t11.FoodBank.model;

/**
 * Models a user of the application.  May be extended in the future to accommodate
 * functionality as needed.
 * @author 
 *
 */
public class User {

	private String name;
	
	public User(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}
