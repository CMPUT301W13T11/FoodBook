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

public class FullImageEditPhotosActivity extends Activity implements FView<DbManager>{

	static final String EXTRA_IMG_ID = "extra_img_id";
	static final String EXTRA_IMG_PATH = "extra_img_path";
	static final String EXTRA_URI = "extra_uri";
	private String imgPath = null;
	private String id;
	private Long uri;
	private ImageView imageView = null;
	private Bitmap bitmap = null;
	private PopupWindow popUp;


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
			progressDialog = ProgressDialog.show(FullImageEditPhotosActivity.this, "", "Uploading...");
		}
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
		//Options options = new Options();
		//options.inJustDecodeBounds = true;
		//Log.d("retpath", imgPath);
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

		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams =darkenScreen.getLayoutParams();

		// responds to button Delete Recipe

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

		//Log.d("what's going on", Long.toString(viewedRecipe.getUri()));


		// responds to button Go Back

	}
	public void OnOK(View View){

		//delete the recipe

		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
		popUp.dismiss();

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


	public void OnCancel(View View){

		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
		popUp.dismiss();
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
