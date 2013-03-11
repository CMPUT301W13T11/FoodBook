package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;

/**
 * Unit tests for he DbController class.
 * @author mbabic
 *
 */
public class DbControllerTests extends AndroidTestCase {

	private DbController dbc = null;
	
	/**
	 * Mock View implementing the FView<DbManager> interface for testing purposes.
	 * @author mbabic
	 *
	 */
	private class MockView implements FView<DbManager>
	{
		public int x;
		public MockView(int x)
		{
			this.x = x;
		}
		
		@Override
		public void update(DbManager m)
		{
			this.x++;
		}
	}
	
	
	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	
	/**
	 * Test getInstance method.
	 */
	public void testGetInstance()
	{
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		assertTrue("instance should not be null", dbc != null);
	}
	
	/**
	 * Test getUserRecipes() -- attempt to get recipes from database without error using 
	 * the controller's functionality.
	 */
	public void testGetUserRecipes()
	{
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		
		try {
			ArrayList<Recipe> recipes = dbc.getUserRecipes();
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test insertRecipe() -- attempt to insert recipe into database without error using 
	 * the controller's functionality.
	 */
	public void testInsertRecipeNoErrors()
	{
		Recipe recipe = Recipe.generateTestRecipe();
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		
		try {
			dbc.addRecipe(recipe);
		} catch (Exception e) {
			fail();
		}
		
	}
	
	/**
	 * Test insertRecipe() and getUserRecipes() -- attempt to insert recipe into database without error using 
	 * the controller's functionality.
	 */
	public void testInsertRecipeAndGetUserRecipes()
	{
		Recipe recipe = Recipe.generateTestRecipe();
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		
		try {
			dbc.addRecipe(recipe);
		} catch (Exception e) {
			fail();
		}
		
		ArrayList<Recipe> ret = dbc.getUserRecipes();
		assertTrue("Database should not be empty.", !ret.isEmpty());
	}
	
	/**
	 * Test deleteRecipe() -- attempt to delete a recipe known to not exist from the database and ensure
	 * it's size has not changed and no errors occur.
	 * the controller's functionality.
	 */
	public void testDeleteRecipe()
	{
		Recipe recipe = Recipe.generateTestRecipe();
		Recipe r1 = new Recipe(new User("tester"), "title1", "insructions");
		Recipe r2 = new Recipe(new User("tester"), "title2", "insructions");
		Recipe not_in_db = new Recipe((long)9999, new User(";aljfpoiwefu qwu"), "uicy9w2y9 hjkwleh ", "w8u q7yt",
							new ArrayList<Ingredient>());

		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);
		
		try {
			dbc.addRecipe(recipe);
			dbc.addRecipe(r1);
			dbc.addRecipe(r2);
		} catch (Exception e) {
			fail();
		}
		
		int size = dbc.getUserRecipes().size();
		dbc.deleteRecipe(not_in_db);
		assertTrue("Size should not have changed.", size == dbc.getUserRecipes().size());	
	}
	
	/**
	 * Test addIngedientToRecipe() -- ensures that a recipe can be added to a recipe.
	 */
	public void testAddIngredientToRecipe()
	{
		Recipe recipe = new Recipe((long)9009, new User("test"), "title", "instructions",
							new ArrayList<Ingredient>());
		
		MockView view = new MockView(1);
		dbc = DbController.getInstance(this.getContext(), view);

		try {
			dbc.addRecipe(recipe);
		} catch (Exception e) {
			fail();
		}
		int flag = 0;
		
		Ingredient ingredient = new Ingredient("x", "x", 1);
		dbc.addIngredientToRecipe(ingredient, recipe);
		for (Recipe r : dbc.getUserRecipes())
		{
			if (!r.getIngredients().isEmpty()) {
				if (!r.getIngredients().get(0).equals("x")) {
					flag = 1;
				}
			}
		}
		assertTrue("Ingredient should have been found.", flag == 1);

	}
	
	
}
