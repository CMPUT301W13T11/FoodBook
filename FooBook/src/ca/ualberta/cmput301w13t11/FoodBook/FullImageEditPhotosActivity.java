package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

/**
 * This class is used to view enlarged pictures/images after clicking on them in the edit gallery (activity EditPhotos).
 *Images can be deleted or uploaded to the recipe server.
 * @author Pablo Jaramillo and Marko Babic
 * 
 */
public class FullImageEditPhotosActivity extends Activity implements FView<DbManager>{

	//intent 'handshakes'
	static final String EXTRA_IMG_ID = "extra_img_id";		//image id (timestamp), incoming
	static final String EXTRA_IMG_PATH = "extra_img_path";	//image path, incoming
	static final String EXTRA_URI = "extra_uri";				//recipe uri, incoming, outgoing to 
	
	private String imgPath = null;		//image path
	private String id = null;				//image id (timestamp)
	private long uri = 0;					//recipe uri, changed Long to long (? causing bug)
	private ImageView imageView = null;	//image container
	private Bitmap bitmap = null;			//bitmap being displayed
	private PopupWindow popUp;

	//
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
	/**
	 * Update method called by onCreate as well as the update method (called by the database manager).
	 * a bitmap is obtained from the image path (imgPath) and is placed as a large image in the current layout.
	 */
	protected void updateView(){
		
		bitmap= BitmapFactory.decodeFile(imgPath);
		this.imageView.setImageBitmap(bitmap);

	}

	/**
	 * Performs an photo upload operation asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask(uri of recipe).execute(photo to be uploaded)".
	 * @author mbabic
	 */
	private class UploadPhotoTask extends AsyncTask<Photo, Void, ReturnCode>{
		private ProgressDialog progressDialog;
		@SuppressWarnings("all")
		private long upload_uri;

		public UploadPhotoTask(long uri)
		{
			this.upload_uri = uri;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(FullImageEditPhotosActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("Uploading...");				}
		@Override
		protected ReturnCode doInBackground(Photo... params) {
			try {
				Photo photo = params[0];
				ServerController SC=ServerController.getInstance(FullImageEditPhotosActivity.this);
				ReturnCode ret = SC.uploadPhotoToRecipe(photo, uri);
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
	 * This method was used for debugging but is no longer neeeded.
	 */
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

	/**
	 * This method loads the current photo onto the server.
	 * @param view - The view which called this method.
	 */
	public void OnUploadPhoto(View view)
	{
		new UploadPhotoTask(uri).execute(new Photo(id, imgPath, bitmap));
	}
	
	/**
	 * This method takes the user back to the previous activity.
	 * @param View - The view calling the method.
	 */
	public void OnGoBack(View View)
	{
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		FullImageEditPhotosActivity.this.finish();
	}
	/**
	 * This method is called in response to hitting the delete (photo) button.
	 * It generates a popup for confirmation.
	 * @param View - The view calling the method. 
	 */
	public void OnDeletePhoto(View View)
	{
		// responds to button Delete Recipe
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams =darkenScreen.getLayoutParams();

		//first darken the screen
		//LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 1000;
		darkenParams.width = 1000;
		darkenScreen.setLayoutParams(darkenParams);
		//make the popup
		LinearLayout layout = new LinearLayout(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View popupLayout = inflater.inflate(R.layout.popup_delete_photo, null, false);
		popUp = new PopupWindow(popupLayout, 380,200,true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);

	}
	/**
	 * This method deletes the photo viewed from the recipe. 
	 * Called by the image popup OK button.
	 * A message is displayed depending on whether the delete was successful.
	 * @param View - The view calling the method. 
	 */
	public void OnOK(View View){
		//remove the dark screen and popup
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
		popUp.dismiss();
		//ready controller, wrap id and image path in photo object
		DbController DbC = DbController.getInstance(this, this);
		Photo photo = new Photo(id, imgPath);
		// call controller's delete Photo method 
		Boolean success = DbC.deleteRecipePhoto(photo);
		if (success) {
			Toast.makeText(getApplicationContext(), "Image deleted with success",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(),
					"Error during image deleting", Toast.LENGTH_LONG).show();
		}
		//exit
		FullImageEditPhotosActivity.this.finish();
	}
	
	/**
	 * Removes the popup, picture not deleted.
	 * @param View - The view calling the method.
	 */
	public void OnCancel(View View){

		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
		popUp.dismiss();
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
