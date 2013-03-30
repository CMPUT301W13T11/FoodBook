package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.database.Cursor;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
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

	/**
	 * Test the functionality of the updateField() method.
	 * Ensure that after calling the method an ingredient known to be unique in the database and with
	 * known initial fields values is successfully stored with the new user-specified field value, while
	 * also ensuring that the the old version of the ingredient is no longer in the database..
	 */
	public void testUpdateField()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		String name = Long.toString(System.currentTimeMillis());
		Ingredient ingredient = new Ingredient(name, "test unit", (float) 100);
		idb.insert(ingredient);

		String newUnit = Long.toString(System.currentTimeMillis());

		//	private void updateField(String name, String field, String value) {


		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[3];
			args[0] = String.class;
			args[1] = String.class;
			args[2] = String.class;
			Method method = idb.getClass().getDeclaredMethod("updateField", args);
			method.setAccessible(true);
			method.invoke(idb, name, "unit", newUnit);

			ArrayList<Ingredient> ingredients = idb.get();

			/* attempt to find ingredient in returned array*/
			int i = 0;
			for (i = 0; i < ingredients.size(); i++) {
				if (ingredients.get(i).getUnit().equals(newUnit)) {
					break;
				}
			}
			assertTrue("Should be able to find new ingredient in database.", i < ingredients.size());

			/* Ensure equality of the other fields. */
			assertTrue("Names should be the same.", ingredients.get(i).getName().equals(ingredient.getName()));
			assertTrue("Quantities should be the same.", ingredients.get(i).getQuantity() == ingredient.getQuantity());

			/* Now ensure that old ingredient cannot be found. */
			for (i = 0; i < ingredients.size(); i++) {
				if (ingredients.get(i).getName().equals(name)) {
					if (ingredients.get(i).getUnit().equals("test unit")) {
						if (ingredients.get(i).getQuantity() == 100) {
							break;
						}
					}
				}

			}

			assertTrue("Should not have been able to find old ingredient in the database.", i >= ingredients.size());


		} catch (NoSuchMethodException nsme) {
			fail("NoSuchMethodException");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException");
		} catch (IllegalAccessException e) {
			fail("IllegalAccessException");
		} catch (InvocationTargetException e) {
			fail("InvocationTargetException");
		}


	}

	/**
	 * Test the functionality of the updateIngredient() method.
	 * Ensure that after calling the method an ingredient known to be unique in the database and with
	 * known initial fields values is successfully stored with the new user-specified field values, while
	 * also ensuring that the the old version of the ingredient is no longer in the database.
	 */
	public void testUpdateIngredient()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		String name = Long.toString(System.currentTimeMillis());
		Ingredient ingredient = new Ingredient(name, "test unit", (float) 100);
		idb.insert(ingredient);

		String newName = Long.toString(System.currentTimeMillis());
		ingredient.setName(newName);
		ingredient.setUnit("not test unit");
		ingredient.setQuantity(99);

		idb.updateIngredient(ingredient, name);
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
			if (ingredients.get(i).getName().equals(name)) {
				if (ingredients.get(i).getUnit().equals("test unit")) {
					if (ingredients.get(i).getQuantity() == 100) {
						break;
					}
				}
			}

		}

		assertTrue("Should not have been able to find old ingredient in the database.", i >= ingredients.size());
	}

	/**
	 * Test the functionality of cursorToMyIngredients()
	 * Query the database to receive and cursor and ensure that a known recipe is correctly present
	 * in the returned cursor.
	 */
	public void testCursorToMyIngredients()
	{
		idb = IngredientsDbManager.getInstance(this.getContext());
		if (idb == null) {
			fail("failed to get instance of IngredientsDbManager");
		}
		String name = Long.toString(System.currentTimeMillis());
		Ingredient ingredient = new Ingredient(name, "test unit", (float) 100);
		idb.insert(ingredient);
		
		DbManager dbm = DbManager.getInstance(this.getContext());
		Cursor cursor = DbManager.getDb().rawQuery("SELECT * FROM " + idb.getTableName(), null);
		
		try {
			/* Testing private member function, need to use reflection. */
			Class[] args = new Class[1];
			args[0] = Cursor.class;
			Method method = idb.getClass().getDeclaredMethod("cursorToMyIngredients", args);
			method.setAccessible(true);
			ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) method.invoke(idb, cursor);

			/* attempt to find known to exist ingredient in returned array*/
			int i = 0;
			for (i = 0; i < ingredients.size(); i++) {
				if (ingredients.get(i).getName().equals(name)) {
					break;
				}
			}

			/*Ensure equality of remaining fields. */
			assertTrue("Units should be the same.", ingredients.get(i).getUnit().equals(ingredient.getUnit()));
			assertTrue("Quantities should be the same.", ingredients.get(i).getQuantity() == ingredient.getQuantity());
			
		} catch (NoSuchMethodException nsme) {
			fail("NoSuchMethodException");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException");
		} catch (IllegalAccessException e) {
			fail("IllegalAccessException");
		} catch (InvocationTargetException e) {
			fail("InvocationTargetException");
		}
	}
}
