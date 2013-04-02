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
		testPhotos.add(testPhoto);
		
		Recipe test1 = new Recipe(testUri, testUser, testTitle, testInstructions, testIngredients);
		Recipe test2 = new Recipe(testUri, testUser, testTitle, testInstructions, testIngredients, testPhotos);
		Recipe test3 = new Recipe(testUri);
		Recipe test4 = new Recipe(testUser, testTitle, testInstructions);
		
		/* Assert equalities for Recipe test 1*/
		assertTrue("Test 1 should have uri equal to testUri", test1.getUri() == testUri);
		assertTrue("test 1 should have title equal to testTitle", test1.getTitle().equals(testTitle));
		assertTrue("test 1 should have same author name as testUser", test1.getAuthor().getName().equals(testUser.getName()));
		assertTrue("test1 should have instructions equal to testInstructions", test1.getInstructions().equals(testInstructions));
		
		ArrayList<Ingredient> temp = test1.getIngredients();
		for (int i = 0; i < testIngredients.size(); i++) {
			assertTrue("test1 ingredient " + i + " should have same name as testIngredient" + (i+1),
					temp.get(i).getName().equals(testIngredients.get(i).getName()));
			assertTrue("test1 ingredient " + i + " should have same unit type as testIngredient" + (i+1),
					temp.get(i).getUnit().equals(testIngredients.get(i).getUnit()));
			assertTrue("test1 ingredient " + i + " should have same unit type as testIngredient" + (i+1),
					temp.get(i).getQuantity() == testIngredients.get(i).getQuantity());
		}
		
		assertTrue("test1 should have empty photos list", test1.getPhotos().isEmpty());
		
		/* Assert equalities for Recipe test 2 */
		assertTrue("test2 should have uri equal to testUri", test2.getUri() == testUri);
		assertTrue("test2 should have title equal to testTitle", test2.getTitle().equals(testTitle));
		assertTrue("test2 should have same author name as testUser", test2.getAuthor().getName().equals(testUser.getName()));
		assertTrue("test2 should have instructions equal to testInstructions", test2.getInstructions().equals(testInstructions));
		
		temp = test2.getIngredients();
		for (int i = 0; i < testIngredients.size(); i++) {
			assertTrue("test2 ingredient " + i + " should have same name as testIngredient" + (i+1),
					temp.get(i).getName().equals(testIngredients.get(i).getName()));
			assertTrue("test2 ingredient " + i + " should have same unit type as testIngredient" + (i+1),
					temp.get(i).getUnit().equals(testIngredients.get(i).getUnit()));
			assertTrue("test2 ingredient " + i + " should have same unit type as testIngredient" + (i+1),
					temp.get(i).getQuantity() == testIngredients.get(i).getQuantity());
		}
		
		ArrayList<Photo> ptemp = test2.getPhotos();
		for (int i = 0; i < testPhotos.size(); i++) {
			assertTrue("test2 photo " + i + " should have same id as testPhoto" + (i+1),
					ptemp.get(i).getId().equals(testPhotos.get(i).getId()));
			assertTrue("test2 ingredient " + i + " should have same unit type as testIngredient" + (i+1),
					ptemp.get(i).getPath().equals(testPhotos.get(i).getPath()));

		}
		
		/* Assert equalities for test 3 */
		assertTrue("test3 should have uri equal to testUri", test3.getUri() == testUri);
		assertTrue("test3 should have blank title", test3.getTitle().equals(""));
		assertTrue("test3 should have author with blank name", test3.getAuthor().getName().equals(""));
		assertTrue("test3 should have blank instructions", test3.getInstructions().equals(""));
		assertTrue("test3 should have empty ingredients list", test3.getIngredients().isEmpty());
		assertTrue("test3 should have empty photos list", test3.getPhotos().isEmpty());
		
		/* Assert equalities for test 4 */
		assertTrue("test4 should have equal to test title", test4.getTitle().equals(testTitle));
		assertTrue("test4 should have author with same name as testUser", test4.getAuthor().getName().equals(testUser.getName()));
		assertTrue("test4 should have instructions equal to testInstructions", test4.getInstructions().equals(testInstructions));
		assertTrue("test4 should have empty ingredients list", test4.getIngredients().isEmpty());
		assertTrue("test4 should have empty photos list", test4.getPhotos().isEmpty());
		
	}
	
	
	
}
