package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.IOException;
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

public class ClientHelperTests extends TestCase {
	private ClientHelper helper = new ClientHelper();
	private HttpClient httpclient = new DefaultHttpClient();
	
	private class MockRecipe extends Recipe
	{
		public MockRecipe(Recipe recipe)
		{
			super(recipe.getAuthor(), recipe.getTitle(), recipe.getInstructions(),
					recipe.getIngredients(), recipe.getPhotos());
			
		}
	}
	
	
	@Test
	/*
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
	/*
	 * Give toRecipe a response with no JSON in i -- it should not be able to extract any information from it.
	 */
	public void testToRecipeEmptyReturn()
	{
		HttpResponse response;
		HttpPost httppost = new HttpPost("http://www.google.com");
		try {
			response = httpclient.execute(httppost);
			ArrayList<Recipe> ret = helper.toRecipeList(response);
			assertTrue(ret.isEmpty());
		} catch (ClientProtocolException cpe) {
			fail("cpe");
		} catch (IOException ioe) {
			fail("ioe");
		}
		
	}
	
	@Test
	/*
	 * Query a server location we know has 
	 */
	public void testToRecipePass()
	{
		
	}

}
