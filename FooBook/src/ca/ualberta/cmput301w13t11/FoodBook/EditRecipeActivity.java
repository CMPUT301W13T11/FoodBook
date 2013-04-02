package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;

/**
 * Used to Edit a recipe that is locally saved in "My Recipes"
 * @author Thomas Cline and Pablo Jaramillo
 *
 */

public class EditRecipeActivity extends Activity implements FView<DbManager>
{
	static final String EXTRA_URI = "extra_uri";
	PopupWindow popUp;
	public long uri;
	private Recipe viewedRecipe;
	private EditText recipeName;
	private EditText instructions;
	private DbController DbC;
	private ImageView darkenScreen;
	private LayoutParams darkenParams;
	private boolean deleted = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		DbC = DbController.getInstance(this, this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
		//To darken the screen, just moved them here
		darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		darkenParams = darkenScreen.getLayoutParams();
		
		recipeName = (EditText) findViewById(R.id.editText1);
		instructions = (EditText) findViewById(R.id.editText3);
		
		Intent intent = getIntent();
		
		//String URI = intent.getStringExtra(ViewRecipeActivity.EXTRA_URI);
		//long uri=Long.parseLong(URI);
		uri = intent.getLongExtra(EXTRA_URI, 0);
		updateView();
	
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		updateView();
	}
	
	/**
	 * If the screen is returned to from an activity, ensures the activity displays the proper information
	 *
	 */
	
	protected void updateView(){
		
		viewedRecipe = DbC.getUserRecipe(uri);
		if(viewedRecipe == null){
			finish();
			return;
		}
		recipeName.setText(viewedRecipe.getTitle());
		instructions.setText(viewedRecipe.getInstructions());
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, DbC.getRecipeIngredients(uri));
		//ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getRecipeIngredients(uri));
		listView.setAdapter(adapter);
	}	
	
	/**
	 * Responds to the button to the "Go back" button and returns the user to "My Recipes"
	 * @param The View that is calling the method
	 *
	 */

	public void OnGotoMyRecipes(View View)
    {
		// responds to button Go Back to My Recipes
		 EditRecipeActivity.this.finish();
    }
	/**
	 * Responds to the button to the "Edit Photos" button and starts the EditPhoto activity
	 * @param The View that is calling the method
	 *
	 */
	
	public void OnEditPhotos (View View)
    {
		// responds to button OnPhotos
    	Intent intent = new Intent(this, EditPhotos.class);
		String uri_str = Long.toString(uri);
    	intent.putExtra("extra_uri", uri_str);
		startActivity(intent);
    }
	
	/**
	 * Responds to the button to the "Edit Ingredients" button and starts the Edit Ingredients
	 * activity sending the URI of the recipe.
	 * @param The View that is calling the method
	 *
	 */
	public void OnEditIngredients (View View)
    {
		// responds to button Ingredients
    	Intent intent = new Intent(this, EditIngredients.class);
		String uri_str = Long.toString(uri);
    	intent.putExtra(EXTRA_URI, uri_str);
		startActivity(intent);
    }
	
	/**
	 * Responds to the button to the "Edit Recipe" button and starts the Edit Recipe Activity
	 * activity sending the URI of the recipe.
	 * @param The View that is calling the method
	 *
	 */
	public void OnSaveChanges (View View)
	{
		/* Defensive call to getInstance() */
		DbC = DbController.getInstance(this, this);
		
		/* Get the updated fields. */
		String newTitle = recipeName.getText().toString();
		String newInstructions = instructions.getText().toString();
		viewedRecipe.setTitle(newTitle);
		viewedRecipe.setInstructions(newInstructions);
		
		/* Put the results in the Db */
		DbC.updateRecipe(viewedRecipe);
		
		/**
		 * Responds to the button to the "Delete" button and deletes the current recipe
		 * from the My Recipes list, returning the user to "My Recipes"
		 * @param The View that is calling the method
		 *
		 */
	}
	public void OnDeleteRecipe (View View)
	{
		// responds to button Delete Recipe

		//first darken the screen
		darkenParams.height = 1000;
		darkenParams.width = 1000;
		darkenScreen.setLayoutParams(darkenParams);
		//make the popup


		LinearLayout layout = new LinearLayout(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View popupLayout = inflater.inflate(R.layout.popup_delete_recipe, null, false);
		Button ok_button = (Button) popupLayout.findViewById(R.id.ok_button);
		OnClickListener ok_button_click_listener = new OnClickListener(){
			
			public void onClick(View View){
			
				//delete the recipe
				DbController DbC = DbController.getInstance(EditRecipeActivity.this, EditRecipeActivity.this);
				//ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
				//LayoutParams darkenParams = darkenScreen.getLayoutParams();
				darkenParams.height = 0;
				darkenParams.width = 0;
				darkenScreen.setLayoutParams(darkenParams);
				popUp.dismiss();				
				//DbC.deleteView(EditRecipeActivity.this);
				deleted = true;
				DbC.deleteRecipe(viewedRecipe);
				finish();
			}
		};
		
		ok_button.setOnClickListener(ok_button_click_listener);
		Button cancel_button = (Button) popupLayout.findViewById(R.id.cancel_button);
		OnClickListener cancel_button_click_listener = new OnClickListener(){
			public void onClick(View View){

				//remove the darkScreen
				//ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
				//LayoutParams darkenParams = darkenScreen.getLayoutParams();
				darkenParams.height = 0;
				darkenParams.width = 0;
				darkenScreen.setLayoutParams(darkenParams);
				popUp.dismiss();
			}
		};
		cancel_button.setOnClickListener(cancel_button_click_listener);

		popUp = new PopupWindow(popupLayout, 380,200,true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);

		//Log.d("what's going on", Long.toString(viewedRecipe.getUri()));

    }
	@Override
	public void update(DbManager db)
	{
		updateView();
		if (!deleted) {
			this.updateView();
		}
	
	}
		
	/**
	 * When the activity ends, ensures the view is destroyed so will not get an error
	 * from displaying a deleted recipe
	 * @param The View that is calling the method
	 *
	 */
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}
}
