package ca.ualberta.cmput301w13t11.FoodBook.test;

import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Unit tests for the Recipe class.
 * @author mbabic
 *
 */
public class RecipeTest extends TestCase {

	@Test
	public void testRecipe()
	{
		Recipe recipe = new Recipe(new User("test"), "Test Recipe");
		assertTrue(recipe != null);
		
	}
}
