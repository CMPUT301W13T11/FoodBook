package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Database helper class.  Responsible for executing the Database creation statements and handling upgrades.
 * @author Mark Tupala
 *
 */
public class DbOpenHelper extends SQLiteOpenHelper {
	
    // create table SQL statements
    private String createRecipeTable = "CREATE TABLE UserRecipes (URI text, title text, author text, instructions text)";
    private String createResultsTable = "CREATE TABLE ResultsRecipes (URI text, title text, author text, instructions text)";
    private String createUserIngredientsTable = "CREATE TABLE UserIngredients (name text, unit text, quantity text)";
    private String createRecipeIngredientsTable = "CREATE TABLE RecipeIngredients (recipeURI text, name text, unit text, quantity text)";

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
