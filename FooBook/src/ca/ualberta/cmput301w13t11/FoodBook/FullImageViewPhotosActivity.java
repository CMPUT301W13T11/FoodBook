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
 * 
 * @author Marko Babic and Pablo Jaramillo
 *
 */
public class FullImageViewPhotosActivity extends Activity implements FView<DbManager>{

	static final String EXTRA_IMG_ID = "extra_img_id";
	static final String EXTRA_IMG_PATH = "extra_img_path";
	static final String EXTRA_URI = "extra_uri";
	private String imgPath = null;
	private String id;
	private Long uri;
	private ImageView imageView = null;
	private Bitmap bitmap = null;

	/**
	 * Performs an photo upload operation asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask(uri of recipe).execute(photo to be uploaded)".
	 * @author mbabic
	 */
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
    protected void updateView(){
    	
    	bitmap= BitmapFactory.decodeFile(imgPath);
    	this.imageView.setImageBitmap(bitmap);
    	
    }
    

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
