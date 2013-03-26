package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyIngredients extends Activity implements FView<DbManager>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_ingredients);
	}

	
	public void OnGotoMainMenu(View View)
    {
		// responds to button Main Menu
		 Intent intent = new Intent(View.getContext(), MainScreen.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 MyIngredients.this.finish();
    }
	
	public void OnAddIngredient(View View)
    {
		 //responds to button Add ingredient
    }
	public void OnModifyIngredient(View View)
    {
		//responds to button Modify ingredient
    }
	public void OnRemoveIngredient(View View)
    {
		//responds to button Modify ingredient
    }


	public void updateIngredients()
	{
		//Gets the user's recipes
		DbController DbC = DbController.getInstance(this, this);
		ListView listView = (ListView) findViewById(R.id.mylist);
			//Recipe testRecipe=Recipe.generateTestRecipe();
			//ArrayList <Recipe> test = new ArrayList<Recipe>();
			//test.add(testRecipe);

			/*//Displays the user's recipes
			ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getUserIngredients);
			//Assigns the adapter
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{	long recipe_uri = ((Recipe) parent.getItemAtPosition(position)).getUri();
					Intent intent = new Intent(MyRecipes.this, ViewRecipeActivity.class);
					String testString=Long.toString(recipe_uri);
					intent.putExtra(EXTRA_URI, testString);
					startActivity(intent);
				}});*/
	}
	
	public void update(DbManager db)
	{
	}
	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
