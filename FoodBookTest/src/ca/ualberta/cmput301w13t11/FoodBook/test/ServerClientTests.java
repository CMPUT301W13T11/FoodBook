package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

public class ServerClientTests extends TestCase {
	
	ServerClient sc = ServerClient.getInstance();
	
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
		assertTrue(sc.hasConnection("http://www.google.com"));
	}
	
	@Test
	/*
	 * Test uploading a recipe to the server.
	 */
	public void testUploadRecipe()
	{
		Recipe recipe = new Recipe(new User("tester"), "testtesttest");
		try { 
			sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			fail("ioe");
		}
		assertTrue(1 == 1);
	}

}
