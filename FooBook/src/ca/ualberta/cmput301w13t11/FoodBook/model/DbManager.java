package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
/**
 * Singleton class that manages the application's database.
 * @author Mark Tupala
 *
 */
public class DbManager extends FModel<FView> {

	
    // the actual database 
    protected static SQLiteDatabase db;
    protected static DbOpenHelper dbHelper;
    
    // singleton pattern implementation
    private static DbManager instance = null;
    
    //Used to ensure that multiple connections to the database are not opened and
    //also to ensure that multiple database helpers do not open.
    private static boolean has_executed = false;
    
    // name of database file
    private String dbFileName= "RecipeApplicationDb";
    public String recipesTable;// = "UserRecipes";
    public String ingredsTable;// = "RecipeIngredients";
    public String photosTable;// = "RecipePhotos";
    public String getSQL;
    
    /**
     * Protected constructor because we're using the singleton pattern.
     */
    protected DbManager(Context context) {
    	super();
    	// open or create the sqlite db accordingly
    	if (DbManager.has_executed == false) {
    		// Use the has_executed flag to ensure we do not open
    		// multiple connections to the database and we do not
    		// create multiple dbHelper objects.
    		dbHelper = new DbOpenHelper(context, dbFileName);
    		db = dbHelper.getWritableDatabase();
    		DbManager.has_executed = true;
    	}
    }
    
    /**
     * @return True if DbManager constructor has been called before, false otherwise.
     */
    public static boolean getHasExecuted()
    {
    	return has_executed;
    }
    
    /**
     * @return SQLiteDatabase attribute
     */
    public static SQLiteDatabase getDb()
    {
    	return db;
    }
    
    /**
     * @return getSQL string attribute
     */
    public String getGetSQL()
    {
    	return getSQL;
    }
    
    /**
     * Get instance of the singleton DbManager.
     * @return The instance of the class.
     */
    public static DbManager getInstance(Context context) {
    	if (instance == null) {
    		// db instance doesn't exist, create new one
    		instance = new DbManager(context);
    	}	
    	return instance;
    }

    /**
     * Get instance of the singleton DbManager, when we know that it exists
     * @return The instance of the class.
     */
    public static DbManager getInstance() {	
    	return instance;
    }
    
    // *********************************************
    // INSERT RECIPES AND THEIR INGREDIENTS/PHOTOS
    // *********************************************
    
    /**
     * Inserts a recipe into the table.
     * @param recipe The Recipe to be stored.
     */
    public void insertRecipe(Recipe recipe) 
    {
        ContentValues values = recipe.toContentValues();
        db.insert(recipesTable, null, values);
        for (Ingredient ingred : recipe.getIngredients()) {
            insertRecipeIngredients(ingred, recipe.getUri());
        }

        Bitmap temp = null;
        ArrayList<Photo> photos = recipe.getPhotos();
        ArrayList<Photo> fullPhotos = getFullPhotos(photos);
        for (Photo photo : fullPhotos) {
        	temp = photo.getPhotoBitmap();
        	if (temp != null)
        		insertRecipePhotos(photo, temp, recipe.getUri());
        }
    }

    /**
     * Inserts the given Ingredient into the database such that it is associated with the
     * recipe identified by recipeURI.
     * @param ingred The ingredient to be inserted.
     * @param recipeURI The URI of the Recipe with which to associate the Ingredient.
     */
    public boolean insertRecipeIngredients(Ingredient ingred, long recipeURI) {
        ContentValues values = ingred.toContentValues();
        values.put("recipeURI", recipeURI);
        db.insert(ingredsTable, null, values);
        return true;
    }

    /**
     * Inserts the given Photo into the database such that it is associated with the
     * recipe identified by recipeURI.
     * @param photo The photo to be inserted.
     * @param recipeURI The URI of the Recipe with which to associate the Photo.
     */
    public boolean insertRecipePhotos(Photo photo, Bitmap bitmap, long recipeURI) {
    	
        /* We first attempt to store the bitmap associated with the photo to the Db */
    	
        if (!savePhotoToDevice(bitmap, photo)) {
            /* Saving failed, return false. */
            return false;
        }

        /* Else, we can safely place the photo information into the database. */
        
        ContentValues values = new ContentValues();
        values.put("recipeURI", recipeURI);
        values.put("id", photo.getId());
        values.put("path", photo.getPath());
        db.insert(photosTable, null, values);
        
        /* If we got here, everything was successful. */
        return true;
    }
	
	// *********************************************
	// UPDATE RECIPE
	// *********************************************
	
	/**
	 * Update the entry in the Db with the URI given by the Recipe parameter with the values
	 * given by the Recipe parameter.
	 * @param recipe The updated recipe to be stored in the database.
	 */
	public void updateRecipe(Recipe recipe)
	{
		long uri = recipe.getUri();
		updateRecipeTitle(uri, recipe.getTitle());
		updateRecipeInstructions(uri, recipe.getInstructions());
	}
	
	
	
