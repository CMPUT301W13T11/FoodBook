package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

// generic model supeclass
public class FModel<V extends FView> {

	private ArrayList<V> views;
	
	public FModel()
	{
		views = new ArrayList<V>();
	}
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

	public ArrayList<V> getViews() {
		return views;
	}

	public void setViews(ArrayList<V> views) {
		this.views = views;
	}
  
}
