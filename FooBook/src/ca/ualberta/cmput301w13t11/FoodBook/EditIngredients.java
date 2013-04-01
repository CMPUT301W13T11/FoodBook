package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
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
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;

/**
 * Used to change or add ingredients in a recipe
 * @author Thomas Cline and Pablo Jaramillo
 *
 */

public class EditIngredients extends Activity implements FView<DbManager>
{
	public static String NO_RESULTS = "no_results";
	public static String SEARCH_TIMEOUT = "timeout";

	private PopupWindow popUp;
	private PopupWindow popUpIncomplete;
	private ImageView darkenScreen;
	private View popUpView;
	static private final Logger logger = Logger.getLogger(EditIngredients.class.getName());
	private int pos;
	private boolean update=false;
	private Ingredient changedIngred;
	
	/* Flag used to decide if we should autosave the changes for the user or not. */
	private boolean cancelled = false;

	static final String EXTRA_URI = "extra_uri";
	private long uri;
	private ArrayList<Ingredient> RecipeIngredients;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_ingredients);

		Intent intent = getIntent();
		String uri_str = intent.getStringExtra(EXTRA_URI);
		uri = Long.parseLong(uri_str);
		DbController DbC = DbController.getInstance(this, this);
		logger.log(Level.INFO, "URI passed to EditIngredients: " + uri);
		RecipeIngredients=DbC.getRecipeIngredients(uri);
		darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		darkenScreen.getLayoutParams();
		updateIngredients();
	}
	/**
	 * Responds to the button to the "Main Menu" button and returns the user to the main menu
	 * @param The View that is calling the method
	 *
	 */

	public void OnGotoMainMenu(View View)
	{
		// responds to button Main Menu
		cancelled = true;
		Intent intent = new Intent(View.getContext(), MainScreen.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		EditIngredients.this.finish();
	}
	
	/**
	 * Responds to the "Add ingredient" button which prompts the user to add an ingredients in a pop-up 
	 * @param The View that is calling the method
	 *
	 */

	public void OnAddIngredient(View View)
	{
		//responds to button Add ingredient

		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 1000;
		darkenParams.width = 1000;
		darkenScreen.setLayoutParams(darkenParams);
		//make the popup
		LinearLayout layout = new LinearLayout(EditIngredients.this);
		LayoutInflater inflater = LayoutInflater.from(EditIngredients.this);
		popUpView = inflater.inflate(R.layout.popup_add_ingredient, null, false);
		popUp = new PopupWindow(popUpView,300,500,true);
		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}
	/**
	 * Removes all the ingredients that have been checked by the user
	 *  button which prompts the user to add an ingredients in a pop-up 
	 * @param The View that is calling the method
	 *
	 */
	public void OnRemoveIngredient(View View)
	{	ListView listView = (ListView) findViewById(R.id.mylist);
		SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
		int index =0;
		if (checkedPositions != null) {
			for (int i = 0; index < RecipeIngredients.size(); i++) {
				if(checkedPositions.get(i))
				{
				RecipeIngredients.remove(index);
				}
				else index++;
			}
		}
		updateIngredients();
	}

	/**
	 * populates the list with the ingredients from the recipe given through the URI
	 *
	 */

	public void updateIngredients()
	{
		DbController.getInstance(this, this);
		ListView listView = (ListView) findViewById(R.id.mylist);
		//Displays the user's recipes
		ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, RecipeIngredients);
		//Assigns the adapter
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			//Long click to edit an ingredient
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{	
				
				ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
				LayoutParams darkenParams = darkenScreen.getLayoutParams();
				darkenParams.height = 1000;
				darkenParams.width = 1000;
				darkenScreen.setLayoutParams(darkenParams);
				//make the popup
				LinearLayout layout = new LinearLayout(EditIngredients.this);
				LayoutInflater inflater = LayoutInflater.from(EditIngredients.this);
				popUpView = inflater.inflate(R.layout.popup_edit_ingredient, null, false);
				popUp = new PopupWindow(popUpView,300,500,true);
				popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);


				EditText editText = (EditText) popUpView.findViewById(R.id.editIngredientType);
				editText.setText(RecipeIngredients.get(position).getName());


				editText = (EditText) popUpView.findViewById(R.id.editIngredientUnit);
				editText.setText(RecipeIngredients.get(position).getUnit());


				editText = (EditText) popUpView.findViewById(R.id.editIngredientAmount);
				editText.setText(String.valueOf(RecipeIngredients.get(position).getQuantity()));
				pos=position;
				update=true;
				return false;
			}});

	}


	/**
	 * Called from the database manager, calls upDateIngredients method
	 */
	public void update(DbManager db)
	{
		updateIngredients();
	}
	public void onDestroy()
	{	
		super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}


	/**
	 * Responds to the "Cancel" button on the pop-up, closing the pop-up without altering anything 
	 * @param The View that is calling the method
	 *
	 */
	public void OnCancel(View v){
		popUp.dismiss();

		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
	}

	//called by the second popup
	/**
	 * Responds to the "Ok" button in the incomplete popup
	 * @param The View that is calling the method
	 *
	 */
	public void OnOKIncomplete(View v){
		popUpIncomplete.dismiss();
	}
	/**
	 * Responds to the "Ok" button in the pop-up, saving the changes the ingredients 
	 * @param The View that is calling the method
	 *
	 */
	public void OnOK(View v){
		
		EditText editText = (EditText) popUpView.findViewById(R.id.editIngredientType);
		String type=editText.getText().toString();

		editText = (EditText) popUpView.findViewById(R.id.editIngredientUnit);
		String unit=editText.getText().toString();


		editText = (EditText) popUpView.findViewById(R.id.editIngredientAmount);
		String quantity = editText.getText().toString();
		
		if( (quantity.trim().equals("")) || (unit.trim().equals("")) || (type.trim().equals("")) ){
			//make the popup prompting user to enter in all fields
			LinearLayout layout = new LinearLayout(EditIngredients.this);
			LayoutInflater inflater = LayoutInflater.from(EditIngredients.this);
			popUpIncomplete = new PopupWindow(
					inflater.inflate(R.layout.popup_add_ingredient_incomplete,
							null, false),300,200,true);
			popUpIncomplete.showAtLocation(layout, Gravity.CENTER, 0, 0);
			return;
		}
		float amount =Float.parseFloat(quantity);

		popUp.dismiss();

		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
		
		if(update) {       
		    changedIngred = RecipeIngredients.get(pos);
		    changedIngred.setName(type);
		    changedIngred.setUnit(unit);
		    changedIngred.setQuantity(amount);
		    update=false;
		} else {
		    Ingredient ingredient = new Ingredient(type, unit, amount);
		    RecipeIngredients.add(ingredient);
		}
		updateIngredients();
	}
	
	
	/**
	 * Responds to the "Save" button which saves the changes to the ingredients and closes the activity 
	 * @param The View that is calling the method
	 *
	 */
	public void OnSaveIngredients(View v)
	{
		DbController DbC = DbController.getInstance(this, this);
		DbC.storeRecipeIngredients(RecipeIngredients, uri);
		updateIngredients();
		this.finish();
	}
	
	@Override
	public void onStop()
	{
		/* If the user has not explicitly told us to dismiss their changes, we save their changes for them. */
		if (!cancelled) {
			DbController DbC = DbController.getInstance(this, this);
			DbC.storeRecipeIngredients(RecipeIngredients, uri);
		}
		super.onStop();
	}
	
}
