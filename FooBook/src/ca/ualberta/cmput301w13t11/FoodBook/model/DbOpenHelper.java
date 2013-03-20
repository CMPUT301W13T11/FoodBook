package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.logging.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Database helper class.  Responsible for executing the Database creation statements and handling upgrades.
 * 
 * Uses the SQLiteOpenHelper class.
 * @author Mark Tupala
 *
 */
public class DbOpenHelper extends SQLiteOpenHelper {
	
    static private final Logger logger = Logger.getLogger(ServerClient.class.getName());

	
    // create table SQL statements
    private String createRecipeTable = "CREATE TABLE " + UserRecipes + " (URI text, title text, author text, instructions text)";
    private String createResultsTable = "CREATE TABLE " + ResultsRecipes + " (URI text, title text, author text, instructions text)";
    private String createUserIngredientsTable = "CREATE TABLE " + UserIngredients + " (name text, unit text, quantity text)";
    private String createRecipeIngredientsTable = "CREATE TABLE " + RecipeIngredients + " (recipeURI text, name text, unit text, quantity text)";
    
    //private String table_name = "create table UserRecipes (_id integer primary key autoincrement, URI text not null, title text no null, author text not null, " +
    //								"instructions text not null";	
//            "create table entries (_id integer primary key autoincrement, "
//                    + "date text not null, description text not null, " +
//                    "calories_per_serving text not null, units_per_serving text not null, " +
//                    "units_consumed text not null, total_calories text not null);";


    /**
     * Constructor.
     * @param context The context the Db is created in.
     * @param name the name of the database to be created.
     */
	public DbOpenHelper(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// DB has just been created, create tables
		db.execSQL(createRecipeTable);
		db.execSQL(createResultsTable);
		db.execSQL(createUserIngredientsTable);
		db.execSQL(createRecipeIngredientsTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
