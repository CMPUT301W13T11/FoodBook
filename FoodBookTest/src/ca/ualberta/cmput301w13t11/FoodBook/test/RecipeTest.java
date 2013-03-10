package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Unit tests for the Recipe class.
 * @author mbabic
 *
 */
public class RecipeTest extends TestCase {

	/**
	 * Generates a simple recipe to be used in the tests.
	 * @return A recipe to be used in the tests.
	 */
	private Recipe generateRecipe()
	{
		User user = new User("tester");
		String title = "test";
		String instructions = "test instructions";
		long uri = 110101012;
		
		/* Generate ingredient list. */
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("egg", "whole", (float) 1/2));
		ingredients.add(new Ingredient("butter", "tbsp", (float) 4));
		ingredients.add(new Ingredient("sdf", "sdf", (float) 1/4));
		ingredients.add(new Ingredient("milk", "mL", (float) 5000));
		ingredients.add(new Ingredient("veal", "the whole baby cow", (float) 1));
		
		/* Generate photos */
		String name1 = "test1";
		String name2 = "test2";
		byte[] byte_data1 = new byte[10];
		byte[] byte_data2 = new byte[10];

		Photo photo1 = new Photo(name1, byte_data1);
		Photo photo2 = new Photo(name2, byte_data2);
		ArrayList<Photo> photos = new ArrayList<Photo>();
		photos.add(photo1);
		photos.add(photo2);

		/* Generate recipe. */
		Recipe ret = new Recipe(uri, user, title, instructions, ingredients, photos);
		return ret;

	}
	@Test
	/*
	 * Test the Recipe constructors to ensure there are no errors (only testing empty photo
	 * set for now).
	 */
	public void testRecipeConstructors()
	{
		String instructions = "mix everything together, eat raw";
		
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("egg", "whole", (float) 1/2));
		ingredients.add(new Ingredient("butter", "tbsp", (float) 4));
		ingredients.add(new Ingredient("sdf", "sdf", (float) 1/4));
		ingredients.add(new Ingredient("milk", "mL", (float) 5000));
		ingredients.add(new Ingredient("veal", "the whole baby cow", (float) 1));

		ArrayList<Photo> photos = new ArrayList<Photo>();
		Photo photo = new Photo("testname", new byte[10]);
		long test_uri = 1125121;
		Recipe recipe1 = new Recipe(new User("test"), "Test Recipe 1");
		assertTrue("Recipe 1 failure.", recipe1 != null);
		
		Recipe recipe2 = new Recipe(new User("test"), "Test Recipe 2", instructions);
		assertTrue("Recipe 2 failure", recipe2 != null);
		
		Recipe recipe3 = new Recipe(new User("test"), "Test Recipe 3", instructions, ingredients);
		assertTrue("Recipe 3 failure", recipe3 != null);
		
		Recipe recipe4 = new Recipe(new User("test"), "Test Recipe 4", instructions, ingredients, photos);
		assertTrue("Recipe 4 failure", recipe4 != null);	
		
		Recipe recipe5 = new Recipe(test_uri, new User("test"), "Test Recipe 5", instructions, ingredients);
		assertTrue("Recipe 5 failure", recipe5 != null);	

	}
	
	@Test
	/*
	 * The add and delete photos method for Recipe.
	 */
	public void testAddDeletePhotos()
	{
		int found_flag = 0;
		Recipe recipe = generateRecipe();
		Photo toAdd = new Photo("toAdd", new byte[10]);
		
		/* Test adding the photo */
		recipe.addPhoto(toAdd);
		for (Photo p : recipe.getPhotos())
		{
			if (p.getName().equals("toAdd"))
				found_flag = 1;
		}
		assertTrue("Photo not added.", found_flag == 1);
		
		/* Test deleting the photo */
		found_flag = 0;
		recipe.deletePhoto(toAdd);
		for (Photo p : recipe.getPhotos())
		{
			if (p.getName().equals("toAdd"))
				found_flag = 1;
		}
		assertTrue("Photo not added.", found_flag == 0);
		
	}
}
