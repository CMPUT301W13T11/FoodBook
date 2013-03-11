package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class EditIngredients extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_ingredients);
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

}
