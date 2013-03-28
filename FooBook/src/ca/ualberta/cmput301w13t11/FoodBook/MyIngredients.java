package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

public class MyIngredients extends Activity implements FView<DbManager>
{
	public static String NO_RESULTS = "no_results";
	public static String SEARCH_TIMEOUT = "timeout";
	
	private PopupWindow popUp;
	private ImageView darkenScreen;
	private LayoutParams darkenParams;
	private View popUpView;
	static private final Logger logger = Logger.getLogger(MyIngredients.class.getName());

	
	
	/**
	 * Performs a search by all ingredients asynchronously (ie. not on the UI thread) and reports
	 * its results.  The process is started by calling "new SearchByKeywordsTask().execute(keyword)".
	 * @author mbabic
	 */
	private class SearchByIngredientSubsetTask extends AsyncTask<ArrayList<Ingredient>, Void, ReturnCode>{ 
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(MyIngredients.this, "", "Searching...");
		}
		@Override
		protected ReturnCode doInBackground(ArrayList<Ingredient>... ingredients) {
			ArrayList<Ingredient> ingredientsList = ingredients[0];
			ServerController SC=ServerController.getInstance(MyIngredients.this);
			ReturnCode ret = SC.searchByIngredients(ingredientsList);
			return ret;
		}
		
		@Override
		protected void onPostExecute(ReturnCode ret)
		{
			progressDialog.dismiss();
			
			
			ServerController sc = ServerController.getInstance(MyIngredients.this);
			if (ret == ReturnCode.SUCCESS) {
				sc.updateResultsDb();
			}
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_ingredients);

		darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		darkenParams = darkenScreen.getLayoutParams();
		updateIngredients();
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

		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 1000;
		darkenParams.width = 1000;
		darkenScreen.setLayoutParams(darkenParams);
		//make the popup
		LinearLayout layout = new LinearLayout(MyIngredients.this);
		LayoutInflater inflater = LayoutInflater.from(MyIngredients.this);
		popUpView = inflater.inflate(R.layout.popup_add_ingredient, null, false);
		popUp = new PopupWindow(popUpView,300,500,true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}

	public void OnRemoveIngredient(View View)
	{
		DbController DbC = DbController.getInstance(this, this);
		ListView listView = (ListView) findViewById(R.id.mylist);
		SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
		
		if (checkedPositions != null) {
			int length = checkedPositions.size();
			for (int i = 0; i < length; i++) {
				DbC.deleteIngredient( (Ingredient) listView.getItemAtPosition(i));
			}
		}
		updateIngredients();
	}


	public void updateIngredients()
	{
		//Gets the user's recipes
		final DbController DbC = DbController.getInstance(this, this);
		ListView listView = (ListView) findViewById(R.id.mylist);
		//Recipe testRecipe=Recipe.generateTestRecipe();
		//ArrayList <Recipe> test = new ArrayList<Recipe>();
		//test.add(testRecipe);
		//Displays the user's recipes
		ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, DbC.getUserIngredients());
		//Assigns the adapter
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			//Long click to edit an ingredient
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{	
				//DbC.deleteIngredient(DbC.getUserIngredients().get(position));
				ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
				LayoutParams darkenParams = darkenScreen.getLayoutParams();
				darkenParams.height = 1000;
				darkenParams.width = 1000;
				darkenScreen.setLayoutParams(darkenParams);
				//make the popup
				LinearLayout layout = new LinearLayout(MyIngredients.this);
				LayoutInflater inflater = LayoutInflater.from(MyIngredients.this);
				popUpView = inflater.inflate(R.layout.popup_edit_ingredient, null, false);
				popUp = new PopupWindow(popUpView,300,500,true);
				popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);


				EditText editText = (EditText) popUpView.findViewById(R.id.editIngredientType);
				editText.setText(DbC.getUserIngredients().get(position).getName());


				editText = (EditText) popUpView.findViewById(R.id.editIngredientUnit);
				editText.setText(DbC.getUserIngredients().get(position).getUnit());


				editText = (EditText) popUpView.findViewById(R.id.editIngredientAmount);
				editText.setText(String.valueOf(DbC.getUserIngredients().get(position).getQuantity()));


				return false;
			}});

	}


	public void update(DbManager db)
	{
		updateIngredients();
	}
	public void onDestroy()
	{	super.onDestroy();
	DbController DbC = DbController.getInstance(this, this);
	DbC.deleteView(this);
	}


	public void OnCancel(View v){
		popUp.dismiss();

		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
	}

	public void OnOK(View v){
		EditText editText = (EditText) popUpView.findViewById(R.id.editIngredientType);
		String type=editText.getText().toString();

		editText = (EditText) popUpView.findViewById(R.id.editIngredientUnit);
		String unit=editText.getText().toString();


		editText = (EditText) popUpView.findViewById(R.id.editIngredientAmount);
		float amount =Float.parseFloat(editText.getText().toString());

		popUp.dismiss();

		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
		Ingredient ingredient = new Ingredient(type, unit, amount);
		DbController DbC = DbController.getInstance(this, this);
		DbC.addIngredient(ingredient);
		updateIngredients();
	}
	
	public void OnIngredientSearch(View View)
	{
	
	}

}
