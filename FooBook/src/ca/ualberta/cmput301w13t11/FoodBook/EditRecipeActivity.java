package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class EditRecipeActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_recipe, menu);
		return true;
	}
	
	/** Called when the user clicks the Photos button */
	public void GoToEditPhotos(View view) {
		Intent intent = new Intent(this, EditPhotos.class);
		startActivity(intent);
	}
	/** Called when the user clicks the Ingredients button */
	public void GoToEditIngredients(View view) {
		Intent intent = new Intent(this, EditIngredientActivity.class);
		startActivity(intent);
	}
	/** Called when the user clicks the Delete Recipe button */
	public void DeleteRecipe(View view) {
		GoToMyRecipes(view);
	}
	/** Called when the user clicks the Save Changes button */
	public void SaveRecipe(View view) {
		GoToMyRecipes(view);
	}
	/** Called when the user clicks the Back button */
	public void GoToMyRecipes(View view) {
		Intent intent = new Intent(this, MyRecipes.class);
		startActivity(intent);
	}

}
