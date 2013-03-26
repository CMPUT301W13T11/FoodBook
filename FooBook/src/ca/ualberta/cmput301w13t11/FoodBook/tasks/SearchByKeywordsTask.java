package ca.ualberta.cmput301w13t11.FoodBook.tasks;

import android.os.AsyncTask;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient.ReturnCode;

public class SearchByKeywordsTask extends AsyncTask<String, Void, ReturnCode>{

	@Override
	protected ReturnCode doInBackground(String... keywords) {
		String searchString = keywords[0];
		ServerClient sc = ServerClient.getInstance();
		try {
			ReturnCode retcode = sc.searchByKeywords(searchString);
			return retcode;
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.ERROR;
		}
	}
	
	@Override
	protected void onPostExecute(ReturnCode ret)
	{
		ServerClient sc = ServerClient.getInstance();
		sc.writeResultsToDb();
	}
}
