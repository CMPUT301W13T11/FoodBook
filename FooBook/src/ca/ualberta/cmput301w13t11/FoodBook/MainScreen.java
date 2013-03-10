package ca.ualberta.cmput301w13t11.FoodBook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_screen, menu);
        return true;
    }
    
    /** Called when the user clicks the My Recipes button */
	public void GoToMyRecipes(View view) {
		Intent intent = new Intent(this, MyRecipes.class);
		startActivity(intent);
	}
	
	/** Called when the user clicks the My Ingredients button */
	public void GoToMyIngredients(View view) {
		Intent intent = new Intent(this, MyIngredients.class);
		startActivity(intent);
	}
	/** Called when the user clicks the Search button */
	public void GoToSearch(View view) {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}
}
