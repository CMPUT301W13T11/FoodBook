package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.ClientHelper;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
/**
 * Unit tests for the ClientHelper class.
 * Run configurations note: if test fails to run, please go to Run Configurations -> ClassPath; if Android 4.1 is present in the
 * bootstrap entries, then delete it and replace it with the JRE System Library (Advanced -> Add Library -> JRE System Library).
 * @author mbabic
 *
 */
public class ClientHelperTests extends TestCase {
	private ClientHelper helper = new ClientHelper();
	private HttpClient httpclient = new DefaultHttpClient();
	
	
	@Test
	/**
	 * Test that a recipe is converted to a JSON object successfully.
	 */
	public void testToJSONPass()
	{
		/* Generate a set of ingredients. */
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("egg", "whole", 1));
		ingredients.add(new Ingredient("turd sandwich", "cup", (float).5));
		
		/* Create test recipe. */
		Recipe test_recipe = new Recipe(new User("Tester"), "Nothing you'd want to eat.",
										"Don't cook this.",ingredients);
		
		StringEntity ret = helper.toJSON(test_recipe);
		
		assertTrue(ret != null);
	}
	
	@Test
	/**
	 * Give toRecipe a response with no JSON in it; the method should fail to extact any information.
	 */
	public void testToRecipeEmptyReturn()
	{
		String json = "", out;
		HttpResponse response;
		HttpPost httppost = new HttpPost("http://www.google.com");
		try {
			response = httpclient.execute(httppost);
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));			
			while ((out = br.readLine()) != null) {
				json += out;
			}
			ArrayList<Recipe> ret = helper.toRecipeList(json);
			assertTrue(ret.isEmpty());
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}
		
	}
	
	@Test
	/**
	 * Pass valid server response to toRecipeList() and make sure it returns the known
	 * contents of the response in a Recipe.
	 */
	public void testToRecipeListPass()
	{
		String out, json = "";

		ArrayList<Recipe> result = null;

		/* Extract the JSON string from the test file. */
		try {
			FileReader file = new FileReader("docs/JSONServerResponse.txt");
			BufferedReader br = new BufferedReader(file);
			
			while ((out = br.readLine()) != null) {
				json += out;
			}
			result = helper.toRecipeList(json);

		} catch (FileNotFoundException fnfe) {
			fail("file not found");
		} catch (IOException ioe) {
			fail("IOException");
		}
		

		for (Recipe r: result) {
			assertTrue("Title failed.", r.getTitle().equals("test"));
			assertTrue("User name failed.", r.getAuthor().getName().equals("tester"));
			System.out.println(r.getUri());
			assertTrue("Uri failed.", r.getUri() == 0);
		}
		assertTrue(!(result.isEmpty()));
	}
	
	@Test
	/**
	 * Pass garbage string to toRecipeList() and insure no recipe object is created.
	 */
	public void tesToRecipeListFail()
	{
		String fake_json = "thisisnotjsonatall191919191921307823 ryqsjke";
		try {
			ArrayList<Recipe> results = helper.toRecipeList(fake_json);
			assertTrue("Results should be empty.", results.isEmpty());
		} catch (IOException ioe) {
			fail("IOEception");
		}
		fail("Should'nt get here.");
	}

}
