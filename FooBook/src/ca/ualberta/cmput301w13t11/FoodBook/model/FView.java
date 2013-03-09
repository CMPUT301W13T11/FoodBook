package ca.ualberta.cmput301w13t11.FoodBook.model;

// generic view interface

public interface FView<M extends FModel> {

  public void update(M model);  

}
