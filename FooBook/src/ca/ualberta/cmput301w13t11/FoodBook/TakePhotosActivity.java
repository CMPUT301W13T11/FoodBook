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
import android.os.AsyncTask;
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
 * This class allows the user to take (capture) pictures and save them onto a recipe.
 * When an SD card is not installed, BogoPicGen is called to generate 640x480 images.
 * BogoPicGen images are stored in main memory in directory 'Pictures'
 * @author jaramill
 *
 */
public class TakePhotosActivity extends Activity implements FView<DbManager>
{
	
	private static final int CAMERA_REQUEST = 1337;
	static final String EXTRA_URI = "extra_uri";
	private long uri;
	private ImageView imageView;
	static private final Logger logger = Logger.getLogger(TakePhotosActivity.class.getName());

	protected Bitmap bitmap = null;
	private String imgPath = null;
	@SuppressWarnings("all")
	private File file = null;
	private ProgressDialog progressDialog;
	boolean success = false;
	boolean worked = false;
	@SuppressWarnings("all")
	private FileOutputStream outStream;
	private DbController DbC; 
	
	
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
	public void OnCaptureProgressBar(View View)
    {
		// responds to button Capture
		//if (SDCARD_INSTALLED){
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
		else{
			//If sd card not installed on vm, use BogoPicGen 
			//progressDialog = ProgressDialog.show(TakePhotosActivity.this, "", "Making BogoPic...");

			new DownloadImageTask().execute();
			progressDialog.dismiss();
		}
    }

	private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
		/** The system calls this to perform work in a worker thread and
		 * delivers it the parameters given to AsyncTask.execute() */
		protected Bitmap doInBackground(Void...voids) {
			return BogoPicGen.generateBitmap(640, 480);
		}
		

		/** The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground() */
		protected void onPostExecute(Bitmap result) {
			//xif (bitmap==null){ Log.d("what", "what");};
			imageView.setImageBitmap(bitmap);    	
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
				imgPath = file.getAbsolutePath();
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

	@Override
	public void update(DbManager db)
	{
		updateView();	
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
	/**
	 * @uml.property  name="editPhotos"
	 * @uml.associationEnd  inverse="takePhotosActivity:ca.ualberta.cmput301w13t11.FoodBook.EditPhotos"
	 * @uml.association  name="accessed from"
	 */
	private EditPhotos editPhotos;


	/**
	 * Getter of the property <tt>editPhotos</tt>
	 * @return  Returns the editPhotos.
	 * @uml.property  name="editPhotos"
	 */
	public EditPhotos getEditPhotos() {
		return editPhotos;
	}
	/**
	 * Setter of the property <tt>editPhotos</tt>
	 * @param editPhotos  The editPhotos to set.
	 * @uml.property  name="editPhotos"
	 */
	public void setEditPhotos(EditPhotos editPhotos) {
		this.editPhotos = editPhotos;
	}
}