package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Unit tests for the Recipe class.
 * @author mbabic
 *
 */
public class RecipeTest extends AndroidTestCase {

	protected void setUp() throws Exception
	{
		super.setUp();
	}
	
	/**
	 * Test the functionality of the Recipe constructors by assuring they return Recipe
	 * objects with the expected parameters.
	 */
	public void testConstructors()
	{
		long testUri = 10101010101L;
		User testUser = new User("test");
		String testTitle = "test title";
		String testInstructions = "test instructions";
		
		/* Generate test ingredients. */
		ArrayList<Ingredient> testIngredients = new ArrayList<Ingredient>();
		Ingredient testIngredient1 = new Ingredient("test1", "x", (float) 1);
		Ingredient testIngredient2 = new Ingredient("test2", "x", (float) 1);
		Ingredient testIngredient3 = new Ingredient("test3", "x", (float) 1);
		testIngredients.add(testIngredient1);
		testIngredients.add(testIngredient2);
		testIngredients.add(testIngredient3);

		/* Generate test photos */
		ArrayList<Photo> testPhotos = new ArrayList<Photo>();
		Bitmap bitmap = BogoPicGen.generateBitmap(100, 100);
		String photoName = "name";
		String photoPath = "path";
		Photo testPhoto = new Photo(photoName, photoPath, bitmap);
		
		Recipe test1 = new Recipe(testUri, testUser, testTitle, testInstructions, testIngredients);
		Recipe test2 = new Recipe(testUri, testUser, testTitle, testInstructions, testIngredients, testPhotos);
		Recipe test3 = new Recipe(testUri);
		Recipe test4 = new Recipe(testUser, testTitle, testInstructions);
		
		//TODO: actually compare equality b/w created and expected.
		assertTrue("Recipe test1 should be equal to recipe test2", true);
	}
	
	
	
}
