package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.util.Log;
import ca.ualberta.cmput301w13t11.FoodBook.BogoPicGen;
import ca.ualberta.cmput301w13t11.FoodBook.FoodBookApplication;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

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
		
		
		assertTrue("insertRecipePhotos() should return true.", ret == true);	
	}
	
	/**
	 * Test the functionality of removeRecipe().
	 * Insure that when attempting to remove a recipe known to exist in the Db that the method returns true.
	 */
	public void testRemoveRecipe1()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager");
		}
		db.insertRecipe(recipe);
		boolean ret = db.removeRecipe(recipe);
		assertTrue("removeRecipe() should return true", ret == true);
		
	}
	
	/**
	 * Test the functionality of removeRecipe()
 	 * Insure that when attempting to remove a recipe known to not exist in the Db that the method returns false.
	 */
	public void testRemoveRecipe2()
	{
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager");
		}
		Recipe recipe = new Recipe(0, new User(""), "", "", new ArrayList<Ingredient>(), new ArrayList<Photo>());
		boolean ret = db.removeRecipe(recipe);
		assertTrue("removeRecipe() should return false", ret == false);
		
	}
	
	/**
	 * Test the functionality of the getRecipe() method.
	 * Insure that a recipe of known parameters which exists in the database is returned correctly.
	 */
	public void testGetRecipe1()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager");
		}
		db.insertRecipe(recipe);
		
		Recipe ret = db.getRecipe(recipe.getUri());
		assertTrue("Returned recipe title should be \"test\".", ret.getTitle().equals(recipe.getTitle()));
		assertTrue("Return recipe user name shoud be \"tester\"", ret.getAuthor().getName().equals(recipe.getAuthor().getName()));
		
		/* Test that all ingredients are returned in order with correct fields. */

		for (Ingredient retIngredient : ret.getIngredients()) {
			for (Ingredient recipeIngredient : recipe.getIngredients()) {
				assertTrue("Names should be the same.", retIngredient.getName().equals(retIngredient.getName()));
				assertTrue("Units should be the same.", retIngredient.getUnit().equals(retIngredient.getUnit()));
				assertTrue("Quantities should be the same.", retIngredient.getQuantity() == retIngredient.getQuantity());

			}
		}
	}
	
	/**
	 * Test the functionality of the getRecipes() function.
	 * We simply ensure that the returned list is not empty when we know there exists a recipe in the database.
	 */
	public void testGetRecipes()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		db = RecipesDbManager.getInstance(this.getContext());
		if (db == null) {
			fail("failed to get an instance of RecipesDbManager");
		}
		db.insertRecipe(recipe);
		
		ArrayList<Recipe> recipes = db.getRecipes();
		
		assertTrue("Returned list should not be empty.", !recipes.isEmpty());
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
