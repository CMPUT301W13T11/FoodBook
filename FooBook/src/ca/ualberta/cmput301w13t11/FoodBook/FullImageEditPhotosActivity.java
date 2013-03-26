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
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

public class FullImageEditPhotosActivity extends Activity implements FView<DbManager>{

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
	 * its results.  The process is started by calling "new SearchByKeywordsTask().execute(keyword)".
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
			progressDialog = ProgressDialog.show(FullImageEditPhotosActivity.this, "", "Uploading...");
		}
		@Override
		protected ReturnCode doInBackground(Photo... params) {
			Photo photo = params[0];
			ServerController SC=ServerController.getInstance(FullImageEditPhotosActivity.this);
			ReturnCode ret = SC.uploadPhotoToRecipe(photo, uri);
			return ret;
		}
		
		@Override
		protected void onPostExecute(ReturnCode ret)
		{
			progressDialog.dismiss();
			ServerClient sc = ServerClient.getInstance();
			if (ret == ReturnCode.SUCCESS) {
				Toast.makeText(getApplicationContext(), "Photo successfully uploaded!", Toast.LENGTH_LONG).show();

			}
			else if (ret == ReturnCode.NOT_FOUND) {
				Toast.makeText(getApplicationContext(), "Sorry, we couldn't find this recipe online. :( \n Have you uploaded it yet?", Toast.LENGTH_LONG).show();
			}
			else if (ret == ReturnCode.ERROR) {
				Toast.makeText(getApplicationContext(), "An error occurred.  No es bueno. :(", Toast.LENGTH_LONG).show();
			}
		}
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	setContentView(R.layout.activity_full_image_edit_photos);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	  if (requestCode == 1) {

    	     if(resultCode == RESULT_OK){      
    	    	 imgPath = data.getStringExtra("imgPath");
    	    	 this.updateView();
    	     }
    	     if (resultCode == RESULT_CANCELED) {    
    	         //Write your code on no result return 
    	     }
    	  }
    	}//onActivityResult

    public void OnUploadPhoto(View view)
	{
    	new UploadPhotoTask(uri).execute(new Photo(id, imgPath, bitmap));
	}

    public void OnGoBack(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 FullImageEditPhotosActivity.this.finish();
    }
    public void OnDeletePhoto(View View)
    {
		// responds to button Go Back
		DbController DbC = DbController.getInstance(this, this);
		Photo photo = new Photo(id, imgPath);
		
		Boolean success = DbC.deleteRecipePhoto(photo);
		if (success) {
			Toast.makeText(getApplicationContext(), "Image deleted with success",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(),
					"Error during image deleting", Toast.LENGTH_LONG).show();
		} 
		FullImageEditPhotosActivity.this.finish();
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
