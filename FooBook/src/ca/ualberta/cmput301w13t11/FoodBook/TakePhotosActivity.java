package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

public class TakePhotosActivity extends Activity implements FView<DbManager>
{
	
	private static final int CAMERA_REQUEST = 1337;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		DbController DbC = DbController.getInstance(this, this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photos);
		this.imageView = (ImageView)this.findViewById(R.id.imageView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photos, menu);
		return true;
	}
	
	private Bitmap bitmap;
	//private Uri fileUri;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            bitmap = (Bitmap) data.getExtras().get("data");

 			// If camera is not working, or for testing purposes, use BogoPicGen
			//bitmap = BogoPicGen.generateBitmap(1024, 1024);

            imageView.setImageBitmap(bitmap);
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
		
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }
	public void OnSavePhoto (View View)
    {
		
		/*
		// responds to button Save Photo
		File file = new File(Environment.getExternalStorageDirectory()+File.separator +        "image.jpg");
        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            //5
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
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
