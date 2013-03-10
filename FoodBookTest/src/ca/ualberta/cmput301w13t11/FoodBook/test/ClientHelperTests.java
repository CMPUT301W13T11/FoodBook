package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.http.entity.StringEntity;
import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.ClientHelper;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

public class ClientHelperTests extends TestCase {
	ClientHelper helper = new ClientHelper();
	
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

}