	/**
	 * Updates the title of the Recipe with the given URI.
	 * @param uri The URI of the Recipe to be updated.
	 * @param tableName The name of the table in which the Recipe resides.
	 * @param newTitle The new title of the Recipe.
	 */
	private void updateRecipeTitle(long uri, String newTitle)
	{
		String filter = "URI=" + Long.toString(uri);
		ContentValues args = new ContentValues();
		args.put("title", newTitle);
		db.update(recipesTable, args, filter, null);
	}
	
	/**
	 * Updates the instructions of he Recipe with the given URI.
	 * @param uri The URI of the Recipe to be updated.
	 * @param tableName The name of the table in which the Recipe resides.
	 * @param newInstructions The new instructions for the Recipe.
	 */
	private void updateRecipeInstructions(long uri, String newInstructions)
	{
		String filter = "URI=" + Long.toString(uri);
		ContentValues args = new ContentValues();
		args.put("instructions", newInstructions);
		db.update(recipesTable, args, filter, null);
	}
    
	/* 
	 * Retrieve Recipes \
	 */
	
    /**
     * Returns an ArrayList of all the Recipes stored in the table.
	 * @return An ArrayList of all the Recipes stored in the table.
	 */
	public ArrayList<Recipe> getRecipes() {
	    Cursor cursor = db.rawQuery(getSQL, null);
	    return cursorToRecipes(cursor);
	}

	/**
	 * Returns Recipe stored in the table, given recipe's uri
	 * @return A Recipe stored in the table.
	 */
	public Recipe getRecipe(long uri) {
		String query = new String("SELECT * FROM " + recipesTable + " WHERE URI = " + uri);
	    Cursor cursor = db.rawQuery(query, null);
	    return cursorToRecipe(cursor);
	}
	
//	/**
//	 * Returns Recipe stored in the table, given recipe's uri
//	 * @return ARecipes stored in the table.
//	 */
//	public Recipe getRecipe() {
//	    Cursor cursor = db.rawQuery(getSQL, null);
//	    return cursorToRecipe(cursor);
//	}
    
    /**
     * Given a cursor, convert it to an ArrayList of Recipes.
     * @param cursor The cursor over which we will iterate to get recipes from.
     * @return An ArrayList of Recipes.
     */
    protected ArrayList<Recipe> cursorToRecipes(Cursor cursor) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long uri = cursor.getLong(0);
            User author = new User(cursor.getString(2));
            String title = cursor.getString(1);
            String instructions = cursor.getString(3);
            ArrayList<Ingredient> ingredients = getRecipeIngredients(uri);
            ArrayList<Photo> photos = getRecipePhotos(uri);
            Recipe recipe = new Recipe(uri, author, title, instructions, ingredients, photos);
            recipes.add(recipe);
            cursor.moveToNext();
        }
        return recipes;
    }


    /**
    * Given a cursor, convert it to a Recipe.
    * @param cursor The cursor over which we will iterate to get recipes from.
    * @return An ArrayList of Recipes.
    */
   protected Recipe cursorToRecipe(Cursor cursor) {
 
       cursor.moveToFirst();
       if (cursor.getCount()>=1) {
	       long uri = cursor.getLong(0);
	       User author = new User(cursor.getString(2));
	       String title = cursor.getString(1);
	       String instructions = cursor.getString(3);
	       ArrayList<Ingredient> ingredients = getRecipeIngredients(uri);
	       ArrayList<Photo> photos = getRecipePhotos(uri);
	       ArrayList<Photo> fullPhotos = getFullPhotos(photos);
	       Recipe recipe = new Recipe(uri, author, title, instructions, ingredients, fullPhotos);
	
	       if (cursor.getCount()!=0) {
	    	   //print error message here
	       }
	       return recipe;
       }
       else{
    	   return null;
       }
   }
   // --- 
    
    
    /**
     * Gets all the Ingredients associated with the recipe identified by its URI.
     * @param uri The URI of the recipe whose ingredients we are fetching.
     * @return An ArrayList of the Ingredients associated with the recipe.
     */
    public ArrayList<Ingredient> getRecipeIngredients(long uri) {
    	Cursor cursor = db.rawQuery("Select * From " + ingredsTable + " Where recipeURI = " + uri, null);
    	return cursorToIngredients(cursor);
    }
    
    /**
     * Gets all the Photos associated with the recipe identified by its URI.
     * @param uri The URI of the recipe whose photos we are fetching.
     * @return An ArrayList of the Photos associated with the recipe.
     */
    public ArrayList<Photo> getRecipePhotos(long uri) 
    {
    	Cursor cursor = db.rawQuery("Select * From " + photosTable + " Where recipeURI = " + uri, null);
    	return cursorToPhotos(cursor);
    }
    
    

    /**
     * Removes the given photo from the local storage device.
     * @param photo The photo to be deleted.
     * @return true on success, false on failure
     */
    public boolean removeRecipePhoto(Photo photo) {
   	
    	int success = db.delete(photosTable, "id = " + photo.getId(), null); 
 
    	Boolean deleted = false;
    	
    		try{
		    	File file = new File(photo.getPath());
		        deleted = file.delete();
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}  	
    	return (success >= 1 && deleted == true);
    }
        
    /**
     * Deletes the ingredients associated with the recipe specified by the uri (ie.
     * removes them from the RecipeIngredients table).
     * @param uri The uri of the recipe whose ingredients we would like to remove.
     * @return true on success, false on failure
     */
    public boolean removeRecipeIngredients(long uri) {
    	
    	int success = 0;
    	try {
    		success = db.delete(ingredsTable, "recipeURI = " + uri, null); 
    	} catch (SQLiteException sqle) {
    		sqle.printStackTrace();
    	}
    	    	
    	return (success>=1);
    }
    
