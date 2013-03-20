package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ViewRecipeActivity extends Activity implements FView<DbManager>
{

	static final String EXTRA_URI = null;
	private PopupWindow popUp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		DbController DbC = DbController.getInstance(this, this);
		TextView recipeName = (TextView) findViewById(R.id.textView2);
		TextView instructions = (TextView) findViewById(R.id.textView5);
		
		Intent intent = getIntent();
		String URI = intent.getStringExtra(MyRecipes.EXTRA_URI);
		long uri=Long.parseLong(URI);
		ArrayList<Recipe> RecipeList= DbC.getUserRecipes();
		Recipe viewedRecipe = Recipe.generateTestRecipe();
		
		for(int index=0; index<RecipeList.size(); index++)
		{
			if(RecipeList.get(index).getUri()==uri)
					{
					viewedRecipe=RecipeList.get(index);
					index=RecipeList.size();
					
					}
		}
		
		
		recipeName.setText(viewedRecipe.getTitle());
		instructions.setText(viewedRecipe.getInstructions());
	}


	public void OnGotoMyRecipes(View View)
    {
		// responds to button Go Back
		 Intent intent = new Intent(View.getContext(), MyRecipes.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 ViewRecipeActivity.this.finish();
    }
	
	public void OnEditRecipe (View View)
    {	Intent intent1 = getIntent();
		String URI = intent1.getStringExtra(MyRecipes.EXTRA_URI);
		
		// responds to button Edit Recipe
    	Intent intent = new Intent(this, EditRecipeActivity.class);
    	intent.putExtra(EXTRA_URI, URI);
		startActivity(intent);
    }
	public void OnEditPhotos (View View)
    {
		// responds to button Edit Photos
    	Intent intent = new Intent(this, EditPhotos.class);
		startActivity(intent);
    }
	public void OnEmailRecipe (View View)
    {
		// responds to button Email Recipe
    	
    }
	public void OnPublishRecipe (View View)
    {
		// responds to button Publish Recipe
		DbController DbC = DbController.getInstance(this, this);
		ServerController SC=ServerController.getInstance(this);
		Intent intent = getIntent();
		String URI = intent.getStringExtra(MyRecipes.EXTRA_URI);
		long uri=Long.parseLong(URI);
		ArrayList<Recipe> RecipeList= DbC.getUserRecipes();
		
		for(int index=0; index<RecipeList.size(); index++)
		{
			if(RecipeList.get(index).getUri()==uri)
					{
					SC.uploadRecipe(RecipeList.get(index));
					index=RecipeList.size();
					
					}
		}
		
		//first darken the screen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 1000;
		darkenParams.width = 1000;
		darkenScreen.setLayoutParams(darkenParams);
		//make the popup
		LinearLayout layout = new LinearLayout(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		popUp = new PopupWindow(inflater.inflate(R.layout.popup_recipe_upload_success, null, false),300,130,true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
    	
    }
	public void OnOK(View v){
		popUp.dismiss();
		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
	}
	@Override
	public void update(DbManager db)
	{
	}

	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
}
