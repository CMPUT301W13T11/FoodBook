package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.tasks.SearchByKeywordsTask;

public class SearchResultsActivity extends Activity implements FView<DbManager>
{

	protected static final String EXTRA_URI = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		Intent intent = getIntent();
		new SearchByKeywordsTask().execute(intent.getStringExtra(SearchActivity.KEYWORD));
		updateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}
	public void OnGotoMainMenu(View View)
    {
		// responds to button Go Back to Main Menu
		 Intent intent = new Intent(View.getContext(), MainScreen.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 SearchResultsActivity.this.finish();
    }
	public void OnSearchAgain(View View)
    {
		// responds to button Search Again
		 Intent intent = new Intent(View.getContext(), SearchActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 SearchResultsActivity.this.finish();
    }
	public void updateList()
	{
		ListView listView = (ListView) findViewById(R.id.mylist);
		/*ServerController SC=ServerController.getInstance(this);
		//SC.notify()*/
		//DbManager DbM=DbManager.getInstance();
		//DbM.getStoredRecipes();

		Recipe testRecipe=Recipe.generateTestRecipe();
		ArrayList <Recipe> test = new ArrayList<Recipe>();
		
		DbController DbC = DbController.getInstance(this, this);
		
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getStoredRecipes());
		//Assigns the adapter
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{	long recipe_uri = ((Recipe) parent.getItemAtPosition(position)).getUri();
				Intent intent = new Intent(SearchResultsActivity.this, ViewSearchResultActivity.class);
				String URI=Long.toString(recipe_uri);
				intent.putExtra(EXTRA_URI, URI);
				startActivity(intent);
			}});                                                                                 
	}
	
	@Override
	public void update(DbManager db)
	{
		updateList();
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}