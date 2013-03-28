package ca.ualberta.cmput301w13t11.FoodBook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
/**
 *
 */
public class TakePhotosActivity extends Activity implements FView<DbManager>
{
	
	private static final int CAMERA_REQUEST = 1337;
	static final String EXTRA_URI = "extra_uri";
	private long uri;
	private ImageView imageView;
	static private final Logger logger = Logger.getLogger(TakePhotosActivity.class.getName());

	protected Bitmap bitmap;
	private String imgPath = null;
	private File file;
	private ProgressDialog progressDialog;
	boolean success = false;
	boolean worked = false;
	private FileOutputStream outStream;
	private DbController DbC; 
	
	//Here's where you choose external or internal image storage  
    //Change to 1 save pictures to the SD card, 
    //Change to 0 to save to local folder 'Pictures' and use BogoPicGen
	//Note that if an actual SD card is not installed, the capturing reverts to local save and BogoPicGen
	
    //protected static boolean SDCARD_INSTALLED = true;
	
	//Automatically done with this
	
	public String state = Environment.getExternalStorageState();
    
    protected static String PICTURES_DIRECTORY = "Pictures";
    

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photos);
		
		Intent intent = getIntent();
		//String extraUri = intent.getStringExtra(EditPhotos.EXTRA_URI);
		//uri = Long.parseLong(extraUri);
		uri = intent.getLongExtra(EXTRA_URI, 0);
		logger.log(Level.INFO, "Uri passed to TakePhotosActivity: " + uri);
		imageView = (ImageView)this.findViewById(R.id.imageView1);
		DbC = DbController.getInstance(this, this);
		
		
		updateView();
		
	}
	protected void updateView(){
		
		if (bitmap!=null){
			imageView.setImageBitmap(bitmap);
		}
	}
 /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photos, menu);
		return true;
	}
	*/
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            bitmap = (Bitmap) data.getExtras().get("data");
            updateView();
        }  
	}
	// responds to button Go Back
	public void OnGoBack(View View)
    {
		if (imgPath!=null){
			Intent returnIntent = new Intent();
			returnIntent.putExtra("imgPath", imgPath);
			setResult(RESULT_OK,returnIntent);
			//Log.d("Received result", "yes");
			finish();
		}
		else{
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			//Log.d("Received result", "no");
			finish();
		}
    }
	// Weird bug here
	public void OnCapturePB(View View)
    {
	
		// responds to button Capture
		//if (SDCARD_INSTALLED){
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
		else{
			//If sd card not installed on vm, use BogoPicGen 
			progressDialog = ProgressDialog.show(TakePhotosActivity.this, "", "Making BogoPic...");

			new Thread() 
			{
				public void run() 
				{
					try{
						bitmap = BogoPicGen.generateBitmap(640, 480);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					try
					{
						sleep(1500);
						
					}catch (InterruptedException e)
					{
						Log.e("tag",e.getMessage());
					}
					// dismiss the progressdialog   
					progressDialog.dismiss();
				}
			}.start();
			if (bitmap==null){ Log.d("what", "what");};
			updateView();
		}
    }
	public void OnCapture(View View)
    {
	
		// responds to button Capture
		//if (SDCARD_INSTALLED){
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
		else{
			Toast.makeText(getApplicationContext(), "Making Bogopic...",
					Toast.LENGTH_LONG).show();
			bitmap = BogoPicGen.generateBitmap(640, 480);
			updateView();
		}
    }
	
	//With progress bar (spinner)
	public void OnSavePhoto (View View)
	{
		String timeStampId = String.valueOf(System.currentTimeMillis());

		// responds to button Save Photo
		if (bitmap!=null){
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				imgPath = Environment.getExternalStorageDirectory()+File.separator+timeStampId;
				file = new File(imgPath);
			}
			else {
				File dir = getDir(PICTURES_DIRECTORY, Context.MODE_PRIVATE);
				file = new File(dir, timeStampId);
			}
			
			progressDialog = ProgressDialog.show(TakePhotosActivity.this, "", "Saving...");
			Photo photo = new Photo(timeStampId, imgPath);
			DbC = DbController.getInstance(this, this);
			/* Attempt to save photo to device. */
			if (DbC.addPhotoToRecipe(photo, bitmap, uri)) {
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Image saved with success",
						Toast.LENGTH_LONG).show();
			} else {
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						"Error during image saving", Toast.LENGTH_LONG).show();
			}
		}
	}

	// no progress bar
//	public void OnSavePhotoNoProgBar (View View)
//	{
//		// responds to button Save Photo
//		if (bitmap!=null){
//
//			     try
//			     {
//			    	 //if (SDCARD_INSTALLED){
//			    	 if (Environment.MEDIA_MOUNTED.equals(state)) {
//			    		 imgPath = Environment.getExternalStorageDirectory()+File.separator+timeStampId;
//			    		 file = new File(imgPath);
//			    	 }
//			    	 else{
//			    		 File dir = getDir(PICTURES_DIRECTORY, Context.MODE_PRIVATE);
//			    		 file = new File(dir, timeStampId);
//			    	 } 		
//			    	 FileOutputStream outStream = new FileOutputStream(file);
//			    	 worked = bitmap.compress(Bitmap.CompressFormat.PNG, 30, outStream);
//			    	 outStream.flush();
//			    	 outStream.close();
//			    	 success = true;
//			    	 imgPath = file.getAbsolutePath();
//			     } catch (Exception e) {
//			    	 // TODO Auto-generated catch block
//			    	 e.printStackTrace();
//			     } 
//	
//			if (success && worked) {
//
//				Photo photo = new Photo(timeStampId, imgPath);
//				DbC.addPhotoToRecipe(photo, uri);
//				//Log.d("recipeuri", Long.toString(uri));
//				//Log.d("image path", imgPath);
//				Toast.makeText(getApplicationContext(), "Image saved with success",
//						Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(getApplicationContext(),
//						"Error during image saving", Toast.LENGTH_LONG).show();
//			}
//		}
//	}
	

	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		updateView();
		
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
}