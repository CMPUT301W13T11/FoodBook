package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyRecipes extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		/*THIS IS THE SECTION TO LOOK AT*/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_recipes);

		DbController DataTest = DbController.getInstance(this);
		DataTest.getUserRecipes();
		
		ListView listView = (ListView) findViewById(R.id.mylist);
		
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DataTest.getUserRecipes());
		 //Assigns the adapter
			listView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_recipes, menu);
		return true;
	}
	public void OnGotoMainMenu(View View)
    {
		// responds to button Main Menu
		 Intent intent = new Intent(View.getContext(), MainScreen.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 MyRecipes.this.finish();
    }
	
	public void OnAddRecipe (View View)
    {
		// responds to button Add Recipe
    	Intent intent = new Intent(this, AddRecipesActivity.class);
		startActivity(intent);
    }

}
