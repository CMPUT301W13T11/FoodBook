package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/* Mark: for now, put all of the relevant data for
 * a recipe (author, title, instructions, ingredients)
 * into a ContentValues. I'll give you a Controller with
 * the proper method to use soon
 */

public class AddRecipesActivity extends Activity
{

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
