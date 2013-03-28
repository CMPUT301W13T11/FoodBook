package ca.ualberta.cmput301w13t11.FoodBook;

import java.io.File;

import android.app.Application;
import android.os.Environment;
import android.os.StrictMode;
import ca.ualberta.cmput301w13t11.FoodBook.controller.DbController;
import ca.ualberta.cmput301w13t11.FoodBook.controller.ServerController;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.IngredientsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.RecipesDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerClient;

/**
 * Application class -- initiates all needed singletons as well as path variables needed by models and activity classes alike.
 * @author mbabic
 *
 */
public class FoodBookApplication extends Application {
	
		private static FoodBookApplication instance;
		transient private static DbManager gdb = null;
		transient private static ResultsDbManager gresultsdb = null;
		transient private static RecipesDbManager grecipesdb = null;
		transient private static IngredientsDbManager gingredientsdb = null;
		transient private static ServerClient gsc = null;
		transient private static ServerController gscc = null;
		transient private static DbController gdbc = null;
		public static String SEARCH_TYPE = "search_type";
		public static String KEYWORDS_SEARCH = "keywords";
		public static String ALL_INGREDIENTS_SEARCH = "all_ingredients";
		public static String SUBSET_INGREDIENTS_SEARCH = "subset_ingredients";
		private static String state;
		private static String sdCardPath;


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
			state = Environment.getExternalStorageState();
			sdCardPath = Environment.getExternalStorageDirectory()+File.separator;
		}
		
		public String getState()
		{
			return state;
		}
		
		/**
		 * Get the path to the Sd card for this device.
		 * @return The path to the SD card for this device.
		 */
		public String getSdCardPath()
		{
			return sdCardPath;
		}
		
		/**
		 * Get an instance of this application (used as a global sort of context when needed).
		 * @return The instance of the FoodBookApplication class.
		 */
		public static FoodBookApplication getApplicationInstance()
		{
			return instance;
		}
	


}
