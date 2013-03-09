package ca.ualberta.cmput301w13t11.FoodBook.test;

import org.junit.Test;

import junit.framework.TestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.*;

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
		
	}
}
