package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;

public class RecipesDbManagerTests extends AndroidTestCase {

	private static RecipesDbManager rdb = null;
	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	
	
	/**
	 * Test both getInstance() methods ensure that a non-null instance of RecipesDbManager is returned by getInstance().
	 */
	public void testGetInstance()
	{
		rdb = RecipesDbManager.getInstance(this.getContext());
		assertTrue("getInstance should not return null.", rdb != null);
		assertTrue("getInstance w/o args should not return null now.", ResultsDbManager.getInstance() != null);
	}
	
	
}
