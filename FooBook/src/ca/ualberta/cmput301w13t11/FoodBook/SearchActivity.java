package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

/**
 * Allows the user to search the online database for 
 * @author Thomas Cline, Marko Babic and Pablo Jaramillo
 *
 */

public class SearchActivity extends Activity implements FView<DbManager>
{

	public static String KEYWORD = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
	}

	/**
	 * Responds to the "Main Menu" button and returns the user to the main menu
	 * @param The View that is calling the method
	 *
	 */
	
	public void OnGotoMainMenu(View View)
    {
		// responds to button Main Menu
		 Intent intent = new Intent(View.getContext(), MainScreen.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 SearchActivity.this.finish();
    }
	/*
	 * Sends the user input keywords to the server controller
	 */
	/**
	 * Responds to the "Search" button, sends the contents of the edit text to the server controller
	 * @param The View that is calling the method
	 *
	 */
	
	public void OnSearch (View View)
    {
		EditText keywords = (EditText) findViewById(R.id.editText1);
		Editable Keyword=keywords.getText();
		String keyword=Keyword.toString();
    	Intent intent = new Intent(this, SearchResultsActivity.class);
    	intent.putExtra(KEYWORD, keyword);
    	intent.putExtra(FoodBookApplication.SEARCH_TYPE, FoodBookApplication.KEYWORDS_SEARCH);
		startActivity(intent);
    }

	@Override
	public void update(DbManager db)
	{
		
	}
	/**
	 * Responds to the "What can I make?" button and searches recipes that can be made with the user's current
	 * ingredients in "My Ingredients"
	 * @param The View that is calling the method
	 *
	 */
		
	public void OnIngredientSearch (View View)
    {
		Intent intent = new Intent(this, SearchResultsActivity.class);
		intent.putExtra(FoodBookApplication.SEARCH_TYPE, FoodBookApplication.ALL_INGREDIENTS_SEARCH);
		startActivity(intent);
    }

}
