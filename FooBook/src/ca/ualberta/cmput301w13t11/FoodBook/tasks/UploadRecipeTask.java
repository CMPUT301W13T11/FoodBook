package ca.ualberta.cmput301w13t11.FoodBook.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import ca.ualberta.cmput301w13t11.FoodBook.model.Recipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

public class UploadRecipeTask extends AsyncTask<Recipe, Void, ReturnCode>{

	@Override
	protected void onPreExecute()
	{
		ProgressDialog progressDialog;

	}
	@Override
	protected ReturnCode doInBackground(Recipe... recipes) {
		Recipe recipe = recipes[0];
		ServerClient sc = ServerClient.getInstance();
		try {
			ReturnCode retcode = sc.uploadRecipe(recipe);
			return retcode;
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.ERROR;
		}
	}
	
	@Override
	protected void onPostExecute(ReturnCode ret)
	{
		
	}
}
