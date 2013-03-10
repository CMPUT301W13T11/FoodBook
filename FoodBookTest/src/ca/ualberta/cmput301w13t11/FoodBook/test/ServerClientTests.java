package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

public class ServerClientTests extends TestCase {
	
	ServerClient sc = ServerClient.getInstance();

	@Test
	/*
	 * Test uploading a recipe to the server.
	 */
	public void testUploadRecipe()
	{
		Recipe recipe = new Recipe(new User("tester"), "test");
		try { 
			sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			fail("ioe");
		}
		/* If we got here, we successfully uploaded a recipe. */
		assertTrue(1 == 1);
	}
	
	@Test
	/*
	 * Test to see if recipe known to exist on server will be found by
	 * searchByKeywords().
	 */
	public void testSearchByKeywordsPass()
	{
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
	 * Test if client can connect to phony URL -- should return false.
	 */
	public void testHasConnectionFail()
	{
		assertTrue(!sc.hasConnection("http://fakefakefake"));
	}
	
	@Test
	/*
	 * Test hasConnection() with the test server URL.
	 */
	public void testHasConnectionPass()
	{
		assertTrue(sc.hasConnection(sc.getServerUrl()));
	}
	
}
