package ca.ualberta.cmput301w13t11.FoodBook.model;


/**
 * Generic View interface -- has only the update() method which is dependent upon the Model M this
 * View is observing.
 * @author mbabic
 *
 * @param <M> A Model extending the FModel superclass.
 */
public interface FView<M extends FModel> {

	/**
	 * Update the View according the changes made to model M.
	 * @param model
	 */
	public void update(M model);  
 }
