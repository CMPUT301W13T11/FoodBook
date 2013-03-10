package ca.ualberta.cmput301w13t11.FoodBook.test;

import org.junit.Test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

/**
 * Unit tests for the DbManager class.
 * @author mbabic
 *
 */
public class DbManagerTests extends AndroidTestCase {


	private DbManager instance = null;
	
	public DbManagerTests()
	{
		super();
	}

	protected void setUp() throws Exception
	{	
		instance = DbManager.getInstance();
		super.setUp();
	}
	
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	/*
	 * Test the getInstance() method -- should not return null.
	 */
	public void testGetInstance() 
	{
		int test = 1;
		instance = DbManager.getInstance();
		assertEquals(1, test);
	}
	

	/*
	 * Test that a Recipe can be inserted into the database.
	 */
	public void testInsertRecipe()
	{
		instance = DbManager.getInstance();
		instance.insert(Recipe.generateTestRecipe());
	}
}
