package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.Collection;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class DbManager<V extends FView> extends FModel<V> {

    private Collection<V> views;
    private SQLiteDatabase db;
    
    // create table SQL statements
    private String createRecipeTable = "CREATE TABLE UserRecipes (URI text, title text, author text, instructions text)";
    private String createUserIngredientTable = "CREATE TABLE UserIngredients (name text, unit text, quantity text)";
    private String createRecipeIngredientTable = "CREATE TABLE RecipeIngredients (recipeURI text, name text, unit text, quantity text)";

    // SQL queries
    private String getUserRecipes = "FROM UserRecipes SELECT *";
    private String getUserIngredients = "FROM UserIngredients SELECT *";
    
    // for the controllers
    public void save() {
    }
    
    public void load() {
      
    }

    public void insert(Recipe recipe) {
      ContentValues values = RecipeToMap(recipe);
      db.insert("UserRecipes", null, values);
      for (Ingredient ingred : recipe.getIngredients()) {
          insert(ingred, recipe.getUri());
      }
    }
    
    public void delete(Recipe recipe) {
      db.delete("UserRecipes", "URI = " + recipe.getUri(), null);
    }

    public void insert(Ingredient ingred) {
      ContentValues values = IngredientToMap(ingred);
      db.insert("UserIngredients", null, values);
    }
    
    public void delete(Ingredient ingred) {
      db.delete("UserIngredients", "name = " + ingred.getName(), null);
    }

    private void insert(Ingredient ingred, String recipeURI) {
        ContentValues values = new ContentValues();
        values.put("recipeURI", recipeURI);
        values.put("name", ingred.getName());
        values.put("unit", ingred.getUnit());
        values.put("quantity", ingred.getQuantity());
        db.insert("RecipeIngredients", null, values);
    }
   
    
    // CONVERTING OBJECTS TO CONTENT VALUES
    
    private ContentValues RecipeToMap(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put("uri", recipe.getUri());
        values.put("author", recipe.getAuthor().getName());
        values.put("title", recipe.getTitle());
        values.put("instructions", recipe.getInstructions());
        return values;
    }

    private ContentValues IngredientToMap(Ingredient ingred) {
        ContentValues values = new ContentValues();
        values.put("name", ingred.getName());
        values.put("quantity", ingred.getQuantity());
        values.put("unit", ingred.getUnit());
        return values;
    }
    
}
    
