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

public class EditPhotos extends Activity implements FView<DbManager>
{

	static final String EXTRA_URI = "extra_uri";
	static final String EXTRA_IMG_ID = "extra_img_id";
	static final String EXTRA_IMG_PATH = "extra_img_path";
	private long uri;
	static private final Logger logger = Logger.getLogger(EditPhotos.class.getName());

	private ArrayList<Photo> photos;
	
	private GridView gridview = null;


	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photos);
		String extraUri = "";
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
				extraUri = extras.getString(EXTRA_URI);
		}
		uri = Long.parseLong(extraUri);
		logger.log(Level.INFO, "Uri passed to EditPhotos: " + uri);

		this.updateView();
	}
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
					Bundle bundle = new Bundle();
					bundle.putString(EXTRA_IMG_ID, photos.get(position).getId());
					bundle.putString(EXTRA_IMG_PATH, photos.get(position).getPath());
					bundle.putLong(EXTRA_URI, uri);
					//Log.d("recipe", Long.toString(uri));
					//Log.d("filename", photos.get(position).getName());
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
	 * This class loads the image gallery in grid view.
	 *
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
	        v = vi.inflate(R.layout.galchild, null);

	        try {

	            ImageView imageView = (ImageView) v.findViewById(R.id.imageView1);
	            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	            imageView.setPadding(8, 8, 8, 8);
	            //Bitmap bmp = decodeURI(mUrls[position].getPath());
	            Bitmap bmp = decodeURI(photos.get(position).getPath());
	            //BitmapFactory.decodeFile(mUrls[position].getPath());
	            imageView.setImageBitmap(bmp);
	            //bmp.
	            //TextView txtName = (TextView) v.findViewById(R.id.TextView01);
	            //txtName.setText(mNames[position]);
	        } catch (Exception e) {

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
	 * This method is to scale down the image 
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
	public void OnDeletePhoto(View view)
	{
		
		//EditPhotos.this.finish();
	}
	

	public void OnTakePhoto(View view)
	{
		Intent intent = new Intent(this, TakePhotosActivity.class);
		intent.putExtra(EXTRA_URI, uri);
        startActivity(intent);
	}
	public void OnGoBack(View View)
    {
		// responds to button Go Back
		// not sure if this is enough -Pablo 
		 EditPhotos.this.finish();
    }
	

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
