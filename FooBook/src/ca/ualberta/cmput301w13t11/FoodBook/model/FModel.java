package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

/**
 * Generic model class -- essentially implements the same functionality as the Java "Observable" class.
 * @author mbabic
 *
 * @param <V> A "view" class which implements the FView interface.
 */
public class FModel<V extends FView> {

	private ArrayList<V> views;

	/**
	 * Constructor -- initializes the list of observing views.
	 */
	public FModel()
	{
		views = new ArrayList<V>();
	}

	/**
	 * Adds the given view to the list of observers of this model.
	 * @param view The view to be added.
	 */
	public void addView(V view) {
		if (!views.contains(view)) {
			views.add(view);
		}
	}

	/**
	 * Deletes the given view from the list of observers of this model.
	 * @param view The view to be deleted.
	 */
	public void deleteView(V view) {
		views.remove(view);
	}

	/**
	 * Forces all observing views to call update -- called when the model has been changed.
	 */
	public void notifyViews() {
		for (V view : views) {
			view.update(this);
		}
	}

	/**
	 * @return An ArrayList of views observing this model.
	 */
	public ArrayList<V> getViews() {
		return views;
	}

	/**
	 * Sets the observers of this model to be the given ArrayList of views.
	 * @param views The new list of observers for the model. 
	 */
	public void setViews(ArrayList<V> views) {
		this.views = views;
	}

}
