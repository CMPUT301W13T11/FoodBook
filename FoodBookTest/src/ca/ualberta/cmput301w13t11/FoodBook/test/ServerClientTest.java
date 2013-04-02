package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.ClientHelper;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
/**
 * Unit tests for the ServerClient class.
 * @author mbabic
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
	 * Test the functionality of the checkForRecipe() function.
	 * We ensure that a Recipe known to exist on the server is correctly found.
	 * NOTE: this test may stall the run of the unit tests -- in practice, it is run in
	 * dismissisable thread with timeout but here we simply make a raw call to it.  In such
	 * a case, the AVD must be restarted be reattempting. BE ADVISED.
	 */
	public void testCheckForRecipe1()
	{
		sc = ServerClient.getInstance();
		//assertTrue("Should be able to find recipe with uri 1364851229133", )
		try {
			Class[] args = new Class[1];
			args[0] = long.class;
			Method method = sc.getClass().getDeclaredMethod("checkForRecipe", args);
			method.setAccessible(true);
			ReturnCode ret = (ReturnCode) method.invoke(sc, 1364851229133L);
			assertTrue("Should be able to find recipe with uri 1364851229133", ret == ReturnCode.SUCCESS);
			
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
	 * Test the functionality of the checkForRecipe() function.
	 * We ensure that a Recipe known to not exist on the server is correctly not found.
	 * NOTE: this test may stall the run of the unit tests -- in practice, it is run in
	 * dismissisable thread with timeout but here we simply make a raw call to it.  In such
	 * a case, the AVD must be restarted be reattempting. BE ADVISED.
	 */
	public void testCheckForRecipe2()
	{
		sc = ServerClient.getInstance();
		try {
			Class[] args = new Class[1];
			args[0] = long.class;
			Method method = sc.getClass().getDeclaredMethod("checkForRecipe", args);
			method.setAccessible(true);
			ReturnCode ret = (ReturnCode) method.invoke(sc, 123456L);
			assertTrue("Should not be able to find recipe with uri 123456", ret == ReturnCode.NOT_FOUND);
			
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
	 * Test uploading a novel recipe to the server, ensure return code
	 * is SUCCESS.
	 */
	public void testUploadRecipePass()
	{
		ReturnCode ret = null;
		sc = ServerClient.getInstance();
		String title = Long.toString(System.currentTimeMillis());

		Recipe recipe = new Recipe(new User("tester"), title, "");
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			fail("ioe");
		}

		assertTrue("uploadRecipe should return SUCCESS", ret == ReturnCode.SUCCESS);
	}
	
	/**
	 * Test the functionality of the ingredientsToString() method by ensuring
	 * that it returns the expected string when passed a known list of recipes.
	 */
	public void testIngredientsToString()
	{
		sc = ServerClient.getInstance();
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("test1", "x", (float) 10));
		ingredients.add(new Ingredient("test2", "x", (float) 10));
		ingredients.add(new Ingredient("test3", "x", (float) 10));

		try {
			Class[] args = new Class[1];
			args[0] = ArrayList.class;
			Method method = sc.getClass().getDeclaredMethod("ingredientsToString", args);
			method.setAccessible(true);
			String ret = (String) method.invoke(sc, ingredients);
			String expectedRet = "test1 OR test2 OR test3";
			assertTrue("Returned string should match expected string.", ret.equals(expectedRet));
			
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
	 * Test uploadRecipe() by passing it a recipe known to already exists on the server
	 * and ensure it correctly returns ALREADY_EXISTS.
	 */
	public void testUploadRecipeFail()
	{
		ResultsDbManager db = ResultsDbManager.getInstance(this.getContext());

		ReturnCode ret = null;
		sc = ServerClient.getInstance();

		Recipe recipe = new Recipe(1364339255953L);
		try { 
			ret = sc.uploadRecipe(recipe);
		} catch (IOException ioe) {
			fail("ioe");
		}

		assertTrue("uploadRecipe should return ALREADY_EXISTS", ret == ReturnCode.ALREADY_EXISTS);
	}
	
	/**
	 * Test to see if searchByIngredients will return a recipe known to have the ingredients
	 * being passed as the search parameters.
	 */
	public void testSearchByIngredients()
	{
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("test1", "xxx", 10));
		ingredients.add(new Ingredient("test2", "xxxx", 100));
		ingredients.add(new Ingredient("test3", "xxxxx", 1000));

		ReturnCode ret = null;
		sc = ServerClient.getInstance();
		ResultsDbManager db = ResultsDbManager.getInstance(this.getContext());
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
		assertTrue("Returns no_resuls.", returnCode == ReturnCode.SUCCESS);
		
	}
	
	/**
	 * Test uploadPhotoToRecipe() by trying to add a Photo to a recipe known to exist on the server 
	 * and check against return code.
	 */
	public void testUploadPhotoToRecipe()
	{
		sc = ServerClient.getInstance();

		Photo photo = new Photo("testname", new byte[10]);
		ReturnCode ret = sc.uploadPhotoToRecipe(photo, 1364241542351L);

		assertTrue("return code != SUCCESS", ret == ReturnCode.SUCCESS);
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
	 */
	public void testSearchByKeywordsFail()
	{
		ResultsDbManager db = ResultsDbManager.getInstance(this.getContext());

		sc = ServerClient.getInstance();

		try {
			ReturnCode result = sc.searchByKeywords("&&^367 78tqyfgylgaahslfauy7 iw");
			assertTrue(result == ReturnCode.NO_RESULTS);
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}		
	}
	
	/**
	 * Test to see if a recipe known to exist on server will be found by
	 * searchByKeywords().
	 */
	public void testSearchByKeywordsPass()
	{
		DbManager db = DbManager.getInstance(this.getContext());

		sc = ServerClient.getInstance();
		assertTrue("sc null", sc != null);
		try {
			assertTrue("sc null", sc != null);

			ReturnCode result = sc.searchByKeywords("turdosandowich");
			assertTrue("Result is not success.", result == ReturnCode.SUCCESS);
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}		
	}

}
