package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Application;
import android.os.StrictMode;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;

public class FoodBookApplication extends Application {
	
		private static FoodBookApplication instance;
		transient private static DbManager gdb = null;
		transient private static ResultsDbManager gresultsdb = null;
		transient private static RecipesDbManager grecipesdb = null;
		transient private static IngredientsDbManager gingredientsdb = null;
		transient private static ServerClient gsc = null;
		transient private static ServerController gscc = null;
		transient private static DbController gdbc = null;


		@Override
		public void onCreate()
		{
			super.onCreate();
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
			instance = this;
			gdb = DbManager.getInstance(this);
			gresultsdb = ResultsDbManager.getInstance(this);
			grecipesdb = RecipesDbManager.getInstance(this);
			gingredientsdb = IngredientsDbManager.getInstance(this);
			gsc = ServerClient.getInstance();
		}
		
		public static FoodBookApplication getApplicationInstance()
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
			return instance;
		}
	


}
