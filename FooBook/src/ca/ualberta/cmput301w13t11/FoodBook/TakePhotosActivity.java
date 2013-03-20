package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;

public class TakePhotosActivity extends Activity implements FView<DbManager>
{
	
	int CAMERA_PIC_REQUEST = 1337; 

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
	
	public void OnGoBack(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 TakePhotosActivity.this.finish();
    }
	public void OnCapture(View View)
    {
		// responds to button Capture
		
		/*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // request code

        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    	Intent intent = new Intent(this, AddRecipesActivity.class);
		startActivity(intent);*/
    }
	public void OnSavePhoto (View View)
    {
		// responds to button Save Photo
    	Intent intent = new Intent(this, AddRecipesActivity.class);
		startActivity(intent);
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
/*
public class demo extends Activity {

Button ButtonClick;
int CAMERA_PIC_REQUEST = 1337; 


@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    ButtonClick =(Button) findViewById(R.id.Camera);
    ButtonClick.setOnClickListener(new OnClickListener (){
        @Override
        public void onClick(View view)
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            // request code

            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

        }
    });

}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) 
{
    if( requestCode == 1337)
    {
    //  data.getExtras()
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

          Now you have received the bitmap..you can pass that bitmap to other activity
          and play with it in this activity or pass this bitmap to other activity
          and then upload it to server.
    }
    else 
    {
        Toast.makeText(demo.this, "Picture NOt taken", Toast.LENGTH_LONG);
    }
    super.onActivityResult(requestCode, resultCode, data);
}
}
*/
