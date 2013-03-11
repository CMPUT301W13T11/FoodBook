package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class TakePhotosActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photos, menu);
		return true;
	}
	
	public void OnGobacktoPrevScreen(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 TakePhotosActivity.this.finish();
    }
	public void OnCapture(View View)
    {
		// responds to button Capture
    	Intent intent = new Intent(this, AddRecipesActivity.class);
		startActivity(intent);
    }
	public void OnSavePhoto (View View)
    {
		// responds to button Save Photo
    	Intent intent = new Intent(this, AddRecipesActivity.class);
		startActivity(intent);
    }
}
