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

	@Test
	/*
	 * Test the ServerRecipe constructor.
	 */
	public void testServerRecipeConstructor() 
	{
		ServerRecipe sr = new ServerRecipe(Recipe.generateTestRecipe());
		assertTrue("ServerRecipe constructor failed.", sr != null);	
	}
	
	@Test
	/*
	 * Test the ServerRecipe toRecipe function.
	 */
	public void testToRecipe()
	{
		Recipe gr = Recipe.generateTestRecipe();
		ServerRecipe sr = new ServerRecipe(gr);
		Recipe ret = ServerRecipe.toRecipe(sr);
		
		/* Confirm equality of generated recipe and results of toRecipe */
		assertTrue("Users not equal.", ret.getAuthor().getName().equals(gr.getAuthor().getName()));
		assertTrue("Title not equal.", ret.getTitle().equals(gr.getTitle()));
	}

}
