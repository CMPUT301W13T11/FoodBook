package ca.ualberta.cmput301w13t11.FoodBook.model;
// generic model interface
public class FModel<V extends FView> {

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
  
}
