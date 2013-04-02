package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;

/**
 * This class shows the recipe's pictures in a gallery and allows capturing of new pictures as well as deleting of stored pictures 
 * (after pictures have been enlarged in new activity). 
 * @author jaramill
 *
 */

public class EditPhotos extends Activity implements FView<DbManager>
{
	//intent 'handshakes'
	static final String EXTRA_URI = "extra_uri";				//recipe uri, incoming, outgoing 
	static final String EXTRA_IMG_ID = "extra_img_id";		//image id, outgoing
	static final String EXTRA_IMG_PATH = "extra_img_path";	//image path, outgoing
	
	private long uri = 0;					//recipe's uri
	static private final Logger logger = Logger.getLogger(EditPhotos.class.getName());

	private ArrayList<Photo> photos;		// photos (no bitmap data in them)	
	
	private GridView gridview = null;		//3 x ? gridview for photo icons

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photos);
		String extraUri = "";
		Intent intent = getIntent();
		// Bundle contains the recipe's uri
		Bundle extras = intent.getExtras();
		if (extras != null) {
				extraUri = extras.getString(EXTRA_URI);
		}
		uri = Long.parseLong(extraUri);
		logger.log(Level.INFO, "Uri passed to EditPhotos: " + uri);

		this.updateView();
	}
	/**
	 * This method updates the gallery images. 
	 * It is called on creation of the activity and by the update method (called by the database manager).
	 */
	protected void updateView(){	

		DbController DbC = DbController.getInstance(this, this);
		photos = DbC.getRecipePhotos(uri);
		if (!photos.isEmpty()) {

			gridview = (GridView) findViewById(R.id.gridView1);
			gridview.setAdapter(new ImageAdapter());

			gridview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent i = new Intent(EditPhotos.this, FullImageEditPhotosActivity.class);
					//wrap recipe uri, image id (timestamp), and path in a bundle
					Bundle bundle = new Bundle();
					bundle.putString(EXTRA_IMG_ID, photos.get(position).getId());
					bundle.putString(EXTRA_IMG_PATH, photos.get(position).getPath());
					bundle.putLong(EXTRA_URI, uri);
					i.putExtras(bundle);
					startActivity(i);
				}
			});
		}
		else{
			setContentView(R.layout.activity_edit_photos);
		}
	}

	/**
	 * This class helps produce a gallery (grid view) of small images which can be clicked and enlarged.
	 * The BaseAdapter class is extended accordingly.
	 */

	public class ImageAdapter extends BaseAdapter {
		/*  private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }
		 */
		public int getCount() {
	        return photos.size();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        //galchild -gallery child-is layout with a a 100x100 dp image view
	        v = vi.inflate(R.layout.galchild, null);
	        try {
	            ImageView imageView = (ImageView) v.findViewById(R.id.imageView1);
	            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	            imageView.setPadding(8, 8, 8, 8);
	            Bitmap bmp = decodeURI(photos.get(position).getPath());
	            imageView.setImageBitmap(bmp);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return v;
	    }
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    updateView();
	}

	/**
	 *  This method retrieves a bitmap image  stored in a compressed format.
	 * @param filePath- complete path where the actual compressed image (jpg, pgn, etc) is stored 
	 * @return a possibly scaled down bitmap image 
	 */
	public Bitmap decodeURI(String filePath){

	    Options options = new Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Only scale if we need to 
	    // (16384 buffer for img processing)
	    Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
	    if(options.outHeight * options.outWidth * 2 >= 16384){
	        // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
	        double sampleSize = scaleByHeight
	            ? options.outHeight / 100
	            : options.outWidth / 100;
	        options.inSampleSize = 
	            (int)Math.pow(2d, Math.floor(
	            Math.log(sampleSize)/Math.log(2d)));
	    }

	    // Do the actual decoding
	    options.inJustDecodeBounds = false;
	    options.inTempStorage = new byte[512];  
	    Bitmap output = BitmapFactory.decodeFile(filePath, options);

	    return output;
	}
	/**
	 * This method does not work here. The delete button has been grayed out in this view.
	 * Delete is allowed when the image is clicked on and enlarged.
	 * See FullImageEditPhotosActivity.java. 
	 * @param view - The view calling the method.
	 */
	public void OnDeletePhoto(View view)
	{
		//EditPhotos.this.finish();
	}
	/**
	 * This method is called by the Capture (Photo) button. 
	 * It starts a new activity in which pictures can be taken.  
	 * @param view - The view calling this method.
	 */

	public void OnTakePhoto(View view)
	{
		Intent intent = new Intent(this, TakePhotosActivity.class);
		//Send recipe uri in intent.
		intent.putExtra(EXTRA_URI, uri);
        startActivity(intent);
	}
	/**
	 *  Returns the user to the previous screen, called on pressing the Go Back button.
	 * @param View - The view calling this method.
	 */
	public void OnGoBack(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 EditPhotos.this.finish();
    }
	
	/**
	 *  Called by the database manager to update views, part of MVC
	 */
	@Override
	public void update(DbManager db)
	{
		//create a new adapter
		this.updateView();
		

	}
	public void onDestroy()
	{	super.onDestroy();
	DbController DbC = DbController.getInstance(this, this);
	DbC.deleteView(this);
	}
}
