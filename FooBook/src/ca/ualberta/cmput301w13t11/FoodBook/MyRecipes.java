package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyRecipes extends Activity implements FView<DbManager>{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_recipes);
		updateList();
		
	}

	public void updateList()
    {
		//Gets the user's recipes
	DbController DbC = DbController.getInstance(this);
	ListView listView = (ListView) findViewById(R.id.mylist);
		
		Recipe testRecipe=Recipe.generateTestRecipe();
		ArrayList <Recipe> test = new ArrayList<Recipe>();
		test.add(testRecipe);
		 //DbC.getUserRecipes();
		//Displays the user's recipes
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, android.R.id.text1, test);
		//Assigns the adapter
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{	long recipe_uri = ((Recipe) parent.getItemAtPosition(position)).getUri();
				Intent intent = new Intent(MyRecipes.this, ViewRecipeActivity.class);
				intent.putExtra("recipe_uri", recipe_uri);
				startActivity(intent);
			}});
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

	@Override
	public void update(DbManager db)
	{
		updateList();
	}
	
}
