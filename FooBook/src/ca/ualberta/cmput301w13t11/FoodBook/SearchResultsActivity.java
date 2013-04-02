package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;


/**
 * Displays what the server has found for search results
 * @author Thomas Cline, Marko Babic and Pablo Jaramillo
 *
 */

public class SearchResultsActivity extends Activity implements FView<DbManager>
{

	public static final String EXTRA_URI = "extra_uri";
	private ProgressDialog progressDialog;


	/**
	 * Performs a keyword search asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask().execute(keyword)".
	 * @author mbabic
	 */
	private class SearchByKeywordsTask extends AsyncTask<String, Void, ReturnCode>{
		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(SearchResultsActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("Searching...");		
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
			DbController dbc = DbController.getInstance(SearchResultsActivity.this, SearchResultsActivity.this);
			if (ret == ReturnCode.SUCCESS) {
				dbc.updateResultsDb();
			} else 
				displayToastByResult(ret);
		}
	}

	/**
	 * Performs a search by all ingredients asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask().execute(keyword)".
	 * @author mbabic
	 */
	private class SearchByAllIngredientsTask extends AsyncTask<ArrayList<Ingredient>, Void, ReturnCode>{		
		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(SearchResultsActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("Searching...");				
		}
		@Override
		protected ReturnCode doInBackground(ArrayList<Ingredient>... ingredients) {
			ArrayList<Ingredient> ingredientsList = ingredients[0];
			ServerController SC=ServerController.getInstance(SearchResultsActivity.this);
			ReturnCode ret = SC.searchByIngredients(ingredientsList);
			return ret;
		}
		
		@Override
		protected void onPostExecute(ReturnCode ret)
		{
			progressDialog.dismiss();
			DbController dbc = DbController.getInstance(SearchResultsActivity.this, SearchResultsActivity.this);
			if (ret == ReturnCode.SUCCESS) {
				dbc.updateResultsDb();
			} else 
				displayToastByResult(ret);
		}
	}

	/**
	 * Displays a Toast message depending on the results of the search.
	 * @param ret The ReturnCode returned by the search functionality.
	 */
	private void displayToastByResult(ReturnCode ret)
	{
		if (ret == ReturnCode.BUSY) {
			Toast.makeText(getApplicationContext(), 
					"The server isn't responding to us. :( Here are your old results though!", Toast.LENGTH_LONG).show();

		} else if (ret == ReturnCode.NO_RESULTS) {
			
			Toast.makeText(getApplicationContext(), "Your search returned no results.\n We kept your old ones though. :)", Toast.LENGTH_LONG).show();
		} else if (ret == ReturnCode.ERROR) {
			
			Toast.makeText(getApplicationContext(), "An error occurred.  Me so sorry. :(", Toast.LENGTH_LONG).show();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String searchType = "";
		
		if (extras != null) {
			searchType = extras.getString(FoodBookApplication.SEARCH_TYPE);
			if (searchType.equals(FoodBookApplication.KEYWORDS_SEARCH)) {
				new SearchByKeywordsTask().execute(extras.getString(SearchActivity.KEYWORD));
			}
			
			else if (searchType.equals(FoodBookApplication.ALL_INGREDIENTS_SEARCH)) {
				DbController dbc = DbController.getInstance(this, this);
				ArrayList<Ingredient> ingredients = dbc.getUserIngredients();
				new SearchByAllIngredientsTask().execute(ingredients);
			}
			
			else if (searchType.equals(FoodBookApplication.SUBSET_INGREDIENTS_SEARCH)) {
				displayToastByResult(FoodBookApplication.SEARCH_RESULT);
			}
		}
		updateList();
	}

	/**
	 * Responds to the "Main Menu" button. Returns the user to the main screen.
	 * @param The View that is calling the method
	 *
	 */
	
	public void OnGotoMainMenu(View View)
    {
		// responds to button Go Back to Main Menu
		 Intent intent = new Intent(View.getContext(), MainScreen.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 SearchResultsActivity.this.finish();
    }
	/**
	 * Responds to the "Search Again" button, and sends the user back to the SearchActivity
	 * @param The View that is calling the method
	 *
	 */
	public void OnSearchAgain(View View)
    {
		// responds to button Search Again
		 Intent intent = new Intent(View.getContext(), SearchActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 SearchResultsActivity.this.finish();
    }
	
	/**
	 * If changes are made to the content of the search results, ensures the 
	 * displayed list is kept up to date
	 *
	 */
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
			{	long recipe_uri = (((Recipe) parent.getItemAtPosition(position)).getUri());
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
	/**
	 *Deletes this view when finish(); is called
	 */
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
	/**
	 * @uml.property  name="viewSearchResultActivity"
	 * @uml.associationEnd  inverse="searchResultsActivity:ca.ualberta.cmput301w13t11.FoodBook.ViewSearchResultActivity"
	 * @uml.association  name="accessed from"
	 */
	private ViewSearchResultActivity viewSearchResultActivity;
	/**
	 * Getter of the property <tt>viewSearchResultActivity</tt>
	 * @return  Returns the viewSearchResultActivity.
	 * @uml.property  name="viewSearchResultActivity"
	 */
	public ViewSearchResultActivity getViewSearchResultActivity() {
		return viewSearchResultActivity;
	}


	/**
	 * Setter of the property <tt>viewSearchResultActivity</tt>
	 * @param viewSearchResultActivity  The viewSearchResultActivity to set.
	 * @uml.property  name="viewSearchResultActivity"
	 */
	public void setViewSearchResultActivity(
			ViewSearchResultActivity viewSearchResultActivity) {
				this.viewSearchResultActivity = viewSearchResultActivity;
			}
	/**
	 * @uml.property  name="serverController"
	 * @uml.associationEnd  inverse="searchResultsActivity:ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController"
	 * @uml.association  name="sends search requests to"
	 */
	private ServerController serverController;
	/**
	 * Getter of the property <tt>serverController</tt>
	 * @return  Returns the serverController.
	 * @uml.property  name="serverController"
	 */
	public ServerController getServerController() {
		return serverController;
	}


	/**
	 * Setter of the property <tt>serverController</tt>
	 * @param serverController  The serverController to set.
	 * @uml.property  name="serverController"
	 */
	public void setServerController(ServerController serverController) {
		this.serverController = serverController;
	}

}
