package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.BogoPicGen;
import ca.ualberta.cmput301w13t11.FoodBook.FoodBookApplication;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
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
	 * Test the functionality of the insertRecipe() method.
	 * We simply check to see that a recipe can be inserted into the database without error.
	 */
	public void testInsertRecipe()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager.");
		}
		
		db.insertRecipe(recipe);
		
		/* We arrived here without error, we'll consider this a pass. */
		assertTrue(true);
	}
	
	/**
	 * Test the functionality of the insertRecipeIngredients() method.
	 * We simply check to see that an ingredient can be added to a recipe -- known to exist in the database-- without error.
	 */
	public void testInsertRecipeIngredients()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager");
		}
		db.insertRecipe(recipe);
		
		/* Now we attempt to insert another ingredient into the recipe we know exists in the Db. */
		Ingredient newIngredient = new Ingredient("this", "is a test", 100);
		boolean ret = db.insertRecipeIngredients(newIngredient, recipe.getUri());
		
		
		assertTrue("insertRecipeIngredients() should return true.", ret == true);
	}
	
	
	/**
	 * Test the functionality of the insertRecipePhotos() method.
	 * We simply check to see that a photo can be added to a recipe -- known to exist in the database-- without error.
	 */
	public void testInsertRecipePhotos()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager");
		}
		db.insertRecipe(recipe);
		
		/* Now we attempt to insert photo into the recipe we know exists in the Db. */

		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String path = FoodBookApplication.getApplicationInstance().getSdCardPath();
		String name = Long.toString(System.currentTimeMillis());
		Photo newPhoto = new Photo(name, path);		
		
		boolean ret = db.insertRecipePhotos(newPhoto, bitmap, recipe.getUri());
		
		
		assertTrue("insertRecipeIngredients() should return true.", ret == true);	
	}
	
	
	
	
	
//	/**
//	 * Test the insert(Ingredient, uri) method, success is simply the operation completing without error.
//	 */
//	public void testInsertIngredient() 
//	{
//		Recipe recipe = Recipe.generateTestRecipe();
//		db = RecipesDbManager.getInstance(this.getContext());
//		if (db == null)
//			fail();
//		try {
//			/* Testing private member function, need to use reflection. */
//			Class[] args = new Class[2];
//			args[0] = Ingredient.class;
//			args[1] = long.class;
//			Method method = db.getClass().getDeclaredMethod("insert", args);
//			method.setAccessible(true);
//			method.invoke(db, recipe.getIngredients().get(0), recipe.getUri());
//			/* Got here without error. */
//			assertTrue(true);
//			
//		} catch (NoSuchMethodException nsme) {
//			fail("NoSuchMethodException");
//		} catch (IllegalArgumentException e) {
//			fail("IllegalArgumentException");
//		} catch (IllegalAccessException e) {
//			fail("IllegalAccessException");
//		} catch (InvocationTargetException e) {
//			fail("InvocationTargetException");
//		}
//
//	}
	
}
