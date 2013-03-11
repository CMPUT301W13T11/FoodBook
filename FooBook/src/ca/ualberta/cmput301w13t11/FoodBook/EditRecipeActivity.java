package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditRecipeActivity extends Activity implements FView<DbManager>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		DbController DbC = DbController.getInstance(this, this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
		EditText recipeName = (EditText) findViewById(R.id.editText1);
		EditText instructions = (EditText) findViewById(R.id.editText3);
	
		
		Intent intent = getIntent();
		String URI = intent.getStringExtra(ViewRecipeActivity.EXTRA_URI);
		long uri=Long.parseLong(URI);
		
		ArrayList<Recipe> RecipeList= DbC.getUserRecipes();
		Recipe viewedRecipe = Recipe.generateTestRecipe();
		
		for(int index=0; index<RecipeList.size(); index++)
		{
			if(RecipeList.get(index).getUri()==uri)
					{
					viewedRecipe=RecipeList.get(index);
					index=RecipeList.size();
					
					}
		}
		
		
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
	
	public void OnPhotos (View View)
    {
		// responds to button OnPhotos
    	Intent intent = new Intent(this, EditPhotos.class);
		startActivity(intent);
    }
	public void OnIngredients (View View)
    {
		// responds to button Ingredients
    	Intent intent = new Intent(this, EditIngredients.class);
		startActivity(intent);
    }
	public void OnSaveChanges (View View)
    {
		// responds to button Save Changes
    	
    }
	public void OnDeleteRecipe (View View)
    {
		// responds to button Delete Recipe
		
		DbController DbC = DbController.getInstance(this, this);		
		Intent intent = getIntent();
		String URI = intent.getStringExtra(ViewRecipeActivity.EXTRA_URI);
		long uri=Long.parseLong(URI);
		
		ArrayList<Recipe> RecipeList= DbC.getUserRecipes();		
		for(int index=0; index<RecipeList.size(); index++)
		{
			if(RecipeList.get(index).getUri()==uri)
					{
					DbC.deleteRecipe(RecipeList.get(index));
					index=RecipeList.size();
					
					}
		}
		finish();
		Intent intentGo = new Intent(this, MyRecipes.class);
		startActivity(intentGo);
    }

	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		
	}

}
