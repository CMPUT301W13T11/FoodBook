package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity that allows the user to view a recipe that was returned from a search result, and subsequently delete 
 * the recipe from the search results or download that recipe
 * @author Thomas Cline, Marko Babic, and Pablo Jaramillo
 *
 */

public class ViewSearchResultActivity extends Activity implements FView<DbManager>
{
	public static final String EXTRA_URI = "extra_uri";
	private long uri;
	private Recipe viewedRecipe;
	private TextView recipeName;
	private TextView instructions;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		DbController DbC = DbController.getInstance(this, this);
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_result);
		
		Intent intent = getIntent();
		String URI = intent.getStringExtra(EXTRA_URI);
		uri=Long.parseLong(URI);
		viewedRecipe=null;

		for(int index=0; index<DbC.getStoredRecipes().size(); index++)
		{
			if(DbC.getStoredRecipes().get(index).getUri()==uri)
					{
					viewedRecipe=DbC.getStoredRecipes().get(index);
					index=DbC.getStoredRecipes().size();
					
					}
		}
		if(viewedRecipe!=null){
			recipeName = (TextView) findViewById(R.id.textView2);
			recipeName.setText(viewedRecipe.getTitle());
			instructions = (TextView) findViewById(R.id.Instructions);
			instructions.setText(viewedRecipe.getInstructions());
			ListView listView = (ListView) findViewById(R.id.listView1);
				//Displays the user's recipes
				ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getStoredRecipeIngredients(uri));
				//Assigns the adapter
				listView.setAdapter(adapter);
				instructions.setMovementMethod(new ScrollingMovementMethod());	
		}
	}
	
	/**
	 * If a change is made to the currently viewed recipe, ensures that the displayed recipe is kept up to date
	 */

protected void updateView(){
		
		DbController DbC = DbController.getInstance(this, this);
		
		recipeName.setText(viewedRecipe.getTitle());
		instructions.setText(viewedRecipe.getInstructions());
		ListView listView = (ListView) findViewById(R.id.listView1);
		ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getStoredRecipeIngredients(uri));
		listView.setAdapter(adapter);
	}

/**
 * Responds to the "Go back" button, and sends the user back to the Search Results screen
 * @param The View that is calling the method
 *
 */

	public void OnGobacktoSearchResults(View View)
    {
		// responds to button Go Back to Search Results
		 Intent intent = new Intent(View.getContext(), SearchResultsActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 ViewSearchResultActivity.this.finish();
    }
	
	/**
	 * Responds to the "View Photos" button, and sends the user back to the view Photos Activity, allowing them to browse
	 * photos of the recipe
	 * @param The View that is calling the method
	 *
	 */

	public void OnViewPhotos (View View)
    {
		// responds to button View Photos
    	Intent intent = new Intent(this, ViewPhotosActivity.class);
    	intent.putExtra(EXTRA_URI, uri);
    	intent.putExtra(ViewPhotosActivity.CALLER, "ViewSearchResultActivity");
		startActivity(intent);
    }
	
	/**
	 * Responds to the "Download Recipe" button, and saves the currently viewed recipe into the users local
	 * "My Recipes" cache
	 * @param The View that is calling the method
	 *
	 */

	
	public void OnDownloadRecipe (View View)
    {
		DbController DbC = DbController.getInstance(this, this);
		
//		Intent intent = getIntent();
//		intent.getLongExtra(SearchResultsActivity.EXTRA_URI, 0);
//		Recipe viewedRecipe=null;
//		for(int index=0; index<DbC.getStoredRecipes().size(); index++)
//		{
//			if(DbC.getStoredRecipes().get(index).getUri()==uri)
//					{
//					viewedRecipe=DbC.getStoredRecipes().get(index);
//					index=DbC.getStoredRecipes().size();
//					
//					}
//		}
//		
		if (viewedRecipe != null)
			DbC.addRecipe(viewedRecipe);
		finish();
    }

	/**
	 * Responds to the "Delete result" button, which sends the user back to the search results screen
	 * and removes the current recipe from the listing
	 * @param The View that is calling the method
	 *
	 */
	public void OnDeleteResult(View View)
    {	
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteStoredRecipe(viewedRecipe);
		// responds to button Go Back to Search Results
		 Intent intent = new Intent(View.getContext(), SearchResultsActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 ViewSearchResultActivity.this.finish();
		 
    }
	
	
	@Override
	public void update(DbManager db)
	{
		
	}

	/**
	 *Deletes this view when finish(); is called
	 */
	
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
