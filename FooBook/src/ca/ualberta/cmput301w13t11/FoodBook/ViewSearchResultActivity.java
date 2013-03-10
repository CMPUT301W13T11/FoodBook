package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ViewSearchResultActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_search_result, menu);
		return true;
	}

	/** Called when the user clicks the Back button */
	public void GoToSearchResults(View view) {
			Intent intent = new Intent(this, SearchResultsActivity.class);
			startActivity(intent);
	}
	/** Called when the user clicks the Add Photos button */
	public void AddPhotos(View view) {
			Intent intent = new Intent(this, TakePhotosActivity.class);
			startActivity(intent);
	}
	/** Called when the user clicks the Download Photo button */
	public void DownloadRecipe(View view) {
			Intent intent = new Intent(this, MyRecipes.class);
			startActivity(intent);
	}
	/** Called when the user clicks the View Photos button */
	public void ViewPhotos(View view) {
			Intent intent = new Intent(this, ViewPhotosActivity.class);
			startActivity(intent);
	}
	
}
