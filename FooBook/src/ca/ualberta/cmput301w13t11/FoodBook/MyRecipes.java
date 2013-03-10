package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.FModel;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

public class MyRecipes extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_recipes);
		// Mark: somewhere here you need to call updateContent to get data
		updateContent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_recipes, menu);
		return true;
	}
	
	// Mark: every view must implement the 'update' method
	public void update(FModel model) {
		updateContent();
	}
	
	public void updateContent() {
		DbController controller = RecipeApplication.getDbController();
		ArrayList<Recipe> recipes = controller.getUserRecipes();
		/* now you guys have to figure out how to put this array of
		 * recipes into the UI
		 */
	}

}
