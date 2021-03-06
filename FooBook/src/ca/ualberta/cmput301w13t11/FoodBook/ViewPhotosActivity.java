package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
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
 * This class only shows the recipe's pictures in a gallery. Capturing or deleting is not allowed.
 * Images are enlarged by clicking on them (starts FullImageViewPhotosActivity.java). 
 * Depending on who the caller is, the upload button is enabled/disabled on the full image screeen.
 * 
 * @author jaramill
 * 
 */
public class ViewPhotosActivity extends Activity implements FView<DbManager>
{
	//intent 'handshakes'
	static final String EXTRA_URI = "extra_uri";				//recipe uri, incoming
	static final String EXTRA_IMG_ID = "extra_img_id";		//image id (timestamp), incoming
	static final String EXTRA_IMG_PATH = "extra_img_path";	//image path, incoming
	static final String EXTRA_NO_UPLOAD ="extra_no_upload";	//another way to pass the caller to FullImage...
		
	public static String CALLER = "caller";			
	private boolean queryResultsDb = false;			//true if viewing photos from online (searched) recipe
	private long uri = 0; 							// photos (no bitmap data in them)	
	private ArrayList<Photo> photos;

	private GridView gridview = null;					//3 x ? gridview for photo icons

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_photos);
		String caller = "";
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			setQueryResultsDb(extras);
		}
		uri = intent.getLongExtra(EXTRA_URI, 0);
		Log.d("ViewPhotosActivity in onCreate()", "uri passed to activity = " + uri);
		this.updateView();

	}
	
	/**
	 * Sets the queryResultsDb flag based on the "caller" extra passed to the method.
	 * @param extras The extras Bundle passed to the Activity on creation.
	 */
	public void setQueryResultsDb(Bundle extras)
	{
		String caller = "";
		caller = extras.getString(CALLER);
		if (caller.equals("ViewSearchResultActivity")) {
			queryResultsDb = true;
		}
	}
	
	/**
	 * This method updates the gallery images. 
	 * It is called on creation of the activity and by the update method (called by the database manager).
	 */
	protected void updateView(){

		DbController DbC = DbController.getInstance(this, this);
		if (queryResultsDb)
			photos = DbC.getStoredRecipePhotos(uri);
		else
			photos = DbC.getRecipePhotos(uri);
		if (photos.isEmpty()) {
			Log.d("ViewPhotosActivity updateView():", "photos is empty");
		}
		
		if (!photos.isEmpty()) {
			gridview = (GridView) findViewById(R.id.gridView1);
			gridview.setAdapter(new ImageAdapter(this));    
			gridview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent i = new Intent(ViewPhotosActivity.this, FullImageViewPhotosActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(EXTRA_IMG_ID, photos.get(position).getId());
					bundle.putString(EXTRA_IMG_PATH, photos.get(position).getPath());
					bundle.putLong(EXTRA_URI, uri);
					//Tells FullImageViewPhotosActivity to disable the upload button if the caller is ViewSearchResultsActivity 
					bundle.putBoolean(EXTRA_NO_UPLOAD, queryResultsDb);
					//Log.d("recipe", Long.toString(uri));
					//Log.d("filename", photos.get(position).getName());
					i.putExtras(bundle);
					startActivity(i);
				}
			});
		} 
	}


	/**
	 * This class helps produce a gallery (grid view) of small images which can be clicked and enlarged.
	 * The BaseAdapter class is extended accordingly.
	 */
	public class ImageAdapter extends BaseAdapter {
		@SuppressWarnings("all")

		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

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
				Bitmap bmp = BitmapFactory.decodeFile(photos.get(position).getPath());
				imageView.setImageBitmap(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return v;
		}
	}
	
	/**
	 *  Returns the user to the previous screen, called on pressing the Go Back button.
	 * @param View - The view calling this method.
	 */
	public void OnGoBack(View View)
	{
		// responds to button Go Back
		ViewPhotosActivity.this.finish();
	}
	
	/**
	 *  Called by the database manager to update views, part of MVC
	 */
	@Override
	public void update(DbManager db)
	{
		// TODO Auto-generated method stub
		//create a new adapter
		this.updateView();

	}
	public void onDestroy()
	{	super.onDestroy();
	DbController DbC = DbController.getInstance(this, this);
	DbC.deleteView(this);
	}


}
