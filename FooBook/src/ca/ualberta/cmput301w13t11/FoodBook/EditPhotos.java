package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

public class EditPhotos extends Activity implements FView<DbManager>
{
	private static final int CAMERA_REQUEST = 1337;
	static final String EXTRA_URI = "extra_uri";
	private long uri;
	private Gallery picGallery;
	//adapter for gallery view
	private PicAdapter imgAdapt;
	private DbManager ourDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		ourDb = DbManager.getInstance(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photos);
		picGallery = (Gallery) findViewById(R.id.pic_gallery_edit);
		this.update(ourDb);
		
	}
	public class PicAdapter extends BaseAdapter {

		//use the default gallery background image
		int defaultItemBackground;
		//gallery context
		private Context galleryContext;
		//array to store bitmaps to display
		private Bitmap[] imageBitmaps;
		//placeholder bitmap for empty spaces in gallery
		Bitmap placeholder;
		public PicAdapter(Context c) {
		    //instantiate context
		    galleryContext = c;
		    //create bitmap array
		    imageBitmaps  = new Bitmap[10];
		    //decode the placeholder image
		    placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		    //more processing
		  //set placeholder as all thumbnail images in the gallery initially
			for(int i=0; i<imageBitmaps.length; i++)
			    imageBitmaps[i]=placeholder;
			//get the styling attributes - use default Andorid system resources
			//TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);
			//get the background resource
			//defaultItemBackground = styleAttrs.getResourceId(
			    //R.styleable.PicGallery_android_galleryItemBackground, 0);
			//recycle attributes
			//styleAttrs.recycle();
		}
		public int getCount() {
		    return imageBitmaps.length;
		}
		//return item at specified position
		public Object getItem(int position) {
		    return position;
		}
		//return item ID at specified position
		public long getItemId(int position) {
		    return position;
		}
		//get view specifies layout and display options for each thumbnail in the gallery
		public View getView(int position, View convertView, ViewGroup parent) {
		    //create the view
		    ImageView imageView = new ImageView(galleryContext);
		    //specify the bitmap at this position in the array
		    imageView.setImageBitmap(imageBitmaps[position]);
		    //set layout options
		    imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
		    //scale type within view area
		    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		    //set default gallery item background
		    imageView.setBackgroundResource(defaultItemBackground);
		    //return the view
		    return imageView;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_photos, menu);
		return true;
	}
	public void OnGoBack(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 EditPhotos.this.finish();
    }
	public void OnTakePhoto(View View)
    {
		// responds to button TakePhoto
    	Intent intent = new Intent(this, TakePhotosActivity.class);
		startActivity(intent);
    }
	public void OnDeletePhoto (View View)
    {
		// responds to button Delete Photo
    	
    }

	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		//create a new adapter
		imgAdapt = new PicAdapter(this);
		//set the gallery adapter
		picGallery.setAdapter(imgAdapt);
		
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
