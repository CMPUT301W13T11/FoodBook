package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.FModel;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ViewRecipeActivity extends Activity implements FView
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_recipe, menu);
		return true;
	}
	public void OnGotoMyRecipes(View View)
    {
		// responds to button Go Back
		 Intent intent = new Intent(View.getContext(), MyRecipes.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 ViewRecipeActivity.this.finish();
    }
	
	public void OnEditRecipe (View View)
    {
		// responds to button Edit Recipe
    	Intent intent = new Intent(this, EditRecipeActivity.class);
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
    	
    }

	@Override
	public void update(FModel model)
	{

		// TODO Auto-generated method stub
		
	}

}
