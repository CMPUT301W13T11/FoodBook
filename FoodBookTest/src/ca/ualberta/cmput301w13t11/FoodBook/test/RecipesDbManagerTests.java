package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;

public class RecipesDbManagerTests extends AndroidTestCase {

	private static RecipesDbManager db = null;
	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	
	
	/**
	 * Test both getInstance() methods ensure that a non-null instance of RecipesDbManager is returned by getInstance().
	 */
	public void testGetInstance()
	{
		db = RecipesDbManager.getInstance(this.getContext());
		assertTrue("getInstance should not return null.", db != null);
		assertTrue("getInstance w/o args should not return null now.", ResultsDbManager.getInstance() != null);
	}
	
	/**
	 * Test the insert(Ingredient, uri) method, success is simply the operation completing without error.
	 */
	public void testInsertIngredient() 
	{
		Recipe recipe = Recipe.generateTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null)
			fail();
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[2];
			args[0] = Ingredient.class;
			args[1] = long.class;
			Method method = db.getClass().getDeclaredMethod("insert", args);
			method.setAccessible(true);
			method.invoke(db, recipe.getIngredients().get(0), recipe.getUri());
			/* Got here without error. */
			assertTrue(true);
			
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
