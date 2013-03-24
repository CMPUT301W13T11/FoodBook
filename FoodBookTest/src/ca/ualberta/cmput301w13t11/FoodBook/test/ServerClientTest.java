package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.junit.Test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ClientHelper;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
/**
 * Unit tests for the ServerClient class.
 * @author mbabic
 *
 */
public class ServerClientTest extends AndroidTestCase {
	
	ServerClient sc = null;

	protected void setUp() throws Exception
	{
		super.setUp();
		
	}
	
	/**
	 * Test the getInstance method of ServerClient
	 */
	public void testGetInstance()
	{
		sc = ServerClient.getInstance();
		// for no reason
		ClientHelper whatever = new ClientHelper();
		whatever.recipeToJSON(new Recipe(new User(""), ""));
		assertTrue("getInstance() failure", sc != null);
	}
	
	
	/**
	 * Test that getThreadSafeClient does not return null.
	 */
	public void testGetThreadSafeClient()
	{
		HttpClient test_client = ServerClient.getThreadSafeClient();
		assertTrue("test_client null, getThreadSafeClient failed", test_client != null);
	}
	
	/**
	 * Test uploading a novel recipe to the server, ensure return code
	 * is SUCCESS.
	 */
	public void testUploadRecipePass()
	{
		ReturnCode ret = null;
		sc = ServerClient.getInstance();
		String title = Long.toString(System.currentTimeMillis());

		Recipe recipe = new Recipe(new User("tester"), title);
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
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
		sc = ServerClient.getInstance();

		Recipe recipe = new Recipe(new User("tester"), "test");
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
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
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("test1", "xxx", 10));
		ingredients.add(new Ingredient("test2", "xxxx", 100));
		ingredients.add(new Ingredient("test3", "xxxxx", 1000));

		ReturnCode ret = null;
		sc = ServerClient.getInstance();
		long uri = System.currentTimeMillis();

		Recipe recipe = new Recipe(uri, new User("tester"), "fake title", "", ingredients);
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			fail("ioe");
		}
		assertTrue("uploadRecipe should return SUCCESS", ret == ReturnCode.SUCCESS);
		
		sc = ServerClient.getInstance();


		ReturnCode returnCode = sc.searchByIngredients(ingredients);
		assertTrue("Returns no_esuls.", returnCode == ReturnCode.NO_RESULTS);
		
	}
	
	/**
	 * Test uploadPhotoToRecipe() by trying to add a Photo to a recipe known to exist on the server 
	 * and check against return code.
	 * WILL FAIL, NOT YET IMPLEMENTED
	 */
	public void testUploadPhotoToRecipe()
	{
		sc = ServerClient.getInstance();

		Photo photo = new Photo("testname", new byte[10]);
		Recipe recipe = Recipe.generateTestRecipe();
		
		assertTrue("return code != SUCCESS", sc.uploadPhotoToRecipe(photo, recipe.getUri()) == ReturnCode.SUCCESS);
	}

	/**
	 * Test if client can connect to phony URL -- should return false.
	 */
	public void testHasConnectionFail()
	{
		sc = ServerClient.getInstance();

		assertTrue(!sc.hasConnection("http://fakefakefake"));
	}
	
	/**
	 * Test hasConnection() with a known, good URL.
	 */
	public void testHasConnectionPass()
	{
		sc = ServerClient.getInstance();

		assertTrue(sc.hasConnection("http://www.google.com"));
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
		sc = ServerClient.getInstance();

		try {
			ReturnCode result = sc.searchByKeywords("&&^367 78tqyfgylgaahslfauy7 iw");
			assertTrue(result == ReturnCode.SUCCESS);
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}
		
		fail();
		
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
		sc = ServerClient.getInstance();
		assertTrue("sc null", sc != null);
		try {
			assertTrue("sc null", sc != null);

			ReturnCode result = sc.searchByKeywords("turdosandowich");
			assertTrue(result == ReturnCode.SUCCESS);
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}
		fail();

		
	}

}
