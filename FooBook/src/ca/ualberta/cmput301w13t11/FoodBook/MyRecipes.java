package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MyRecipes extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_recipes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_recipes, menu);
		return true;
	}
	
	/* Mark: every view must implement the 'update' method
	 * right now this isn't very MVC, but it might have to do for now
	 */
	public void update(FModel model) {
		ArrayList<Recipe> recipes = model.getUserRecipes();
	}

}
