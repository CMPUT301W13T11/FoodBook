package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;

public class EditRecipeActivity extends Activity implements FView<DbManager>
{
	PopupWindow popUp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_recipe, menu);
		return true;
	}
	public void OnGotoMyRecipes(View View)
    {
		// responds to button Go Back to My Recipes
		 Intent intent = new Intent(View.getContext(), MyRecipes.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 EditRecipeActivity.this.finish();
    }
	
	public void OnPhotos (View View)
    {
		// responds to button OnPhotos
    	Intent intent = new Intent(this, EditPhotos.class);
		startActivity(intent);
    }
	public void OnIngredients (View View)
    {
		// responds to button Ingredients
    	Intent intent = new Intent(this, EditIngredients.class);
		startActivity(intent);
    }
	public void OnSaveChanges (View View)
    {
		// responds to button Save Changes
		
    }
	public void OnDeleteRecipe (View View)
    {
		LinearLayout layout = new LinearLayout(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		popUp = new PopupWindow(inflater.inflate(R.layout.popup, null, false),300,150, true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
		
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount=1.0f;
		this.getWindow().setAttributes(lp);
		
		Log.d("what", "what");
		//popUp.update(50, 50, 300, 80);
		
		

    }
	@Override
	public void update(DbManager db)
	{

		// TODO Auto-generated method stub
		
	}
	public void OnOK(View v){
		popUp.dismiss();
	}
	public void OnCancel(View v){
		popUp.dismiss();
	}

}
