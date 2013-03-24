package ca.ualberta.cmput301w13t11.FoodBook;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

public class FullImageViewPhotosActivity extends Activity implements FView<DbManager>{


	private String imgPath;
	private ImageView imageView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	imageView = (ImageView) findViewById(R.id.imageView1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view_photos);
        Intent intent = getIntent();
        imgPath = intent.getDataString();
 
        BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+File.separator+imgPath);
        
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
		
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
}
