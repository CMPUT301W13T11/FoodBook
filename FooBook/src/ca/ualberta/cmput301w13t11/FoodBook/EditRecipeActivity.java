package ca.ualberta.cmput301w13t11.FoodBook;

<<<<<<< HEAD
import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
=======
>>>>>>> branch 'master' of https://github.com/CMPUT301W13T11/FoodBook.git
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
<<<<<<< HEAD
import android.widget.EditText;
import android.widget.TextView;
=======
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
>>>>>>> branch 'master' of https://github.com/CMPUT301W13T11/FoodBook.git

public class EditRecipeActivity extends Activity implements FView<DbManager>
{
	PopupWindow popUp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		DbController DbC = DbController.getInstance(this, this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
<<<<<<< HEAD
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
=======
		
>>>>>>> branch 'master' of https://github.com/CMPUT301W13T11/FoodBook.git
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
<<<<<<< HEAD
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
=======
		LinearLayout layout = new LinearLayout(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		popUp = new PopupWindow(inflater.inflate(R.layout.popup, null, false),300,150, true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
		
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount=1.0f;
		this.getWindow().setAttributes(lp);
		
		Log.d("what", "what");
		//popUp.update(50, 50, 300, 80);
		
		
>>>>>>> branch 'master' of https://github.com/CMPUT301W13T11/FoodBook.git

    }
	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		
	}
	public void OnOK(View v){
		popUp.dismiss();
	}
	public void OnCancel(View v){
		popUp.dismiss();
	}

}
