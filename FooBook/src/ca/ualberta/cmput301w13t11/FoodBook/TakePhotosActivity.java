package ca.ualberta.cmput301w13t11.FoodBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;

public class TakePhotosActivity extends Activity implements FView<DbManager>
{
	
	private static final int CAMERA_REQUEST = 1337;
	static final String EXTRA_URI = "extra_uri";
	private long uri;
	private ImageView imageView;
	private Bitmap bitmap = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photos);
		this.imageView = (ImageView)this.findViewById(R.id.imageView1);
		//this.updateView();
		
	}
	public void updateView(){
		this.imageView.setImageBitmap(bitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photos, menu);
		return true;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            this.bitmap = (Bitmap) data.getExtras().get("data");

        }  
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
		
		DbController DbC = DbController.getInstance(this, this);
		if (DbC.isSDcardInstalled()){
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
		else{
		//If sd card not installed on vm, use BogoPicGen  
		this.bitmap = BogoPicGen.generateBitmap(640, 480);
		}
		this.updateView();

    }
	public void OnSavePhoto (View View)
    {
		// responds to button Save Photo
		if (bitmap!=null){
			
			DbController DbC = DbController.getInstance(this, this);
			
			boolean success = DbC.addPhotoToRecipe(bitmap, uri);
			if (success) {	
				Toast.makeText(getApplicationContext(), "Image saved with success",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error during image saving", Toast.LENGTH_LONG).show();
			}
        
		}
    }

	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		this.updateView();
		
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
}
