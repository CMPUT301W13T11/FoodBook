package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
/**
 * Unit tests for the IngredientsDbManager class.
 * @author mbabic
 *
 */
public class IngredientsDbManagerTests extends AndroidTestCase {

	private IngredientsDbManager idb = null;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		
	}
	
	/**
	 * Test the getInstance() method to ensure a non-null instance of IngredientsDbManager
	 */
	public void testGetInstance()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		assertTrue("Test should not be null.", idb != null);
		assertTrue("getInstance() (method w/o args) should not return null now.", IngredientsDbManager.getInstance() != null);
	}

	/**
	 * Test the functionality of the insert() method.
	 * Ensure that insertion of an ingredient procedes without error.
	 */
	public void testInsert()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		
		Ingredient ingredient = new Ingredient("test name", "test unit", (float) 100);
		idb.insert(ingredient);
	}
	
	
	/**
	 * Test the functionality of the get() method.
	 * Query a database we know to be non-empty and ensure that 
	 */
	public void testGet()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		Ingredient ingredient = new Ingredient("test name 298q n2sdfasdfasdfwf23fffpuhtg7tg 3tghvjbhadiu", "test unit", (float) 100);
		idb.insert(ingredient);
		
		ArrayList<Ingredient> ingredients = idb.get();
		assertTrue("ArrayList returned should be non-empty.", !ingredients.isEmpty());
		
		/* Attempt to find the ingredient we stored */
		int i = 0;
		for (i = 0; i < ingredients.size();i ++) {
			if (ingredients.get(i).getName().equals(ingredient.getName()))
				break;
		}
		
		/* Ensure equality of the other fields. */
		assertTrue("Units should be the same.", ingredients.get(i).getUnit().equals(ingredient.getUnit()));
		assertTrue("Quantities should be the same.", ingredients.get(i).getQuantity() == ingredient.getQuantity());

	}
	
	/**
	 * Test the functionality of the delete() method.
	 * We attempt to remove an ingredient we know to exist in the database, then call delete to attempt to remove it,
	 * then ensure that it cannot be found when we re-query the database.
	 */
	public void testDelete() 
	{	
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		Ingredient ingredient = new Ingredient("test name 298q n2sdfpuhtg7tg 3tghvjbhadiu", "test unit", (float) 100);
		idb.insert(ingredient);
		
		ArrayList<Ingredient> ingredients = idb.get();
		assertTrue("ArrayList returned should be non-empty.", !ingredients.isEmpty());
		
		/* Attempt to find the ingredient we stored */
		int i = 0;
		for (i = 0; i < ingredients.size();i ++) {
			if (ingredients.get(i).getName().equals(ingredient.getName()))
				break;
		}
		
		/* Ensure equality of the other fields. */
		assertTrue("Units should be the same.", ingredients.get(i).getUnit().equals(ingredient.getUnit()));
		assertTrue("Quantities should be the same.", ingredients.get(i).getQuantity() == ingredient.getQuantity());
		
		/* now attempt deletion */
		idb.delete(ingredient);
		
		ArrayList<Ingredient> ingredients2 = idb.get();
		
		for (i = 0; i < ingredients2.size(); i++) {
			if (ingredients2.get(i).getName().equals(ingredient.getName())) {
				assertTrue("Should not be able to find old ingredient in database.", true);
			}
		}
	}
	
	
	
	public void testUpdateIngredient()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		Ingredient ingredient = new Ingredient("x", "test unit", (float) 100);
		idb.insert(ingredient);
		
		String newName = Long.toString(System.currentTimeMillis());
		ingredient.setName(newName);
		ingredient.setUnit("not test unit");
		ingredient.setQuantity(99);

		idb.updateIngredient(ingredient, "x");
		ArrayList<Ingredient> ingredients = idb.get();
		
		/* attempt to find ingredient in returned array*/
		int i = 0;
		for (i = 0; i < ingredients.size(); i++) {
			if (ingredients.get(i).getName().equals(ingredient.getName())) {
				break;
			}
		}
		
		assertTrue("Should be able to find new ingredient in database.", i < ingredients.size());

		/* Ensure equality of the other fields. */
		assertTrue("Units should be the same.", ingredients.get(i).getUnit().equals(ingredient.getUnit()));
		assertTrue("Quantities should be the same.", ingredients.get(i).getQuantity() == ingredient.getQuantity());
		
		/* Now ensure that old ingredient cannot be found. */
		for (i = 0; i < ingredients.size(); i++) {
			if (ingredients.get(i).getName().equals("testUpdateIngredientname")) {
				if (ingredients.get(i).getUnit().equals("test unit")) {
					if (ingredients.get(i).getQuantity() == 100) {
						break;
					}
				}
			}
			
		}
		
		assertTrue("Should not have been able to find old ingredient in the database.", i >= ingredients.size());
	}
}
