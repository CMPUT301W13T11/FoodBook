package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe;
/**
 * Unit tests for the ServerRecipe class.
 * @author mbabic
 *
 */
public class ServerRecipeTest extends AndroidTestCase {

	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	
	/**
	 * Make sure that a given ServerRecipe is converted without error to a Recipe.
	 * Preservation of image information will have to be tested visually.
	 */
	public void testToRecipe()
	{
		int equal_count = 0;
		Recipe gr = Recipe.generateTestRecipe();
		ServerRecipe sr = new ServerRecipe(gr);
		Recipe ret = ServerRecipe.toRecipe(sr);
		
		/* Confirm equality of generated recipe and results of toRecipe */
		assertTrue("Users not equal.", ret.getAuthor().getName().equals(gr.getAuthor().getName()));
		assertTrue("Titles not equal.", ret.getTitle().equals(gr.getTitle()));
		assertTrue("Instructions not equal.", ret.getInstructions().equals(gr.getInstructions()));

		/* Make sure the ingredient lists are identical. */
		for (Ingredient i : ret.getIngredients())
		{
			for (Ingredient j : gr.getIngredients())
			{
				if (i.getName().equals(j.getName()) && i.getUnit().equals(j.getUnit())
						&& (i.getQuantity() == j.getQuantity())) {
					equal_count++;
				}
			}
		}
		
		assertTrue("Ingredients not equal.", equal_count == ret.getIngredients().size());
		
		/* Make sure the photos are identical. */
		equal_count = 0;
		assertTrue("Photo lists sizes not equal.", ret.getPhotos().size() == gr.getPhotos().size());
		for (int i = 0; i < ret.getPhotos().size(); i++)
		{
			if (ret.getPhotos().get(i).getId().equals(gr.getPhotos().get(i).getId()))
			{
				equal_count++;
			}
		}
		assertTrue("Photo lists not identical.", equal_count == ret.getPhotos().size());
		
	}

	/**
	 * Make sure that a given Recipe is converted without error to a ServerRecipe.
	 * Preservation of image information will have to be tested visually.
	 */
	public void testServerRecipe() 
	{
		int equal_count = 0;
		Recipe recipe = Recipe.generateTestRecipe();
		ServerRecipe sr = new ServerRecipe(recipe);
		
		/* Confirm (near) equality of the ServerRecipe constructed and the generated test Recipe. */
		assertTrue("Users not equal.", recipe.getAuthor().getName().equals(sr.getAuthor().getName()));
		assertTrue("Titles not equal.", recipe.getTitle().equals(sr.getTitle()));
		assertTrue("Instructions not equal.", recipe.getInstructions().equals(sr.getInstructions()));

		/* Make sure the ingredient lists are identical. */
		for (Ingredient i : recipe.getIngredients())
		{
			for (Ingredient j : sr.getIngredients())
			{
				if (i.getName().equals(j.getName()) && i.getUnit().equals(j.getUnit())
						&& (i.getQuantity() == j.getQuantity())) {
					equal_count++;
				}
			}
		}
		
		assertTrue("Ingredients not equal.", equal_count == recipe.getIngredients().size());
		
		/* Make sure the photos are identical. */
		equal_count = 0;
		assertTrue("Photo lists sizes not equal.", recipe.getPhotos().size() == sr.getPhotos().size());
		for (int i = 0; i < recipe.getPhotos().size(); i++)
		{
			if (recipe.getPhotos().get(i).getId().equals(sr.getPhotos().get(i).getId()))
			{
				equal_count++;
			}
		}
		assertTrue("Photo lists not identical.", equal_count == recipe.getPhotos().size());
	}


}
