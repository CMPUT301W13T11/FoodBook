package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class EditPhotos extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_photos, menu);
		return true;
	}

	/** Called when the user clicks the TakePhoto button */
	public void TakePhoto(View view) {
	
	}
	/** Called when the user clicks the Back button */
	public void GoToEditRecipes(View view) {
		Intent intent = new Intent(this, EditRecipeActivity.class);
		startActivity(intent);
	}
	/** Called when the user clicks the Delete Photo button */
	public void DeletePhoto(View view) {
	}
}
