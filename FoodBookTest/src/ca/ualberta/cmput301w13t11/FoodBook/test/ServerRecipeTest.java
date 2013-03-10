package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import android.graphics.Bitmap;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Unit tests for the ServerRecipe class.
 * @author mbabic
 *
 */
public class ServerRecipeTest extends TestCase {

	/**
	 * Used to generate a recipe used in the tests.
	 * @return A recipe with a user, a title, a uri, a set of instructions, a set of ingredients, and a set of photos.
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
		Photo photo2 = new Photo(name1, byte_data2);
		ArrayList<Photo> photos = new ArrayList<Photo>();
		photos.add(photo1);
		photos.add(photo2);

		/* Generate recipe. */
		Recipe ret = new Recipe(uri, user, title, instructions, ingredients, photos);
		return ret;

	}
	@Test
	/*
	 * Test the ServerRecipe constructor.
	 */
	public void testServerRecipeConstructor() 
	{
		ServerRecipe sr = new ServerRecipe(generateRecipe());
		assertTrue("ServerRecipe constructor failed.", sr != null);	
	}
	
	@Test
	/*
	 * Test the ServerRecipe toRecipe function.
	 */
	public void testToRecipe()
	{
		Recipe gr = generateRecipe();
		ServerRecipe sr = new ServerRecipe(gr);
		Recipe ret = ServerRecipe.toRecipe(sr);
		
		/* Confirm equality of generated recipe and results of toRecipe */
		assertTrue("Users not equal.", ret.getAuthor().getName().equals(gr.getAuthor().getName()));
		assertTrue("Title not equal.", ret.getTitle().equals(gr.getTitle()));
	}

}
