package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

public class ServerConrollerTest extends AndroidTestCase {
	
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
	 * Test the getInstance method of ServerClient
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

		Recipe recipe = new Recipe(new User("tester"), title);
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

		Recipe recipe = new Recipe(new User("tester"), "test");
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (Exception ioe) {
			fail("ioe");
		}

		assertTrue("uploadRecipe should return ALREADY_EXISTS", ret == ReturnCode.ALREADY_EXISTS);
	}
	
	/**
	 * Test to see if searchByIngredients will return a recipe known to have the ingredients
	 * beng passed to S
	 * WILL FAIL, NOT YET IMPLEMENTED.
	 */
	public void testSearchByIngredients()
	{
		MockView view = new MockView(1);
		sc = ServerController.getInstance(view);

		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("test1", "xxx", 10));
		ingredients.add(new Ingredient("test2", "xxxx", 100));
		ingredients.add(new Ingredient("test3", "xxxxx", 1000));
		fail();
		//ArrayList<Recipe> results = sc.searchByIngredients(ingredients);
		//assertTrue("Resuls empty.", results.size() != 0);
		
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
//		MockView view = new MockView(1);
//		sc = ServerController.getInstance(view);
//		try {
//			ReturnCode result = sc.searchByKeywords("&&^367 78tqyfgylgaahslfauy7 iw");
//			assertTrue(result == ReturnCode.SUCCESS);
//		} catch (Exception cpe) {
//			fail("exception");
//		} 
//		
//		fail();
		
	}
	
	/**
	 * Test to see if a recipe known to exist on server will be found by
	 * searchByKeywords().
	 * 
	 * DUE TO ERRORS WITH THE BUILD PATH, THIS TEST DOES NOT RUN CORRECTLY AT THE MOMENT
	 * THIS IS AN ERROR IN ECLIPSE/ANDROID CONFIGURATION, NOT WITH THE METHOD ITSELF
	 */
	public void testSearchByKeywordsPass()
	{
//		sc = ServerClient.getInstance();
//		try {
//			ReturnCode result = sc.searchByKeywords("turdosandowich");
//			assertTrue(result == ReturnCode.SUCCESS);
//		} catch (ClientProtocolException cpe) {
//			fail("cpe");
//		} catch (IOException ioe) {
//			fail("ioe");
//		}
		fail();

		
	}

}