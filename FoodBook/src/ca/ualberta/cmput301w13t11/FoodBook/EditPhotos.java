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
	public void OnGobacktoPrevScreen(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 EditPhotos.this.finish();
    }
	public void OnTakePhoto(View View)
    {
		// responds to button TakePhoto
    	Intent intent = new Intent(this, TakePhotosActivity.class);
		startActivity(intent);
    }
	public void OnSavePhoto (View View)
    {
		// responds to button Save Photo
    	
    }

}