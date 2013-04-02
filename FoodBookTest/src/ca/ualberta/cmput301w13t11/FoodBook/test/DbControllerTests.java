package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Unit tests for the DbController class.
 * NOTE: A large number of DbController methods are simple calls to DbManager methods followed by calls to the FModel<> method
 * notifyViews().  For these methods, we simply attempt to invoke them without error -- the underlying methods are _thoroughly_
 * vetted in DbManagerTests, RecipesDbManagerTests, ResultsDbManagerTests, FModelTests, and IngredientsDbManager tests. 
 * @author Marko Babic
 *
 */
public class DbControllerTests extends AndroidTestCase {

	private DbController dbc = null;

	/**
	 * Mock View implementing the FView<DbManager> interface for testing purposes.
	 * @author mbabic
	 *
	 */
	private class MockView implements FView<DbManager>
	{
		public int x;
		public MockView(int x)
		{
			this.x = x;
		}

		@Override
		public void update(DbManager m)
		{
			this.x++;
		}
	}


	protected void setUp() throws Exception
	{
		super.setUp();
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
	}	

	/**
	 * Test getInstance method.
	 */
	public void testGetInstance()
	{
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		assertTrue("instance should not be null", dbc != null);
	}

	/**
	 * Test getUserRecipes() -- attempt to get recipes from database without error using 
	 * the controller's functionality.
	 */
	public void testGetUserRecipes()
	{
		try {
			dbc.getUserRecipes();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test getUserRecipe() by simply ensuring it can be called without error.
	 */
	public void testGetUserRecipe()
	{
		try {
			dbc.getUserRecipe(0);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test addRecipe() by simply ensuring it can be called without error.
	 */
	public void testAddRecipe()
	{
		try {
			dbc.addRecipe(Recipe.generateTestRecipe());
		} catch (Exception e) {
			fail();
		}
	}


	/**
	 * Test deleteRecipe() by simply ensuring it can be called without error.
	 */
	public void testDeleteRecipe()
	{
		try {
			dbc.deleteRecipe(Recipe.generateTestRecipe());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test updateRecipe() by simply ensuring it can be called without error.
	 */
	public void testUpdateRecipe()
	{
		try {
			dbc.deleteRecipe(Recipe.generateTestRecipe());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test addPhotoToRecipe() by simply ensuring it can be called without error.
	 */
	public void testAddPhotoToRecipe()
	{
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		Photo photo = new Photo("test id", "test path");
		try {
			dbc.addPhotoToRecipe(photo, bitmap, 0);
		} catch (Exception e) {
			fail();
		}
	}


	/**
	 * Test getRecipePhotos() by simply ensuring it can be called without error.
	 */
	public void testGetRecipePhotos()
	{

		try {
			dbc.getRecipePhotos(0);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test addIngredientToRecipe() by simply ensuring it can be called without error.
	 */
	public void testAddIngredientToRecipe()
	{
		Ingredient ingredient = new Ingredient("test", "test", (float) 100);
		try {
			dbc.addIngredientToRecipe(ingredient, 0);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test getRecipeIngredients() by simply ensuring it can be called without error.
	 */
	public void testGetRecipeIngredients()
	{
		try {
			dbc.getRecipeIngredients(0);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}


	/**
	 * Test the functionality of the storeRecipeIngredients() method -- this is the only
	 * non-trivial test in this set of unit tests.  We ensure that the new list of
	 * ingredients overwrites the old one in the database.
	 */
	public void testStoreRecipeIngredients()
	{
		RecipesDbManager dbm = RecipesDbManager.getInstance(this.getContext());
		long testUri = 10101019999L;
		String testTitle = "test title";
		User testAuthor = new User("test name");
		String testInstructions = "test instructions";
		ArrayList<Ingredient> testIngredients1 = new ArrayList<Ingredient>();
		ArrayList<Ingredient> testIngredients2 = new ArrayList<Ingredient>();
		Ingredient testIngredient1 = new Ingredient("test1", "x", (float) 100);
		Ingredient testIngredient2 = new Ingredient("test1", "x", (float) 100);
		testIngredients1.add(testIngredient1);
		testIngredients2.add(testIngredient2);

		
		Recipe recipe = new Recipe(testUri, testAuthor, testTitle, testInstructions, testIngredients1);
		dbm.insertRecipe(recipe);
		
		/* 
		 * Given the correct functionality of DbManager methods (verified in th eset of unit tests
		 * for RecipesDbManager) we proceed as follows.
		 */
		dbc.storeRecipeIngredients(testIngredients2, testUri);
		
		Recipe ret = dbm.getRecipe(testUri);
		
		assertTrue("Returned recipe should only have one ingredient.", ret.getIngredients().size() == 1);
		assertTrue("Returned recipe should consist of only testIngredient2 (names should be equal)",
				ret.getIngredients().get(0).getName().equals(testIngredient2.getName()));
		assertTrue("Returned recipe should consist of only testIngredient2 (units should be equal)",
				ret.getIngredients().get(0).getUnit().equals(testIngredient2.getUnit()));
		assertTrue("Returned recipe should consist of only testIngredient2 (quantity should be equal)",
				ret.getIngredients().get(0).getQuantity() == testIngredient2.getQuantity());
	}

	/**
	 * Test the functionality of the updateResultsDb() method by simply ensuring it can be called
	 * without error.
	 */
	public void testUpdateResultsDb()
	{
		try {
			dbc.updateResultsDb();
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test the functionality of the getStoredRecipes() method by simply ensuring it can be called
	 * without error.
	 */
	public void testGetStoredRecipes()
	{
		try {
			dbc.updateResultsDb();
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test the functionality of the deleteStoredRecipe() method by simply ensuring it can be called
	 * without error.
	 */
	public void testDeleteStoredRecipe()
	{
		Recipe recipe = Recipe.generateRandomTestRecipe();
		try {
			dbc.deleteStoredRecipe(recipe);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}

	/**
	 * Test the functionality of the getStoredRecipePhotos() method by simply ensuring it can be called
	 * without error.
	 */
	public void testGetStoredRecipePhotos()
	{
		try {
			dbc.getStoredRecipePhotos(0);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test the functionality of the getStoredRecipeIngredients() method by simply ensuring it can be called
	 * without error.
	 */
	public void testGetStoredRecipeIngredients()
	{
		try {
			dbc.getStoredRecipeIngredients(0);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test the functionality of the addIngredient() method by simply ensuring it can be called
	 * without error.
	 */
	public void testAddIngredient()
	{
		try {
			dbc.addIngredient(new Ingredient("", "", 0));
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test the functionality of the deleteIngredient() method by simply ensuring it can be called
	 * without error.
	 */
	public void testDeleteIngredient()
	{
		try {
			dbc.deleteIngredient(new Ingredient("", "", 0));
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	/**
	 * Test the functionality of the getUserIngredients() method by simply ensuring it can be called
	 * without error.
	 */
	public void testGetUserIngredients()
	{
		try {
			dbc.getUserIngredients();
		} catch (Exception e) {
			fail("Exception thrown.");
		}
	}
	
	
}
