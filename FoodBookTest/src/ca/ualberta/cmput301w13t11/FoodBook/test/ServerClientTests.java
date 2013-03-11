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

public class ServerClientTests extends TestCase {
	
	ServerClient sc = null;

	@Test
	/*
	 * Test the getInstance method of ServerClient
	 */
	public void testGetInstance()
	{
		sc = ServerClient.getInstance();
		assertTrue("getInstance() failure", sc != null);
	}
	
	@Test
	/*
	 * Test that getThreadSafeClient does not return null.
	 */
	public void testGetThreadSafeClient()
	{
		HttpClient test_client = ServerClient.getThreadSafeClient();
		assertTrue("test_client null, getThreadSafeClient failed", test_client != null);
	}
	
	@Test
	/*
	 * Test uploading a recipe to the server.
	 */
	public void testUploadRecipe()
	{
		sc = ServerClient.getInstance();

		Recipe recipe = new Recipe(new User("tester"), "test");
		try { 
			sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			fail("ioe");
		}
		/* If we got here, we successfully uploaded a recipe. */
		assertTrue(true);
	}
	
	@Test
	/*
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
	
	@Test
	/*
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
	/*
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
	/*
	 * Test if client can connect to phony URL -- should return false.
	 */
	public void testHasConnectionFail()
	{
		sc = ServerClient.getInstance();

		assertTrue(!sc.hasConnection("http://fakefakefake"));
	}
	
	@Test
	/*
	 * Test hasConnection() with a known, good URL.
	 */
	public void testHasConnectionPass()
	{
		sc = ServerClient.getInstance();

		assertTrue(sc.hasConnection("http://www.google.com"));
	}
	
}
