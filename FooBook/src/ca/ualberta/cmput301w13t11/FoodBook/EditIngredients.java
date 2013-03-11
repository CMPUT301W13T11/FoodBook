package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditIngredients extends Activity implements FView<DbManager>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_ingredients);
		ListView listView = (ListView) findViewById(R.id.mylist);

		//ArrayList to hold the ingredients
		ArrayList<String> IngredientNames = new ArrayList<String>();
		
		/*
		 *Functions to fill the array go here 
		 * 
		 */
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, IngredientNames);
		 //Assigns the adapter
			listView.setAdapter(adapter);
		
	}

	public void OnGotoMainMenu(View View)
    {
		// responds to button Main Menu
		 Intent intent = new Intent(View.getContext(), MyRecipes.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 EditIngredients.this.finish();
    }
	
	public void OnAddIngredient(View View)
    {
		 //responds to button Add ingredient
    }
	public void OnModifyIngredient(View View)
    {
		//responds to button Modify ingredient
    }

	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		
	}

}
