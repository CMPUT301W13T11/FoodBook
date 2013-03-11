package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ViewPhotosActivity extends Activity implements FView<DbManager>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_photos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_photos, menu);
		return true;
	}
	
	public void OnGobacktoPrevScreen(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 ViewPhotosActivity.this.finish();
    }

	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
