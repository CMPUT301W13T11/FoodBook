package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.ClientHelper;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerPhoto;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
/**
 * Unit tests for the ClientHelper class.
 * Run configurations note: if test fails to run, please go to Run Configurations -> ClassPath; if Android 4.1 is present in the
 * bootstrap entries, then delete it and replace it with the JRE System Library (Advanced -> Add Library -> JRE System Library).
 * @author mbabic
 *
 */
public class ClientHelperTests extends AndroidTestCase {
	
	private ClientHelper helper = new ClientHelper();
	private HttpClient httpclient = new DefaultHttpClient();
	
	
	protected void setUp() throws Exception
	{
		super.setUp();
		helper = new ClientHelper();
		httpclient = ServerClient.getThreadSafeClient();
	}	
	
	/**
	 * Test that a recipe is converted to a JSON object successfully.
	 */
	public void testRecipeToJSONPass()
	{
		/* Generate a set of ingredients. */
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("egg", "whole", 1));
		ingredients.add(new Ingredient("turd sandwich", "cup", (float).5));
		
		/* Create test recipe. */
		Recipe test_recipe = new Recipe(10001010L, new User("Tester"), "Nothing you'd want to eat.", "Don't cook this.", ingredients);
		
		StringEntity ret = helper.recipeToJSON(test_recipe);
		
		assertTrue("Returned StringEntity should be non-null.", ret != null);
	}
	
	/**
	 * Give toRecipe a response with no JSON in it; the method should fail to extract any information.
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
	
	/**
	 * Pass valid server response to toRecipeList() and make sure it returns the known
	 * contents of the response in a Recipe.
	 */
	public void testToRecipeListPass()
	{
		String json = "{\"took\":5,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\""
				+ ":{\"total\":1,\"max_score\":1.5397208,\"hits\":[{\"_index\":\"testing\",\"_type\":\"cmput301w13t11\",\"_id\"" +
				":\"test\",\"_score\":1.5397208, \"_source\" : {\"author\":{\"name\":\"tester\"},\"title\":\"test\",\"instructions\"" +
				":\"\",\"ingredients\":[],\"photos\":[],\"uri\":0}}]}}";

		ArrayList<Recipe> result = null;

		/* Extract the JSON string from the test file. */
		try {
			result = helper.toRecipeList(json);
		} catch (IOException ioe) {
			fail("IOException");
		}
		
		for (Recipe r: result) {
			
			assertTrue("Title failed.", r.getTitle().equals("test"));
			assertTrue("User name failed.", r.getAuthor().getName().equals("tester"));
			assertTrue("Uri failed.", r.getUri() == 0);
			assertTrue("Instructions failed.", r.getInstructions().equals(""));
		}
		assertTrue(!(result.isEmpty()));
	}
	
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
	
	/**
	 * Test the functionality of the serverPhotoToJSON() method by ensuring it returns
	 * a non-null string when passed a non-null ServerPhoto() object.  
	 */
	public void testServerPhotoToJSON()
	{
		String testId = "test id";
		String testPath = "test path";
		Bitmap testBitmap = BogoPicGen.generateBitmap(100, 100);
		Photo testPhoto = new Photo(testId, testPath, testBitmap);
		ServerPhoto sp = new ServerPhoto(testPhoto);
		
		String ret = helper.serverPhotoToJSON(sp);
		assertTrue("Returned string should not be null", ret != null);
	}
	
	/**
	 * Test the responseToString() method by ensuring that a non-null HttpResponse
	 * is transformed to a non-null String -- accuracy of the conversion should be confirmed
	 * by logging the results of the method's return if the HttpResponse is known.
	 * NOTE: this test may stall the run of the unit tests.  In such
	 * a case, the AVD must be restarted be reattempting. BE ADVISED.
	 */
	public void testResponseToString()
	{
		HttpGet get = new HttpGet("http://www.google.ca");
		HttpResponse response = null;
		try {
			response = httpclient.execute(get);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
		assertTrue("HttpResponse snould not be null -- server contact failure, test results INCONCLUSIVE.", response != null);
		
		try {
			String ret = helper.responseToString(response);
			assertTrue("Returned string should not be null", ret != null);
		} catch (Exception e) {
			fail("Exception thrown.");
		}
		
	}

}
