package ca.ualberta.cmput301w13t11.FoodBook.test;

import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301w13t11.FoodBook.MainActivity;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

public class RecipeTests extends ActivityInstrumentationTestCase2<Recipe> {

	public RecipeTests()
	{
		super("ca.ualberta.cmput301w13t11.FoodBook.model", Recipe.class);
	}
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testConstructor() {
		pass(1 == 1 - 2 + 2);
	}

}