//    /**
//     * Deletes the ingredient associated with the recipe specified by the uri (ie.
//     * removes it from the RecipeIngredients table).
//     * @param uri The uri of the recipe whose ingredients we would like to remove.
//     * @return true on success, false on failure
//     */
//    public boolean removeRecipeIngredient(String ingredName, long uri) {
//    	
//        int success = db.delete(ingredsTable, "recipeURI = " + uri + " and name = " + ingredName, null); 
//        return (success>=1);
//    }
    
    
    /**
     * Given a cursor, convert it to an ArrayList of Ingredients.
     * @param cursor The cursor over which we will iterate to get ingredients from.
     * @return An ArrayList of Ingredients.
     */
    protected ArrayList<Ingredient> cursorToIngredients(Cursor cursor) {
        ArrayList<Ingredient> ingreds = new ArrayList<Ingredient>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            String unit = cursor.getString(2);
            float quantity = cursor.getFloat(3);
            Ingredient ingred = new Ingredient(name, unit, quantity);
            ingreds.add(ingred);
            cursor.moveToNext();
        }
        return ingreds;
    }
    
    /**
     * Given a cursor, convert it to an ArrayList of Photos.
     * @param cursor The cursor over which we will iterate to get photos from.
     * @return An ArrayList of Photos.
     */
    protected ArrayList<Photo> cursorToPhotos(Cursor cursor) {
        ArrayList<Photo> photos = new ArrayList<Photo>();
        //ArrayList<Photo> fullPhotos = new ArrayList<Photo>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(1);
            String path = cursor.getString(2);
            Photo photo = new Photo(id, path);
            photos.add(photo);
            cursor.moveToNext();
        }
        //fullPhotos = getFullPhotos(photos);
        return photos;
    }


    /**
     * Remove the given recipe from both local storage and the database.
     * @param recipe The recipe to be deleted.
     * @return True on success, False on failure.
     */
    public boolean removeRecipe(Recipe recipe) {
    	
    	long uri = recipe.getUri();
    	
    	int recipes_removed = 0;
    	boolean deleted_pictures = true;

    	try{

    		recipes_removed = db.delete(recipesTable, "URI = " + uri, null);

    		ArrayList<Photo> photos = getRecipePhotos(uri); 
    		for (Photo p: photos){
    			if (removeRecipePhoto(p)!=true){
    				deleted_pictures=false;
    			}
    		}
  
    		removeRecipeIngredients(uri);

    	} catch(Exception e){e.printStackTrace();};

    	return (recipes_removed==1 && deleted_pictures==true);
    }
    
    /**
     * Saves the given bitmap to the local device.
     * @param bitmap The bitmap to be saved.
     * @param photo .
     * @return True on success, false on failure.
     */
    private boolean savePhotoToDevice(Bitmap bitmap, Photo photo)
    {
    	String imgPath = photo.getPath(); 	/* the path to the image file on the device */
    	File file = null; 					/* the image file itself */
    	boolean success = false;			/* set to true on successful write of image file to device storage*/
    	boolean worked = false;			/* set to true on successful compression of given bitmap*/
    	FileOutputStream outStream = null;	/* the file write stream */
    	
    	try {

    		file = new File(imgPath);
			outStream = new FileOutputStream(file);
			
			worked = bitmap.compress(Bitmap.CompressFormat.PNG, 30, outStream);
			outStream.flush();
			outStream.close();
			success = true;
			imgPath = file.getAbsolutePath();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			Log.d("Failed to save image.", "Failed to save image.");
			return false;
		} 

    	return (success && worked);
    }
 
	
	/**
	 * Given the ArrayList of photos with only an id and a pathname, returns a list of photos
	 * with a byte_array as well by fetching the information from the SdCard.
	 * @param photos A list of partial photos from which we wish to construct full photos.
	 * @return An ArrayList of a photos with the corresponding byte_area representing the img data included.
	 */
	private ArrayList<Photo> getFullPhotos(ArrayList<Photo> photos)
	{
		if (photos == null || photos.isEmpty()) {
			return new ArrayList<Photo>();
		}
		ArrayList<Photo> fullPhotos = new ArrayList<Photo>();
	    Options options = new Options();
	    options.inJustDecodeBounds = false;
		for (int i = 0; i < photos.size(); i++)
		{
			Photo temp = photos.get(i);
			Photo fullPhoto = new Photo(temp.getId(), temp.getPath(), BitmapFactory.decodeFile(temp.getPath(), options));
			fullPhotos.add(fullPhoto);
		}
		return fullPhotos;
	}
}



    
