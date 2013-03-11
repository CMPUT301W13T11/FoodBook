package ca.ualberta.cmput301w13t11.FoodBook.model;

import ca.ualberta.cmput301w13t11.FoodBook.EditIngredientActivity;
import ca.ualberta.cmput301w13t11.FoodBook.EditPhotos;
import ca.ualberta.cmput301w13t11.FoodBook.MyRecipes;
import ca.ualberta.cmput301w13t11.FoodBook.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Test activity to try to get Android JUnit test to run correctly -- please ignore.
 * @author mbabic
 *
 */
public class DbManagerTestActivity  extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_recipes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_recipes, menu);
		return true;
	}
	
	/** Called when the user clicks the Back to My Recipes button */
		public void GoToMyRecipes(View view) {
			Intent intent = new Intent(this, MyRecipes.class);
			startActivity(intent);
		}
		
		public void SaveRecipe(View view) {
			GoToMyRecipes(view);
		}
		
		/** Called when the user clicks the Edit Photos button */
		public void GoToEditPhotos(View view) {
			Intent intent = new Intent(this, EditPhotos.class);
			startActivity(intent);
		}
		/** Called when the user clicks the Edit Ingredients button */
		public void GoToEditIngredients(View view) {
			Intent intent = new Intent(this, EditIngredientActivity.class);
			startActivity(intent);
		}
}
