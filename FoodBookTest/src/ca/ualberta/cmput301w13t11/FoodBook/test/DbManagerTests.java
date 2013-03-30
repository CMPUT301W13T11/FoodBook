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
 * IngredientsDbManager, RecipesDbManager, ResultsDbManager).
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

	

	
	/**
	 * Test RecipeToMap() to ensure it returns correct ContentValue types.
	 */
	public void testRecipeToMap()
	{
		db = DbManager.getInstance(this.getContext());
		Recipe recipe = Recipe.generateTestRecipe();
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Recipe.class;
			Method method = db.getClass().getDeclaredMethod("RecipeToMap", args);
			method.setAccessible(true);
			ContentValues cv = (ContentValues) method.invoke(db, recipe);
			assertTrue("ContentValue object should not be null", cv != null);
			assertTrue("URI of created CV should be equal URI of recipe.", recipe.getUri() == cv.getAsLong("URI"));
			assertTrue("Author names should be equal.", recipe.getAuthor().getName().equals(cv.getAsString("author")));
			assertTrue("Titles should be the same.", recipe.getTitle().equals(cv.getAsString("title")));
			assertTrue("Instructions should be the same.", recipe.getInstructions().equals(cv.getAsString("instructions")));
			
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
	
	/**
	 * Test RecipeToMap() for error in ContentValues creation.
	 */
	public void testIngredientToMap()
	{
		db = DbManager.getInstance(this.getContext());
		Ingredient ingredient = new Ingredient("test", "test", 100);
		
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Ingredient.class;
			Method method = db.getClass().getDeclaredMethod("IngredientToMap", args);
			method.setAccessible(true);
			ContentValues cv = (ContentValues) method.invoke(db, ingredient);
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
	
	/*
	 * 
	 */

}
