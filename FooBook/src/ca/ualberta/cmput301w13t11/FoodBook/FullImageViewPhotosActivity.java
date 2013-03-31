package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

/**
 * This class is used to view enlarged pictures/images after clicking on them in the view gallery (activity ViewPhotosActivity).
 * Deletion or capturing is not allowed.
 * @author Marko Babic and Pablo Jaramillo
 *
 */
public class FullImageViewPhotosActivity extends Activity implements FView<DbManager>{

	//intent 'handshakes'
	static final String EXTRA_IMG_ID = "extra_img_id";		//image id, incoming
	static final String EXTRA_IMG_PATH = "extra_img_path";	//image path, incoming
	static final String EXTRA_URI = "extra_uri";				//recipe uri, incoming
	
	private String imgPath = null;				//image path
	@SuppressWarnings("all")
	private String id;
	@SuppressWarnings("all")
	private long uri;								//recipe uri, changed Long to long (? causing bug)
	private ImageView imageView = null;			//image container
	private Bitmap bitmap = null;					//bitmap being displayed

	/**
	 * Performs an photo upload operation asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask(uri of recipe).execute(photo to be uploaded)".
	 * @author mbabic
	 */
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	setContentView(R.layout.activity_full_image_view_photos);
    	imageView = (ImageView) findViewById(R.id.imageView1);
        super.onCreate(savedInstanceState);
        
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		id = bundle.getString(EXTRA_IMG_ID);
		imgPath = bundle.getString(EXTRA_IMG_PATH);
		uri = bundle.getLong(EXTRA_URI); 
        this.updateView();
    }
    
    /**
	 * Update method called by onCreate as well as the update method (called by the database manager).
	 * a bitmap is obtained from the image path (imgPath) and is placed as a large image in the current layout.
	 */
    protected void updateView(){
    	
    	bitmap= BitmapFactory.decodeFile(imgPath);
    	this.imageView.setImageBitmap(bitmap);
    	
    }
    
	@SuppressWarnings("all")
	private class UploadPhotoTask extends AsyncTask<Photo, Void, ReturnCode>{
		private ProgressDialog progressDialog;
		private long upload_uri;

		public UploadPhotoTask(long uri)
		{
			this.upload_uri = uri;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(FullImageViewPhotosActivity.this, "", "Uploading...");
		}

		@Override
		protected ReturnCode doInBackground(Photo... params) {
			try {
				Photo photo = params[0];
				ServerController SC=ServerController.getInstance(FullImageViewPhotosActivity.this);
				ReturnCode ret = SC.uploadPhotoToRecipe(photo, upload_uri);
				return ret;
			} catch (Exception e) {
				return ReturnCode.ERROR;
			}
		}

		@Override
		protected void onPostExecute(ReturnCode ret)
		{
			progressDialog.dismiss();
			if (ret == ReturnCode.SUCCESS) {
				Toast.makeText(getApplicationContext(), "Photo successfully uploaded!", Toast.LENGTH_LONG).show();

			} else if (ret == ReturnCode.BUSY) {
				
				Toast.makeText(getApplicationContext(), "It looks like the server is busy or not responding. Aieee.", Toast.LENGTH_LONG).show();
				
			} else if (ret == ReturnCode.NOT_FOUND) {
				
				Toast.makeText(getApplicationContext(), "Sorry, we couldn't find this recipe online. :( \n Have you uploaded it yet?", Toast.LENGTH_LONG).show();
				
			} else if (ret == ReturnCode.ERROR) {
				
				Toast.makeText(getApplicationContext(), "An error occurred.  Oy gevalt. \n :(", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * This method takes the user back to the previous activity.
	 * @param View - The view calling the method.
	 */
    public void OnGoBack(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 FullImageViewPhotosActivity.this.finish();
    }
   
    /**
     * Action taken on UploadPhoto button press -- launches the UploadPhotoTask() to upload
     * the currently viewed photo.
     */
    public void OnUploadPhoto()
    {
		//new UploadPhotoTask(uri).execute(new Photo(id, imgPath, bitmap));
    }
	/**
	 *  Called by the database manager to update views, part of MVC
	 */
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
