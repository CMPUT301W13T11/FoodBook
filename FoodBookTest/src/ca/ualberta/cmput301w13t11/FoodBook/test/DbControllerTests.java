package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

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
	 * Test the functionality of the updateResultsDb() method by simiply ensuring it can be called
	 * without error.
	 */
	public void testupdateResultsDb()
	{
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		dbc.updateResultsDb();
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




}
