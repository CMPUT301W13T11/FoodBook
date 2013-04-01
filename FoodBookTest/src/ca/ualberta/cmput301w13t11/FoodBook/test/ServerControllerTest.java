package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Units tests for the ServerController class methods.
 * 
 * @author mbabic
 *
 */
public class ServerControllerTest extends AndroidTestCase {

	ServerController sc = null;
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
		DbManager temp = DbManager.getInstance(this.getContext());

	}
	
	/**
	 * Test the getInstance method of ServerClient.
	 * Ensure that getInstance() does not return null.
	 */
	public void testGetInstance()
	{
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		assertTrue("getInstance() failure", sc != null);
	}

	/**
	 * Test uploading a novel recipe to the server, ensure return code
	 * is SUCCESS.
	 */
	public void testUploadRecipePass()
	{
		ReturnCode ret = null;
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		String title = Long.toString(System.currentTimeMillis());

		Recipe recipe = new Recipe(10101 ,new User("tester"), title, "", new ArrayList<Ingredient>());
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (Exception ioe) {
			fail("ioe");
		}

		assertTrue("uploadRecipe should return SUCCESS", ret == ReturnCode.SUCCESS);
	}

	/**
	 * Test uploadRecipe() by passing it a recipe known to already exists on the server
	 * and ensure it correctly returns ALREADY_EXISTS.
	 */
	public void testUploadRecipeFail()
	{
		ReturnCode ret = null;
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		long uri = 999;
		
		Recipe recipe = new Recipe(999, new User("tester"), "test", "", new ArrayList<Ingredient>(), new ArrayList<Photo>());
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (Exception ioe) {
			fail("ioe");
		}

		assertTrue("uploadRecipe should return ALREADY_EXISTS", ret == ReturnCode.ALREADY_EXISTS);
	}

	/**
	 * Test the functionality of the searchByIngredients() method.
	 * Test to see if searchByIngredients will return a recipe known to have the ingredients
	 * being passed as arguments.
	 * Please note that this test will sometimes fail as the result of an internal server error at the upload server.
	 */
	public void testSearchByIngredients()
	{
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);

		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("test1", "xxx", 10));
		ingredients.add(new Ingredient("test2", "xxxx", 100));
		ingredients.add(new Ingredient("test3", "xxxxx", 1000));

		ReturnCode ret = sc.searchByIngredients(ingredients);
		assertTrue("searchByIngredients should return SUCCESS", ret == ReturnCode.SUCCESS);
	}


	/**
	 * Test hasConnection()
	 * Will always pass given current implementation of has Connection.
	 */
	public void testHasConnectionPass()
	{
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		assertTrue(sc.hasConnection());
	}


	/**
	 * Test searchByKeywords by seeing if passing it a keyword known to now relate to any uploaded recipe
	 * will cause it to return the code NO_RESULTS.
	 * 
	 * DUE TO ERRORS WITH THE BUILD PATH, THIS TEST DOES NOT RUN CORRECTLY AT THE MOMENT
	 * THIS IS AN ERROR IN ECLIPSE/ANDROID CONFIGURATION, NOT WITH THE METHOD ITSELF
	 */
	public void testSearchByKeywordsFail()
	{
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		try {
			ReturnCode result = sc.searchByKeywords("&&^367 78tqyfgylgaahslfauy7 iw");
			assertTrue(result != ReturnCode.SUCCESS);
		} catch (Exception cpe) {
			fail("exception");
		} 
	}

	/**
	 * Test to see if a recipe known to exist on server will be found by
	 * searchByKeywords().
	 * 
	 */
	public void testSearchByKeywordsPass()
	{
		MockView view = new MockView(1);

		sc = ServerController.getInstance(view);
		ReturnCode result = sc.searchByKeywords("chicken");
		assertTrue(result == ReturnCode.SUCCESS);

	}
	
	/**
	 * Test the functionality of the uploadPhotoToRecipe() function.
	 * Attempt to upload a photo a recipe known to exists, make sure the returned ReturnCode is SUCCESS.
	 */
	public void testUploadPhotoToRecipe1()
	{
		
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String name = Long.toString(System.currentTimeMillis());
		Photo toUpload = new Photo(name, name, bitmap);
		ReturnCode result = sc.uploadPhotoToRecipe(toUpload, 1364851229133L);
		assertTrue(result == ReturnCode.SUCCESS);
		
	}
	
	/**
	 * Test the functionality of the uploadPhotoToRecipe() function.
	 * Attempt to upload a photo a recipe known to not exist, ReturnCode is NOT_FOUND.
	 */
	public void testUploadPhotoToRecipe2()
	{
		
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String name = Long.toString(System.currentTimeMillis());
		Photo toUpload = new Photo(name, name, bitmap);
		ReturnCode result = sc.uploadPhotoToRecipe(toUpload, 1234567L);
		assertTrue(result == ReturnCode.NOT_FOUND);
		
	}


}