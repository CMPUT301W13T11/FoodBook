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

	/** Called when the user clicks the Back button */
	public void GoToMainMenu(View view) {
		Intent intent = new Intent(this, MainScreen.class);
		startActivity(intent);
	}
	
	/** Called when the user clicks the capture button */
	public void CapturePhoto(View view) {}
	/** Called when the user clicks the Save button */
	public void SavePhoto(View view) {
		GoToMainMenu(view);
	}
}
