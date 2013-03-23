package ca.ualberta.cmput301w13t11.FoodBook;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
	static final String EXTRA_IMG = "extra_img";
	private long uri;
	private ArrayList<Photo> photos;
	//private static Uri[] mUrls = null;
	//private static String[] strUrls = null;
	//private String[] mNames = null;
	//private String[] picNames = null;
	private GridView gridview = null;
	//private Cursor cc = null;
	//private Button btnMoreInfo = null;
	private ProgressDialog myProgressDialog = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photos);
		
			
		Intent intent = getIntent();
		//String URI = intent.getStringExtra(ViewRecipeActivity.EXTRA_URI);
		//long uri=Long.parseLong(URI);
		uri = intent.getLongExtra(EXTRA_URI, 0);
		this.updateView();
	}
	protected void updateView(){
		DbController DbC = DbController.getInstance(this, this);
		photos = DbC.getRecipePhotos(uri);
		
		//btnMoreInfo = (Button) findViewById(R.id.btnMoreInfo);
	    // It have to be matched with the directory in SDCard
	    //cc = this.getContentResolver().query(
	            //MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
	            //null);

	    // File[] files=f.listFiles();
	    if (!photos.isEmpty()) {
	    	/*
	        myProgressDialog = new ProgressDialog(EditPhotos.this);
	        myProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        myProgressDialog.setMessage(getResources().getString(R.string.pls_wait_txt));
	        //myProgressDialog.setIcon(R.drawable.blind);
	        myProgressDialog.show();
	        
	        new Thread() {
	            public void run() {
	                try {
	                	
	                }
	                    cc.moveToFirst();
	                    mUrls = new Uri[cc.getCount()];
	                    strUrls = new String[cc.getCount()];
	                    mNames = new String[cc.getCount()];
	                    for (int i = 0; i < cc.getCount(); i++) {
	                        cc.moveToPosition(i);
	                        mUrls[i] = Uri.parse(cc.getString(1));
	                        strUrls[i] = cc.getString(1);
	                        mNames[i] = cc.getString(3);
	                        //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
	                    }
	                    
	                	
	                } catch (Exception e) {
	                }
	                myProgressDialog.dismiss();
	            }
	        }.start();
	        */
	    gridview = (GridView) findViewById(R.id.gridView1);
	    gridview.setAdapter(new ImageAdapter(this));
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
	            Intent i = new Intent(EditPhotos.this, FullImageEditPhotosActivity.class);
	            //Log.e("intent : ", ""+position);
	            //i.putExtra(EXTRA_URI, uri);
	            Bundle bundle = new Bundle();
	            bundle.putString(EXTRA_IMG, photos.get(position).getName());
	            bundle.putLong(EXTRA_URI, uri);

	            i.putExtras(bundle);

	            startActivity(i);
	        }
	    });
	
	    }
	    /*
	    btnMoreInfo.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            Intent i = new Intent(GalleryPage.this, ChildLogin.class);
	            startActivity(i);
	        }
	    });
	    */
	}


	/**
	 * This class loads the image gallery in grid view.
	 *
	 */
	
	public class ImageAdapter extends BaseAdapter {
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
	        v = vi.inflate(R.layout.galchild, null);

	        try {

	            ImageView imageView = (ImageView) v.findViewById(R.id.imageView1);
	            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	            // imageView.setPadding(8, 8, 8, 8);
	            //Bitmap bmp = decodeURI(mUrls[position].getPath());

	            Bitmap bmp = decodeURI(Environment.getExternalStorageDirectory()+File.separator+photos.get(position).getName());
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
	/*
	@Override
	protected void onStart() {
	    // TODO Auto-generated method stub
	    super.onStart();
	    FlurryAgent.onStartSession(this, "LPJJF9WYENDWYXXTEUDM");
	}
	*/
	// @Override
	// protected void onStop() {
	// TODO Auto-generated method stub
	// super.onStop();
	// FlurryAgent.onEndSession(this);

	// }

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
