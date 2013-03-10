package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MyIngredients extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_ingredients);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_ingredients, menu);
		return true;
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

}
