package ca.ualberta.cmput301w13t11.FoodBook.model;
// generic model interface
public interface FModel<V implements FView> {

  public void addView(V view);
  public void deleteView(V view);
  public void notifyViews();
  
}
