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
	
	/**
	 * Test RecipeToMap() for error in ContentValues creation.
	 */
	public void testIngredientToMap()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		Ingredient ingredient = new Ingredient("test", "test", 100);
		
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Ingredient.class;
			Method method = idb.getClass().getDeclaredMethod("IngredientToMap", args);
			method.setAccessible(true);
			ContentValues cv = (ContentValues) method.invoke(idb, ingredient);
			assertTrue("Returned ContentValues object should not be null.", cv != null);
			assertTrue("Names should be the same.", ingredient.getName().equals(cv.getAsString("name")));
			assertTrue("Units should be the same.", ingredient.getUnit().equals(cv.getAsString("unit")));
			assertTrue("Titles should be the same.", ingredient.getQuantity() == (cv.getAsFloat("quantity")));
			
		} catch (NoSuchMethodException nsme) {
			fail("NoSuchMethodException");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException");
		} catch (IllegalAccessException e) {
			fail("IllegalAccessException");
		} catch (InvocationTargetException e) {
			fail("InvocationTargetException");
		}
		
	}

}
