package ca.ualberta.cmput301w13t11.FoodBook;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
