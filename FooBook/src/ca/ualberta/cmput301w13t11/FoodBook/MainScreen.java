package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

public class MainScreen extends Activity implements FView<DbManager>{

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);	
    }
    
    /** Called when the user clicks the My Recipes button */
	public void OnMyRecipes(View view) {
		Intent intent = new Intent(this, MyRecipes.class);
		startActivity(intent);
	}
	
	/** Called when the user clicks the My Ingredients button */
	public void OnMyIngredients(View view) {
		Intent intent = new Intent(this, MyIngredients.class);
		startActivity(intent);
	}
	/** Called when the user clicks the Search button */
	public void OnSearch(View view) {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}

	@Override
	public void update(DbManager db)
	{
	}
}
