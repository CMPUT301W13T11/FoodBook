package ca.ualberta.cmput301w13t11.FoodBook.model;

/**
 * Models a user of the application.  May be extended in the future to accommodate
 * functionality as needed.
 * @author Marko Babic
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

}
