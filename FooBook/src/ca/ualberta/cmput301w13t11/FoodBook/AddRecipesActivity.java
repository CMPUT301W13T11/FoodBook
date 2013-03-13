package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class AddRecipesActivity extends Activity implements FView<DbManager>
{
//Test
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
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
	
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
