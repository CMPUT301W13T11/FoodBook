package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ViewSearchResultActivity extends Activity implements FView<DbManager>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_result);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_search_result, menu);
		return true;
	}
	
	public void OnGobacktoSearchResults(View View)
    {
		// responds to button Go Back to Search Results
		 Intent intent = new Intent(View.getContext(), SearchResultsActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 ViewSearchResultActivity.this.finish();
    }
	
	public void OnViewPhotos (View View)
    {
		// responds to button View Photos
    	Intent intent = new Intent(this, EditIngredients.class);
		startActivity(intent);
    }
	public void OnAddPhotos (View View)
    {
		// responds to button Add Photos
    	Intent intent = new Intent(this, EditPhotos.class);
		startActivity(intent);
    }
	public void OnDownloadRecipe (View View)
    {
		// responds to button Download Recipe
		
		/*Intent intent=
		DbController DbC = DbController.getInstance(this, this);
		ServerController SC=ServerController.getInstance();
		//Recipe recipe =SC.getSearchResult()
		Recipe recipe = null;
		DbC.addRecipe(recipe);
		finish();
*/
    	
    }

	@Override
	public void update(DbManager db)
	{}

}
