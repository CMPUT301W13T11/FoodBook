package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;


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
	protected void updateView(){
		
		viewedRecipe = DbC.getUserRecipe(uri);
		recipeName.setText(viewedRecipe.getTitle());
		instructions.setText(viewedRecipe.getInstructions());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_recipe, menu);
		return true;
	}
	public void OnGotoMyRecipes(View View)
    {
		// responds to button Go Back to My Recipes
		 Intent intent = new Intent(View.getContext(), MyRecipes.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 EditRecipeActivity.this.finish();
    }
	
	public void OnEditPhotos (View View)
    {
		// responds to button OnPhotos
    	Intent intent = new Intent(this, EditPhotos.class);
    	intent.putExtra(EXTRA_URI, uri);
		startActivity(intent);
    }
	public void OnEditIngredients (View View)
    {
		// responds to button Ingredients
    	Intent intent = new Intent(this, EditIngredients.class);
		startActivity(intent);
    }
	public void OnSaveChanges (View View)
	{
		/* Currently we talk directly to the Db -- eventually, this will go through a controller. */
		DbManager db = DbManager.getInstance(this);
		
		/* Get the updated fields. */
		String newTitle = recipeName.getText().toString();
		String newInstructions = instructions.getText().toString();
		
		db.updateRecipeTitle(uri, "UserRecipes", newTitle);
		db.updateRecipeInstructions(uri, "UserRecipes", newInstructions);

	}
	public void OnDeleteRecipe (View View)
	{
		// responds to button Delete Recipe

		//first darken the screen

		//LayoutParams darkenParams = darkenScreen.getLayoutParams();
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

				Log.d("are we here", "are we here");
				Boolean success = DbC.deleteRecipe(viewedRecipe);
				//ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
				//LayoutParams darkenParams = darkenScreen.getLayoutParams();
				darkenParams.height = 0;
				darkenParams.width = 0;
				darkenScreen.setLayoutParams(darkenParams);
				popUp.dismiss();
				
				if (success) {
					Toast.makeText(getApplicationContext(), "Everything deleted with success",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Error during deleting", Toast.LENGTH_LONG).show();
				} 
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

		// TODO Auto-generated method stub
		this.updateView();
	
	}
		
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
