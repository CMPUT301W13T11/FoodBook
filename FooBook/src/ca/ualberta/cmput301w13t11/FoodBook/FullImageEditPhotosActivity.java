package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;

public class FullImageEditPhotosActivity extends Activity implements FView<DbManager>{

	static final String EXTRA_URI = "extra_uri";
	static final String EXTRA_IMG = "extra_img";
	private String imgPath = null;
	private Long uri;
	private ImageView imageView = null;
	private Bitmap bitmap = null;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	setContentView(R.layout.activity_full_image_edit_photos);
    	imageView = (ImageView) findViewById(R.id.imageView1);
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        imgPath = bundle.getString(EXTRA_IMG);
        uri = bundle.getLong(EXTRA_URI);
        bitmap= BitmapFactory.decodeFile(imgPath);
        this.updateView();
    }
    protected void updateView(){
    	
    	this.imageView.setImageBitmap(bitmap);
    	
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	  if (requestCode == 1) {

    	     if(resultCode == RESULT_OK){      
    	    	 this.bitmap= BitmapFactory.decodeFile(data.getStringExtra("imgPath"));
    	    	 this.updateView();
    	     }
    	     if (resultCode == RESULT_CANCELED) {    
    	         //Write your code on no result return 
    	     }
    	  }
    	}//onActivityResult

    public void OnTakePhoto(View view)
	{
		Intent intent = new Intent(this, TakePhotosActivity.class);
		intent.putExtra(EXTRA_URI, uri);
        startActivityForResult(intent, 1);
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
		Photo photo = new Photo(imgPath);
		DbC.deleteRecipePhoto(photo, uri);
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
