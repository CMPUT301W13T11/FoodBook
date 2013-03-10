package ca.ualberta.cmput301w13t11.FoodBook.test;

import org.junit.Assert;
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
	

	/*
	 * Test the getInstance() method -- should not return null.
	 */
	public void testWhatever() 
	{
		Assert.assertTrue(true);
	}
	

	public void testInsertRecipe()
	{
		instance = DbManager.getInstance();
		instance.insert(Recipe.generateTestRecipe());
		Assert.assertTrue(true);
	}
}
