package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.logging.Level;
import java.util.logging.Logger;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class AddRecipesActivity extends Activity implements FView<DbManager>
{

	PopupWindow popUp;
	static private final Logger logger = Logger.getLogger(AddRecipesActivity.class.getName());
	private long mRecipeUri; /* timestamp the recipe on entry into the  */
	public static String EXTRA_URI = "extra_uri"; /*passed with intent to newly launched "edit" acitivities */
	private Recipe mRecipe; /* the recipe being created.  Saved on entry, we use the setters to set the fields when "press save" is hit */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		/* Initialize the recipe on entry. */
		mRecipeUri = System.currentTimeMillis();
		mRecipe = new Recipe(mRecipeUri);
		mRecipe.setTitle(""); /* We set blank title */

		/* Immediately write the Recipe to the Db -- will be deleted if need be at the appropriate time. */
		DbController DbC = DbController.getInstance(this, this);
		DbC.addRecipe(mRecipe);


		setContentView(R.layout.activity_add_recipes);
	}

	public void OnGobacktoMyRecipes(View View)
	{
		// responds to button Go Back to My Recipes
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteRecipe(mRecipe);
		
		Intent intent = new Intent(View.getContext(), MyRecipes.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		AddRecipesActivity.this.finish();
	}

	public void OnEditIngredients (View View)
	{
		// responds to button Edit Ingredients
		Intent intent = new Intent(this, EditIngredients.class);
		startActivity(intent);
	}
	public void OnEditPhotos (View View)
	{
		/* Send the recipe URI to the new Activity */
//		EditText keywords = (EditText) findViewById(R.id.editText1);
//		Editable Keyword=keywords.getText();
//		String keyword=Keyword.toString();
//    	Intent intent = new Intent(this, SearchResultsActivity.class);
//    	intent.putExtra(KEYWORD, keyword);
		
		String uri = Long.toString(mRecipeUri);
		Intent intent = new Intent(this, EditPhotos.class);
		intent.putExtra(EXTRA_URI, uri);
		logger.log(Level.INFO, "Uri passed FROM AddRecipesActivity: " + uri);
		startActivity(intent);
	}
	public void OnSaveChanges (View View)
	{
		//Gets the user's recipes
		DbController DbC = DbController.getInstance(this, this);

		EditText editText = (EditText) findViewById(R.id.editText1);
		String Title= editText.getText().toString();

		editText = (EditText) findViewById(R.id.editText3);
		String Instructions = editText.getText().toString();

		editText = (EditText) findViewById(R.id.editText2);
		String Author = editText.getText().toString();

		//validate input
		if(Title.isEmpty()||Instructions.isEmpty()||Author.isEmpty()) {
			//first darken the screen
			ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
			LayoutParams darkenParams = darkenScreen.getLayoutParams();
			darkenParams.height = 1000;
			darkenParams.width = 1000;
			darkenScreen.setLayoutParams(darkenParams);

			//make the popup
			LinearLayout layout = new LinearLayout(this);
			LayoutInflater inflater = LayoutInflater.from(this);
			popUp = new PopupWindow(inflater.inflate(R.layout.popup_add_recipe_incomplete, null, false),300,100,true);
			popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
			return;
		}

		//Still have to do ingredients and photos
		User author=new User(Author);
		mRecipe.setAuthor(author);
		mRecipe.setTitle(Title);
		mRecipe.setInstructions(Instructions);

		DbC.updateRecipe(mRecipe);
		//Recipe newRecipe=new Recipe(author, Title, Instructions);
		//			db.updateRecipeTitle(uri, "UserRecipes", newTitle);
		//			db.updateRecipeInstructions(uri, "UserRecipes", newInstructions)			
		finish();
	}
	@Override
	public void update(DbManager db)
	{
	}
	public void OnOK(View v){
		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);

		popUp.dismiss();
	}
	public void onDestroy()
	{	super.onDestroy();
	DbController DbC = DbController.getInstance(this, this);
	DbC.deleteView(this);
	}

}
