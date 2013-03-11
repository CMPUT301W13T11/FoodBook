package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
/**
 * Unit tests for the ServerClient class.
 * Run configurations note: if test fails to run, please go to Run Configurations -> ClassPath; if Android 4.1 is present in the
 * bootstrap entries, then delete it and replace it with the JRE System Library (Advanced -> Add Library -> JRE System Library).
 * @author mbabic
 *
 */
public class ServerClientTests extends TestCase {
	
	ServerClient sc = null;

	@Test
	/**
	 * Test the getInstance method of ServerClient
	 */
	public void testGetInstance()
	{
		sc = ServerClient.getInstance();
		assertTrue("getInstance() failure", sc != null);
	}
	
	@Test
	/**
	 * Test that getThreadSafeClient does not return null.
	 */
	public void testGetThreadSafeClient()
	{
		HttpClient test_client = ServerClient.getThreadSafeClient();
		assertTrue("test_client null, getThreadSafeClient failed", test_client != null);
	}
	
	@Test
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
	
	@Test
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
	
	@Test
	/**
	 * Test to see if searchByIngredients will return a recipe known to have the ingredients
	 * beng passed to S
	 * WILL FAIL, NOT YET IMPLEMENTED.
	 */
	public void testSearchByIngredients()
	{
		sc = ServerClient.getInstance();

		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("test1", "xxx", 10));
		ingredients.add(new Ingredient("test2", "xxxx", 100));
		ingredients.add(new Ingredient("test3", "xxxxx", 1000));

		ArrayList<Recipe> results = sc.searchByIngredients(ingredients);
		assertTrue("Resuls empty.", results.size() != 0);
		
	}
	
	@Test
	/**
	 * Test uploadPhotoToRecipe() by trying to add a Photo to a recipe known to exist on the server 
	 * and check against return code.
	 */
	public void testUploadPhotoToRecipe()
	{
		sc = ServerClient.getInstance();

		Photo photo = new Photo("testname", new byte[10]);
		Recipe recipe = Recipe.generateTestRecipe();
		
		assertTrue("return code != SUCCESS", sc.uploadPhotoToRecipe(photo, recipe) == ReturnCode.SUCCESS);
	}

	@Test
	/**
	 * Test if client can connect to phony URL -- should return false.
	 */
	public void testHasConnectionFail()
	{
		sc = ServerClient.getInstance();

		assertTrue(!sc.hasConnection("http://fakefakefake"));
	}
	
	@Test
	/**
	 * Test hasConnection() with a known, good URL.
	 */
	public void testHasConnectionPass()
	{
		sc = ServerClient.getInstance();

		assertTrue(sc.hasConnection("http://www.google.com"));
	}

	
	@Test
	/**
	 * Test searchByKeywords by seeing if passing it a keyword known to now relate to any uploaded recipe
	 * will cause it to return the code NO_RESULTS.
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
		
	}
	
	@Test
	/**
	 * Test to see if a recipe known to exist on server will be found by
	 * searchByKeywords().
	 */
	public void testSearchByKeywordsPass()
	{
		sc = ServerClient.getInstance();

		try {
			ReturnCode result = sc.searchByKeywords("turdosandowich");
			assertTrue(result == ReturnCode.SUCCESS);
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}
		
	}

}
