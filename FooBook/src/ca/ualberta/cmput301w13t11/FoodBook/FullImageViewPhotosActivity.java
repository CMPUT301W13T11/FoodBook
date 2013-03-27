package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

public class FullImageViewPhotosActivity extends Activity implements FView<DbManager>{

	static final String EXTRA_IMG_ID = "extra_img_id";
	static final String EXTRA_IMG_PATH = "extra_img_path";
	static final String EXTRA_URI = "extra_uri";
	private String imgPath = null;

	private ImageView imageView = null;
	private Bitmap bitmap = null;

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

		imgPath = bundle.getString(EXTRA_IMG_PATH);

        
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
