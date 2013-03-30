package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

/**
 * Unit tests for the DbManager class.
 * Please note that the majority of DbManger methods require a subclass to properly identify table names,
 * the unit tests for these methods can be found in the set of tests for each of DbManagers subclass (namely,
 * IngredientsDbManager, RecipesDbManager, ResultsDbManager -- as appropriate).
 * @author mbabic
 *
 */
public class DbManagerTests extends AndroidTestCase {

	private DbManager db = null;

	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	/**
	 * Test to ensure that a non-null instance of DbManager is returned by getInstance().
	 */
	public void testGetInstance()
	{
		db = DbManager.getInstance(this.getContext());
		assertTrue("getInstance should not return null.", db != null);
	}

}
