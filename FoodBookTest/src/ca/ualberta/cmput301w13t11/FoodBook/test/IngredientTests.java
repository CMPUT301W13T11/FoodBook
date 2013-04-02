package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;

public class IngredientTests extends AndroidTestCase {

	protected void setUp() throws Exception
	{
		super.setUp();
	}
	
	/**
	 * Test the functionality of the Ingredient instructor by ensuring
	 * it returns an Ingredient with expected parameters.
	 */
	public void testConstructor()
	{
		String testName = "test name";
		String testUnit = "test unit";
		float testQuantity = (float) 100.5;
		Ingredient test = new Ingredient(testName, testUnit, testQuantity);
		
		assertTrue("Created ingredient name should be equal to testName",
				test.getName().equals(testName));
		assertTrue("Created ingredient unit shold be equal to testUnit",
				test.getUnit().equals(testUnit));
		assertTrue("Created ingredient quantity should be equal to testQuantity",
				test.getQuantity() == testQuantity);
	}
	
	/**
	 * Test the functionality of the toContentValues() method by ensuring that an
	 * ingredient of known parameters is mapped to a ContentValues item with correct
	 * key values.
	 */
	public void testToContentValues()
	{
		String testName = "test name";
		String testUnit = "test unit";
		float testQuantity = (float) 100.5;
		Ingredient test = new Ingredient(testName, testUnit, testQuantity);
		ContentValues ret = test.toContentValues();
		
		assertTrue("Content value with key \"name\" should have same name as test ingredient's name",
				test.getName().equals( (String) ret.get("name")));
		assertTrue("Content value with key \"unit\" should have same value as test ingredient's unit",
				test.getUnit().equals( (String) ret.get("unit")));
		assertTrue("Content value with key \"quantity\" should have same value as test ingredient's quantity",
				test.getQuantity() == (Float) ret.get("quantity"));
	}
	
	/**
	 * Test the functionality of the toString() by simply ensuring it does not return void
	 * as the precise string it returns may need to be changed over time.
	 */
	public void testToString()
	{
		String testName = "test name";
		String testUnit = "test unit";
		float testQuantity = (float) 100.5;
		Ingredient test = new Ingredient(testName, testUnit, testQuantity);
		assertTrue("Returned string should not be null.", test.toString() != null);
	}
}
