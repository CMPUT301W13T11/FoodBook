package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
/**
 * Unit tests for the IngredientsDbManager class.
 * @author mbabic
 *
 */
public class IngredientsDbManagerTests extends AndroidTestCase {

	private IngredientsDbManager idb = null;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		
	}
	
	/**
	 * Test the getInstance() method to ensure a non-null instance of IngredientsDbManager
	 */
	public void testGetInstance()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		assertTrue("Test should not be null.", idb != null);
		assertTrue("getInstance() (method w/o args) should not return null now.", IngredientsDbManager.getInstance() != null);
	}
	

}
