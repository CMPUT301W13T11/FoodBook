package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.Collection;

import android.content.ContentValues;

public class DbManager<V extends FView> implements FModel<V> {
  
  private Collection<V> views;

  // implement FModel methods
  public void addView(V view) {
    if (!views.contains(view)) {
      views.add(view);
    }
  }

  public void deleteView(V view) {
    views.remove(view);
  }

  public void notifyViews() {
    for (V view : views) {
      view.update(this);
    }
  }

  // for the controllers
  public void save() {
  }
  
  public void load() {
    
  }
/*
  public void insert(Recipe recipe) {
    ContentValues values = RecipeToMap(recipe);
    db.insert("UserRecipes", null, values);
  }
  
  public void delete(Recipe recipe) {
    db.delete("UserRecipes", "Name = " + recipe.getTitle()); // FIX!
  }

  public void insert(Ingredient ingred) {
    ContentValues values = IngredientToMap(ingred);
    db.insert("UserIngredients", null, values);
  }
  
  public void delete(Ingredient ingred) {
    db.delete("UserIngredients", "Name = " + ingred.getName()); // FIX!
  }
  */

}
