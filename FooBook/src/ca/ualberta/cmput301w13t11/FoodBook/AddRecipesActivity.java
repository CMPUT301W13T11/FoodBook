package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	private long recipe_uri; /* timestamp the recipe on entry into the  */
	private Recipe mRecipe;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		
		/* Initialize the recipe on entry. */
		recipe_uri = System.currentTimeMillis();
		mRecipe = new Recipe(recipe_uri);
		
		setContentView(R.layout.activity_add_recipes);
	}

	public void OnGobacktoMyRecipes(View View)
    {
		// responds to button Go Back to My Recipes
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
		// responds to button Edit Photos
    	Intent intent = new Intent(this, EditPhotos.class);
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
			if(Title.isEmpty()||Instructions.isEmpty()||Author.isEmpty()){
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
			Recipe newRecipe=new Recipe(author, Title, Instructions);
			DbC.addRecipe(newRecipe);
			
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
