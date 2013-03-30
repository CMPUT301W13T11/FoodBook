package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.BogoPicGen;
import ca.ualberta.cmput301w13t11.FoodBook.FoodBookApplication;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

public class ResultsDbManagerTests extends AndroidTestCase {

	private static ResultsDbManager dbm = null;
	protected void setUp() throws Exception
	{
		super.setUp();
	}	


	/**
	 * Test both getInstance() methods ensure that a non-null instance of ResultsDbManager is returned by getInstance().
	 */
	public void testGetInstance()
	{
		dbm = ResultsDbManager.getInstance(this.getContext());
		assertTrue("getInstance should not return null.", dbm != null);
		assertTrue("getInstance w/o args should not return null now.", ResultsDbManager.getInstance() != null);
	}


	/**
	 * Test the functionality of the insertRecipe() method.
	 * We simply check to see that a recipe can be inserted into the database without error.
	 */
	public void testInsertRecipe()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager.");
		}

		dbm.insertRecipe(recipe);

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
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		/* Now we attempt to insert another ingredient into the recipe we know exists in the Db. */
		Ingredient newIngredient = new Ingredient("this", "is a test", 100);
		boolean ret = dbm.insertRecipeIngredients(newIngredient, recipe.getUri());


		assertTrue("insertRecipeIngredients() should return true.", ret == true);
	}


	/**
	 * Test the functionality of the insertRecipePhotos() method.
	 * We simply check to see that a photo can be added to a recipe -- known to exist in the database-- without error.
	 */
	public void testInsertRecipePhotos()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		/* Now we attempt to insert photo into the recipe we know exists in the Db. */

		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String path = FoodBookApplication.getApplicationInstance().getSdCardPath();
		String name = Long.toString(System.currentTimeMillis());
		Photo newPhoto = new Photo(name, path);		

		boolean ret = dbm.insertRecipePhotos(newPhoto, bitmap, recipe.getUri());


		assertTrue("insertRecipePhotos() should return true.", ret == true);	
	}

	/**
	 * Test the functionality of removeRecipe().
	 * Ensure that when attempting to remove a recipe known to exist in the Db that the method returns true.
	 */
	public void testRemoveRecipe1()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);
		boolean ret = dbm.removeRecipe(recipe);
		assertTrue("removeRecipe() should return true", ret == true);

	}

	/**
	 * Test the functionality of removeRecipe()
	 * Ensure that when attempting to remove a recipe known to not exist in the Db that the method returns false.
	 */
	public void testRemoveRecipe2()
	{
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		Recipe recipe = new Recipe(0, new User(""), "", "", new ArrayList<Ingredient>(), new ArrayList<Photo>());
		boolean ret = dbm.removeRecipe(recipe);
		assertTrue("removeRecipe() should return false", ret == false);

	}

	/**
	 * Test the functionality of removeRecipeIngredient()
	 * Ensure that the no errors occur when attempting to remove ingredients from a recipe known to have ingredients.
	 */
	public void testRemoveRecipeIngredients1()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);
		dbm.removeRecipeIngredients(recipe.getUri());

		/* Got here without error, we will consider this a pass. */
		assertTrue(true);
	}



	/**
	 * Test the functionality of the getRecipe() method.
	 * Ensure that a recipe of known parameters which exists in the database is returned correctly.
	 */
	public void testGetRecipe1()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		Recipe ret = dbm.getRecipe(recipe.getUri());
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
	 * Test the functionality of removeRecipeIngredient()
	 * Ensure that all ingredients are actually removed from a recipe.
	 */
	public void testRemoveRecipeIngredients2()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);
		dbm.removeRecipeIngredients(recipe.getUri());
		Recipe ret = dbm.getRecipe(recipe.getUri());

		assertTrue("Upon retrieval, recipe should now have no ingredients.", ret.getIngredients().isEmpty());
	}

	/**
	 * Test the functionality of the getRecipes() function.
	 * We simply ensure that the returned list is not empty when we know there exists a recipe in the database.
	 */
	public void testGetRecipes()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		ArrayList<Recipe> recipes = dbm.getRecipes();

		assertTrue("Returned list should not be empty.", !recipes.isEmpty());
	}

	/**
	 * Test the functionality of the updateRecipeTitle function.
	 * Ensure that after calling the method on a recipe known to exist in the database the
	 * updated title is retrieved.
	 */
	public void testUpdateRecipeTitle()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		DbManager dbManager = DbManager.getInstance(this.getContext());
		/* This is a private method, so we must use reflection. */
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[2];
			args[0] = long.class;
			args[1] = String.class;
			Method method = dbManager.getClass().getDeclaredMethod("updateRecipeTitle", args);
			method.setAccessible(true);
			method.invoke(dbm, recipe.getUri(),"new");

			Recipe ret = dbm.getRecipe(recipe.getUri());
			assertTrue("Recipe's title should now be \"new\"", ret.getTitle().equals("new"));
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
	 * Test the functionality of the updateRecipeInstructions function.
	 * Ensure that after calling the method on a recipe known to exist in the database the
	 * updated instructions are retrieved.
	 */
	public void testUpdateRecipeInstructions()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		DbManager dbManager = DbManager.getInstance(this.getContext());
		/* This is a private method, so we must use reflection. */
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[2];
			args[0] = long.class;
			args[1] = String.class;
			Method method = dbManager.getClass().getDeclaredMethod("updateRecipeInstructions", args);
			method.setAccessible(true);
			method.invoke(dbm, recipe.getUri(),"new instructions");

			Recipe ret = dbm.getRecipe(recipe.getUri());
			assertTrue("Recipe's title should now be \"new instructions\"", ret.getInstructions().equals("new instructions"));
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
	 * Test the functionality of the updateRecipe() method.
	 * Ensure that after updating a recipe known to exist in the database we retrieve
	 * the updated fields when pulling the recipe from the database.
	 */
	public void testUpdateRecipe()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		/* Now change the parameters */
		recipe.setTitle("new");
		recipe.setInstructions("new instructions");
		dbm.updateRecipe(recipe);

		Recipe ret = dbm.getRecipe(recipe.getUri());
		assertTrue("Recipe's title should now be \"new\"", ret.getTitle().equals("new"));
		assertTrue("Recipe's title should now be \"new instructions\"", ret.getInstructions().equals("new instructions"));

	}

	/**
	 * Test the functionality of cursor to ingredients.
	 * Ensure that 
	 */
	public void testCursorToIngredients()
	{
		//protected ArrayList<Ingredient> cursorToIngredients(Cursor cursor) {
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		Cursor cursor = ResultsDbManager.getDb().rawQuery("Select * From " + dbm.ingredsTable + " Where recipeURI = " + recipe.getUri(), null);

		DbManager dbManager = DbManager.getInstance(this.getContext());
		/* This is a private method, so we must use reflection. */
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Cursor.class;
			Method method = dbManager.getClass().getDeclaredMethod("cursorToIngredients", args);
			method.setAccessible(true);
			ArrayList<Ingredient> ret = (ArrayList<Ingredient>) method.invoke(dbm, cursor);

			for (int i = 0; i < ret.size(); i++) {
				assertTrue("Names should be equal.", ret.get(i).getName().equals(recipe.getIngredients().get(i).getName()));
				assertTrue("Units should be equal.", ret.get(i).getUnit().equals(recipe.getIngredients().get(i).getUnit()));
				assertTrue("Quantities should be equal.", ret.get(i).getQuantity() == recipe.getIngredients().get(i).getQuantity());
			}

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
	 * Test the functionality of getRecipeIngredients()
	 * We ensure that when querying for the ingredients of a recipe known to exist in the database with a known
	 * set of ingredients that the correct ingredients are returned to us.
	 */
	public void testGetRecipeIngredients()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		ArrayList<Ingredient> ret = dbm.getRecipeIngredients(recipe.getUri());
		for (int i = 0; i < ret.size(); i++) {
			assertTrue("Names should be equal.", ret.get(i).getName().equals(recipe.getIngredients().get(i).getName()));
			assertTrue("Units should be equal.", ret.get(i).getUnit().equals(recipe.getIngredients().get(i).getUnit()));
			assertTrue("Quantities should be equal.", ret.get(i).getQuantity() == recipe.getIngredients().get(i).getQuantity());
		}
	}

	/**
	 * Test the functionality of cursorToRecipes()
	 * Perform a raw SQL query of the database and ensure that a known Recipe is among the results
	 * and has been completely and correctly converted using the cursor (does not ensure Photos are 
	 * correctly returned, this will have to be done by visual inspection).
	 */
	public void testCursorToRecipes()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		DbManager dbManager = DbManager.getInstance(this.getContext());
		Cursor cursor = ResultsDbManager.getDb().rawQuery(dbm.getGetSQL(), null);

		/* This is a private method, so we must use reflection. */
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Cursor.class;
			Method method = dbManager.getClass().getDeclaredMethod("cursorToRecipes", args);
			method.setAccessible(true);
			ArrayList<Recipe> ret = (ArrayList<Recipe>) method.invoke(dbm, cursor);

			int i = 0;
			for (i = 0; i < ret.size(); i++) {
				if(recipe.getUri() == ret.get(i).getUri()) {
					break;
				}
			}
			assertTrue("We should find the recipe we inserted at the start of the test in the database.", i < ret.size());

			/* If we get here, we found the recipe and need to ensure all fields are as expected */
			assertTrue("Titles should be the same.", ret.get(i).getTitle().equals(recipe.getTitle()));
			assertTrue("Instructions should be the same.", ret.get(i).getInstructions().equals(recipe.getInstructions()));

			Ingredient retTemp, recipeTemp;
			for (int j = 0; j < ret.get(i).getIngredients().size(); j++) {
				retTemp = ret.get(i).getIngredients().get(j);
				recipeTemp = recipe.getIngredients().get(j);
				assertTrue("Names should be the same.", retTemp.getName().equals(recipeTemp.getName()));
				assertTrue("Units should be the same.", retTemp.getUnit().equals(recipeTemp.getUnit()));
				assertTrue("Quantities should be the same.", retTemp.getQuantity() == recipeTemp.getQuantity());
			}

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
	 * Test the functionality of cursorToRecipe()
	 * Perform a raw SQL query of the database and ensure that the recipe known to be returned
	 * has been completely and correctly converted using the cursor (does not ensure Photos are 
	 * correctly returned, this will have to be done by visual inspection).
	 */
	public void testCursorToRecipe()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		DbManager dbManager = DbManager.getInstance(this.getContext());
		Cursor cursor = ResultsDbManager.getDb().rawQuery("Select * From " + dbm.recipesTable + " Where URI = " + recipe.getUri(), null);


		/* This is a private method, so we must use reflection. */

		try {

			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Cursor.class;
			Method method = dbManager.getClass().getDeclaredMethod("cursorToRecipe", args);
			method.setAccessible(true);
			Recipe ret = (Recipe) method.invoke(dbm, cursor);

			assertTrue("Titles should be the same.", ret.getTitle().equals(recipe.getTitle()));
			assertTrue("Instructions should be the same.", ret.getInstructions().equals(recipe.getInstructions()));

			Ingredient retTemp, recipeTemp;
			for (int j = 0; j < ret.getIngredients().size(); j++) {
				retTemp = ret.getIngredients().get(j);
				recipeTemp = recipe.getIngredients().get(j);
				assertTrue("Names should be the same.", retTemp.getName().equals(recipeTemp.getName()));
				assertTrue("Units should be the same.", retTemp.getUnit().equals(recipeTemp.getUnit()));
				assertTrue("Quantities should be the same.", retTemp.getQuantity() == recipeTemp.getQuantity());
			}

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
	 * Test the functionality of the getRecipe() function.
	 * Attempt to retrieve a recipe we know to exist in the database and ensure its fields are as expected.
	 */
	public void testGetRecipe()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);

		Recipe ret = dbm.getRecipe(recipe.getUri());

		assertTrue("Titles should be the same.", ret.getTitle().equals(recipe.getTitle()));
		assertTrue("Instructions should be the same.", ret.getInstructions().equals(recipe.getInstructions()));

		Ingredient retTemp, recipeTemp;
		for (int j = 0; j < ret.getIngredients().size(); j++) {
			retTemp = ret.getIngredients().get(j);
			recipeTemp = recipe.getIngredients().get(j);
			assertTrue("Names should be the same.", retTemp.getName().equals(recipeTemp.getName()));
			assertTrue("Units should be the same.", retTemp.getUnit().equals(recipeTemp.getUnit()));
			assertTrue("Quantities should be the same.", retTemp.getQuantity() == recipeTemp.getQuantity());	
		}
	}
}
