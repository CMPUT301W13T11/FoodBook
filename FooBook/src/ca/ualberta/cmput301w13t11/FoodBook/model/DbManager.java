package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.Collection;

import android.content.ContentValues;

public class DbManager<V implements FView> implements FModel<V implements FView> {
    
    private Collection<V> views;
    
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
      for (Ingredient ingred : recipe.ingredients) {
          insert(ingred, recipe.uri);
      }
    }
    
    public void delete(Recipe recipe) {
      db.delete("UserRecipes", "URI = " + recipe.getUri());
    }

    public void insert(Ingredient ingred) {
      ContentValues values = IngredientToMap(ingred);
      db.insert("UserIngredients", null, values);
    }
    
    public void delete(Ingredient ingred) {
      db.delete("UserIngredients", "name = " + ingred.getName());
    }

    private insert(Ingredient ingred, String recipeURI) {
        values = new ContentValues();
        values.put("recipeURI", recipeURI);
        values.put("name", ingred.getName());
        values.put("unit", ingred.getUnit());
        values.put("quantity", ingred.getQuantity());
        db.insert("RecipeIngredients", null, values);
    }
   
    
    // CONVERTING OBJECTS TO CONTENT VALUES
    
    private ContentValues RecipeToMap(Recipe recipe) {
        values = new ContentValues();
        values.put("uri", recipe.uri);
        values.put("author", recipe.author);
        values.put("title", recipe.title);
        values.put("instructions", recipe.instructions);
        return values;
    }

    private ContentValues IngredientToMap(Ingredient ingred) {
        values = new ContentValues();
        values.put("name", ingred.name);
        values.put("quantity", ingred.quantity);
        values.put("unit", ingred.unit);
        return values;
    }
    
    
    
    
    
    
    
    
   
    
}  
    
