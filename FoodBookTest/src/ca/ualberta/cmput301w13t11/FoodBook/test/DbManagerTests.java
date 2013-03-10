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
 * @author mbabic
 *
 */
public class DbManagerTests extends AndroidTestCase {

	private DbManager db = null;
//	public DbManagerTests()
//	{
//		super("ca.ualberta.cmput301w13t11.FoodBook.model", DbManagerTestActivity.class);
//	}
	protected void setUp() throws Exception
	{
		super.setUp();
	}	

	public void testGetInstance()
	{
		db = DbManager.getInstance();
		if (db == null)
			fail("getInstance failed");
		assertTrue(true);
	}
	
	/*
	 * Test the getInstance() method -- should not return null.
	 */
	public void testInsert() 
	{
		Recipe recipe = Recipe.generateTestRecipe();
		db = DbManager.getInstance();
		if (db == null)
			fail();
		
		db.insert(recipe);
		assertTrue(true);
	}
	
	/*
	 * Test RecipeToMap() 
	 */
	public void testRecipeToMap()
	{
		db = DbManager.getInstance();
		Recipe recipe = Recipe.generateTestRecipe();
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Recipe.class;
			Method method = db.getClass().getDeclaredMethod("RecipeToMap", args);
			method.setAccessible(true);
			ContentValues cv = (ContentValues) method.invoke(db, recipe);
			assertTrue(cv != null);
			
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
	
	/*
	 * Test RecipeToMap() 
	 */
	public void testIngredientToMap()
	{
		db = DbManager.getInstance();
		Ingredient ingredient = new Ingredient("test", "test", 100);
		
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Ingredient.class;
			Method method = db.getClass().getDeclaredMethod("IngredientToMap", args);
			method.setAccessible(true);
			ContentValues cv = (ContentValues) method.invoke(db, ingredient);
			assertTrue(cv != null);
			
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
