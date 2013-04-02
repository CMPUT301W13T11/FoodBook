package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;
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

	private static String sdCardPath = Environment.getExternalStorageDirectory()+File.separator;
	private static ResultsDbManager dbm = null;
	protected void setUp() throws Exception
	{
		super.setUp();
		sdCardPath = Environment.getExternalStorageDirectory()+File.separator;
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
		String name = Long.toString(System.currentTimeMillis());
		String path = sdCardPath + name;
		Photo newPhoto = new Photo(name, path);		

		boolean ret = dbm.insertRecipePhotos(newPhoto, bitmap, recipe.getUri());


		assertTrue("insertRecipePhotos() should return true.", ret == true);	
	}

	/**
	 * Tests the functionality of the cursorToPhotos() method.
	 * We test to ensure that a the known result of a database query is correctly transformed into
	 * a photo object with the correct, known parameters.
	 */
	public void testCursorToPhotos()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String name = Long.toString(System.currentTimeMillis());
		String path = sdCardPath + name;
		Photo newPhoto = new Photo(name, path);
		boolean ret = dbm.insertRecipePhotos(newPhoto, bitmap, recipe.getUri());
		assertTrue("We failed to insert a photo, so the results of this test are invalid.", ret == true);
		
		
		DbManager dbManager = DbManager.getInstance(this.getContext());
    	Cursor cursor = dbm.getDb().rawQuery("Select * From " + dbm.photosTable + " Where recipeURI = " + recipe.getUri(), null);
    	
		try {

			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Cursor.class;
			Method method = dbManager.getClass().getDeclaredMethod("cursorToPhotos", args);
			method.setAccessible(true);
			ArrayList<Photo> photos = (ArrayList<Photo>) method.invoke(dbm, cursor);

			/* Attempt to find the photo we stored in the returned array list */
			int i;
			for (i = 0; i < photos.size(); i++) {
				if (photos.get(i).getId().equals(name))
					break;
			}

			assertTrue("The photo we inserted should be in the returned ArrayList.", i < photos.size());
			assertTrue("The returned path should be the same as the one stored.", photos.get(i).getPath().equals(path));
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
	 * Test the functionality of the getRecipePhotos() method.
	 * We attempt to retrieve a photo from a Recipe which we know to exist in the database and ensure
	 * that the path and name and correctly fetched and returned.
	 */
	public void testGetRecipesPhotos()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String name = Long.toString(System.currentTimeMillis());
		String path = sdCardPath + name;
		Photo newPhoto = new Photo(name, path);		

		boolean ret = dbm.insertRecipePhotos(newPhoto, bitmap, recipe.getUri());
		assertTrue("We failed to insert a photo, so the results of this test are invalid.", ret == true);

		ArrayList<Photo> photos = dbm.getRecipePhotos(recipe.getUri());

		/* Attempt to find the photo we stored in the returned array list */
		int i;
		for (i = 0; i < photos.size(); i++) {
			if (photos.get(i).getId().equals(name))
				break;
		}

		assertTrue("The photo we inserted should be in the returned ArrayList.", i < photos.size());

		/* Else, we found the photo in the list and need to further ensure that the path is the same. */
		assertTrue("The returned path should be the same as the one stored.", photos.get(i).getPath().equals(path));
	}


	
	/**
	 * Test the functionality of the getFullPhotos() method.
	 * Ensure that expected photo objects are returned with non-null bitmap (where applicable).
	 */
	public void testGetFullPhotos()
	{
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		assertTrue("Generated bitmap should be non-null -- BogoPicGen failure.", bitmap != null);
		String name = Long.toString(System.currentTimeMillis());
		String path = sdCardPath + name;
		Photo newPhoto = new Photo(name, path);		
		ArrayList<Photo> arg = new ArrayList<Photo>();
		arg.add(newPhoto);
		
		/* save bitmap to device */
    	File file = null; 					/* the image file itself */
    	boolean success = false;			/* set to true on successful write of image file to device storage*/
    	boolean worked = false;			/* set to true on successful compression of given bitmap*/
    	FileOutputStream outStream = null;	/* the file write stream */
    	
    	try {

    		file = new File(path);
			outStream = new FileOutputStream(file);
			
			worked = bitmap.compress(Bitmap.CompressFormat.PNG, 30, outStream);
			outStream.flush();
			outStream.close();
			success = true;
			path = file.getAbsolutePath();

		} catch (Exception ex) {
			ex.printStackTrace();
			Log.d("Failed to save image.", "Failed to save image.");
			fail("Exception during bitmap save.");
		} 

    	assertTrue("Should be able to save bitmap to device.", success && worked);
		
		DbManager dbManager = DbManager.getInstance(this.getContext());
		
		try {

			/* Testing private member function, need to use reflection. */
			
			Method method = dbManager.getClass().getDeclaredMethod("getFullPhotos", ArrayList.class);
			method.setAccessible(true);
			ArrayList<Photo> photos = (ArrayList<Photo>) method.invoke(dbm, arg);
			
			/* Attempt to find the photo we stored in the returned array list */
			int i;
			for (i = 0; i < photos.size(); i++) {
				if (photos.get(i).getId().equals(name))
					break;
			}

			assertTrue("The photo we inserted should be in the returned ArrayList.", i < photos.size());
			assertTrue("The returned path should be the same as the one stored.", photos.get(i).getPath().equals(path));
			assertTrue("The photo object returned should have non-null bitmap.", photos.get(i).getPhotoBitmap() != null);
			
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
	 * Test the functionality of the removeRecipePhoto() method.
	 * We attempt to remove a photo we know to exist on the phone and ensure that when we retrieve
	 * the Recipe's photos the photo we deleted is not among them.  We also test to make sure that
	 * the photo file is deleted from device storage.
	 */
	public void testRemoveRecipePhoto()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		dbm.insertRecipe(recipe);
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String name = Long.toString(System.currentTimeMillis());
		String path = sdCardPath + name;
		Photo newPhoto = new Photo(name, path);		

		boolean ret = dbm.insertRecipePhotos(newPhoto, bitmap, recipe.getUri());
		assertTrue("We failed to insert a photo, so the results of this test are invalid.", ret == true);

		ArrayList<Photo> photos = dbm.getRecipePhotos(recipe.getUri());

		/* Attempt to find the photo we stored in the returned array list */
		int i;
		for (i = 0; i < photos.size(); i++) {
			if (photos.get(i).getId().equals(name))
				break;
		}

		assertTrue("The photo we inserted should be in the returned ArrayList.", i < photos.size());

		/* We got here, we know the photo was correctly inserted.  We now attempt to remove it and ensure it has been deleted. */
		ret = dbm.removeRecipePhoto(newPhoto);
		assertTrue("removeRecipePhoto should return true", ret == true);

		photos = dbm.getRecipePhotos(recipe.getUri());

		for (i = 0; i < photos.size(); i++) {
			if (photos.get(i).getId().equals(name))
				break;
		}

		assertTrue("The photo we deleted should not be in the returned ArrayList.", i >= photos.size());
		
		boolean deleted = true;
		try{
	    	File file = new File(path);
	        deleted = file.delete();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		assertTrue("file.delete() method should return false", deleted == false);

	}
	
	/**
	 * Test the functionality of the storeRecipsMethod()
	 * Ensure that an array list of recipes is correctly stored in the results database by attempting to store/retrieve
	 * known recipe objects.
	 * CAUTION: partially dependent on the correct functioning of getRecipes() -- make sure is tests have passed before
	 * evaluating the results of this test.
	 */
	public void testStoreRecipes()
	{
		dbm = ResultsDbManager.getInstance(this.getContext());
		if (dbm == null) {
			fail("failed to get an instance of ResultsDbManager");
		}
		
		/* Create photo arrays. */
		String name1 = Long.toString(System.currentTimeMillis());
		String path1 = sdCardPath + name1;
		Photo newPhoto1 = new Photo(name1, path1);
		
		ArrayList<Photo> p1 = new ArrayList<Photo>();
		p1.add(newPhoto1);

		
		String name2 = Long.toString(System.currentTimeMillis());
		String path2 = sdCardPath + name2;
		Photo newPhoto2 = new Photo(name2, path2);
		
		String name3 = Long.toString(System.currentTimeMillis());
		String path3 = sdCardPath + name3;
		Photo newPhoto3 = new Photo(name3, path3);
		
		ArrayList<Photo> p2 = new ArrayList<Photo>();
		p2.add(newPhoto2);
		p2.add(newPhoto3);
		

		/* Create ingredient arrays. */
		ArrayList<Ingredient> i1 = new ArrayList<Ingredient>();
		ArrayList<Ingredient> i2 = new ArrayList<Ingredient>();
		i1.add(new Ingredient("egg", "whole", (float) 1/2));
		i2.add(new Ingredient("butter", "tbsp", (float) 4));
		
		
		Recipe r1 = new Recipe(1, new User("user1"), "title1", "instructions1", i1, p1);
		Recipe r2 = new Recipe(2, new User("user2"), "title2", "instructions2", i2, p2);
		
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(r1);
		recipes.add(r2);
		
		dbm.storeRecipes(recipes);
		
		ArrayList<Recipe> ret = dbm.getRecipes();
		
		assertTrue("Results table should have same number of elements as test recipe array.", ret.size() == recipes.size());
		
		/* Check for equality amongst results and recipes created above. */
		
		Recipe t1 = null;
		Recipe t2 = null;
		Ingredient it1 = null;
		Ingredient it2 = null;
		Photo ip1 = null;
		Photo ip2= null;
		for (int i = 0; i < ret.size(); i++) {
			t1 = ret.get(i);
			t2 = recipes.get(i);
			
			assertTrue("Titles should be the same.", t1.getTitle().equals(t2.getTitle()));
			assertTrue("Author names should be the same.", t1.getAuthor().getName().equals(t2.getAuthor().getName()));
			assertTrue("Instructions should be the same.", t1.getInstructions().equals(t2.getInstructions()));
			
			/* Check equality of ingredient arrays. */
			for (int j = 0; j < t1.getIngredients().size(); j++) {
				it1 = t1.getIngredients().get(j);
				it2 = t2.getIngredients().get(j);
				assertTrue("Names should be the same.", it1.getName().equals(it2.getName()));
				assertTrue("Units should be the same.", it1.getUnit().equals(it2.getUnit()));
				assertTrue("Quantities shoul be the same.", it1.getQuantity() == it2.getQuantity());
			}
			
			/* Check equality of photo arrays. */
			for (int j = 0; j < t1.getPhotos().size(); j++) {
				it1 = t1.getIngredients().get(j);
				it2 = t2.getIngredients().get(j);
				assertTrue("Ids should be the same.", ip1.getId().equals(ip2.getId()));
				assertTrue("Paths should be the same.", ip1.getPath().equals(ip2.getPath()));
			}
			
		}
		
	}
}
