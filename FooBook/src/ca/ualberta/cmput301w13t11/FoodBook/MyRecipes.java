package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
/*
import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.FModel;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
*/
public class MyRecipes extends Activity
{
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photos);
    }


	/*@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_recipes);
		// Mark: somewhere here you need to call updateContent to get data
		//updateContent();
	}	
/*	// Mark: every view must implement the 'update' method
	public void update(FModel model) {
		updateContent();
	}
	
	public void updateContent() {
		DbController controller = RecipeApplication.getDbController();
		ArrayList<Recipe> recipes = controller.getUserRecipes();
		/* now you guys have to figure out how to put this array of
		 * recipes into the UI
		 
	}
	*/
	
	/** Called when the user clicks the Back button */
	public void GoToMainMenu(View view) {
		Intent intent = new Intent(this, MainScreen.class);
		startActivity(intent);
	}
	/** Called when the user clicks the Add new recipe button */
	public void GoToAddRecipe(View view) {
		Intent intent = new Intent(this, AddRecipesActivity.class);
		startActivity(intent);
	}

}
