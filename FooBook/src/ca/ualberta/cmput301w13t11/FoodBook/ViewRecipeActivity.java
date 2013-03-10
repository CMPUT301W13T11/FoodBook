package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ViewRecipeActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_recipe, menu);
		return true;
	}

	/** Called when the user clicks the Back button */
	public void GoToMyRecipes(View view) {
			Intent intent = new Intent(this, MyRecipes.class);
			startActivity(intent);
	}
	/** Called when the user clicks the Publish Recipe button */
	public void Publish(View view) {
	}
	/** Called when the user clicks the Edit Photo button */
	public void EditPhotos(View view) {
			Intent intent = new Intent(this, EditPhotos.class);
			startActivity(intent);
	}
	/** Called when the user clicks the EmailRecipe button */
	public void EmailRecipe(View view) {
	}
	/** Called when the user clicks the Edit Recipe button */
	public void EditRecipe(View view) {
			Intent intent = new Intent(this, EditRecipeActivity.class);
			startActivity(intent);
	}
	
}
