package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

public class SearchResultsActivity extends Activity implements FView<DbManager>
{

	//protected static final String EXTRA_URI = null;
	public static final String EXTRA_URI = "extra_uri";


	/**
	 * Performs a keyword search asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask().execute(keyword)".
	 * @author mbabic
	 */
	private class SearchByKeywordsTask extends AsyncTask<String, Void, ReturnCode>{
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(SearchResultsActivity.this, "", "Searching...");
		}
		@Override
		protected ReturnCode doInBackground(String... keywords) {
			String searchString = keywords[0];
			ServerController SC=ServerController.getInstance(SearchResultsActivity.this);
			ReturnCode ret = SC.searchByKeywords(searchString);
			return ret;
		}
		
		@Override
		protected void onPostExecute(ReturnCode ret)
		{
			progressDialog.dismiss();
			ServerClient sc = ServerClient.getInstance();
			if (ret == ReturnCode.SUCCESS) {
				sc.writeResultsToDb();
			}
			else if (ret == ReturnCode.NO_RESULTS) {
				Toast.makeText(getApplicationContext(), "Your search returned no results.\n We kept your old ones though. :)", Toast.LENGTH_LONG).show();
			}
			else if (ret == ReturnCode.ERROR) {
				Toast.makeText(getApplicationContext(), "An error occurred.  Me so sorry. :(", Toast.LENGTH_LONG).show();
			}
		}
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		Intent intent = getIntent();
		new SearchByKeywordsTask().execute(intent.getStringExtra(SearchActivity.KEYWORD));
		updateList();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}*/
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
		DbController DbC = DbController.getInstance(this, this);
		
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getStoredRecipes());
		//Assigns the adapter
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{	long recipe_uri = ((Recipe) parent.getItemAtPosition(position)).getUri();
				Intent intent = new Intent(SearchResultsActivity.this, ViewSearchResultActivity.class);
				// Minor adjustment, passing uris as longs -Pablo Readjusted back
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