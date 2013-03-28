package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.logging.Logger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.EmailSender;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;
/**
 * Activity launched that presents to the user detailed information about a Recipe.
 * From here, the user may edit, e-mail or publish the recipe.
 * @author 
 *
 */
public class ViewRecipeActivity extends Activity implements FView<DbManager>
{

	static final String EXTRA_URI = "extra_uri";
	private long uri;
	private Recipe viewedRecipe;
	private PopupWindow popUp;
	private TextView recipeName;
	private TextView instructions;
	private ProgressDialog progressDialog;
	static private final Logger logger = Logger.getLogger(ViewRecipeActivity.class.getName());

	/**
	 * Async task to upload a recipe -- launched when the PublishRecipe button is pressed.
	 * @author mbabic
	 *
	 */
	private class UploadRecipeTask extends AsyncTask<Recipe, Void, ReturnCode>{

		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(ViewRecipeActivity.this, "", "Uploading Recipe...");


		}
		@Override
		protected ReturnCode doInBackground(Recipe... recipes) {
			try {
				Recipe recipe = recipes[0];
				DbController DbC = DbController.getInstance(ViewRecipeActivity.this, ViewRecipeActivity.this);
				ServerController SC=ServerController.getInstance(ViewRecipeActivity.this);
				ReturnCode retcode = SC.uploadRecipe(recipe);
				return retcode;
			} catch (Exception e) {
				return ReturnCode.ERROR;
			}
		}
		
		@Override
		protected void onPostExecute(ReturnCode ret)
		{
			progressDialog.dismiss();
			if (ret == ReturnCode.SUCCESS) {
				ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
				LayoutParams darkenParams = darkenScreen.getLayoutParams();
				darkenParams.height = 1000;
				darkenParams.width = 1000;
				darkenScreen.setLayoutParams(darkenParams);
				//make the popup
				LinearLayout layout = new LinearLayout(ViewRecipeActivity.this);
				LayoutInflater inflater = LayoutInflater.from(ViewRecipeActivity.this);
				popUp = new PopupWindow(inflater.inflate(R.layout.popup_recipe_upload_success, null, false),300,130,true);
				popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
			} else if (ret == ReturnCode.BUSY) {
				
				Toast.makeText(getApplicationContext(), "It looks like the server is busy or not responding. Aieee.", Toast.LENGTH_LONG).show();

			} else if (ret == ReturnCode.ALREADY_EXISTS) {
				
				/* Temp toast, could eventually be its own popup if someone cares to do it. */
				Toast.makeText(getApplicationContext(), "A recipe with this uri already exists. :S", Toast.LENGTH_LONG).show();
				
			} else if (ret == ReturnCode.ERROR) {
				
				/* Temp toast, could eventually be its own popup */
				Toast.makeText(getApplicationContext(),
						"An error occurred.  Sorry about that. :(", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		recipeName = (TextView) findViewById(R.id.textView2);
		instructions = (TextView) findViewById(R.id.textView5);
		
		Intent intent = getIntent();
		//String URI = intent.getStringExtra(ViewRecipeActivity.EXTRA_URI);
		//long uri=Long.parseLong(URI);
		uri = intent.getLongExtra(EXTRA_URI, 0);
		//Log.d("recipeuri", Long.toString(uri));
		instructions.setMovementMethod(new ScrollingMovementMethod());
		this.updateView();
	}
	public void updateView(){
		DbController DbC = DbController.getInstance(this, this);
		viewedRecipe = DbC.getUserRecipe(uri);
		recipeName.setText(viewedRecipe.getTitle());
		instructions.setText(viewedRecipe.getInstructions());
		
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DbC.getRecipeIngredients(uri));
		listView.setAdapter(adapter);

	}

	public void OnGotoMyRecipes(View View)
	{
		// responds to button Go Back
		 Intent intent = new Intent(View.getContext(), MyRecipes.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 ViewRecipeActivity.this.finish();
    }
	
	public void OnEditRecipe (View View)
    {	
		//Intent intent1 = getIntent();
		//String URI = intent1.getStringExtra(MyRecipes.EXTRA_URI);
		
		// responds to button Edit Recipe
    	Intent intent = new Intent(this, EditRecipeActivity.class);
    	//intent.putExtra(EXTRA_URI, URI);
    	intent.putExtra(EXTRA_URI, uri);
		startActivity(intent);
		finish();
    }
	public void OnViewPhotos (View View)
    {
		// responds to button Edit Photos
    	Intent intent = new Intent(this, ViewPhotosActivity.class);
    	intent.putExtra(EXTRA_URI, uri);
    	intent.putExtra(ViewPhotosActivity.CALLER, "ViewRecipeActivity");
		startActivity(intent);
    }
	public void OnEmailRecipe (View View)
    {
		EmailSender.emailRecipe(this, viewedRecipe);
    	
    }
	public void OnPublishRecipe (View View)
    {
		// responds to button Publish Recipe
		DbController DbC = DbController.getInstance(this, this);
		ServerController SC=ServerController.getInstance(this);
		Intent intent = getIntent();
		//String URI = intent.getStringExtra(MyRecipes.EXTRA_URI);
		//long uri=Long.parseLong(URI);
		intent.putExtra(EXTRA_URI, uri);
		
		
		new UploadRecipeTask().execute(viewedRecipe);
		    	
    }
	public void OnOK(View v){
		popUp.dismiss();
		//remove the darkScreen
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
	}
	@Override
	public void update(DbManager db)
	{
		this.updateView();
	}

	public void onDestroy()
	{	super.onDestroy();
		DbController DbC = DbController.getInstance(this, this);
		DbC.deleteView(this);
	}

}
