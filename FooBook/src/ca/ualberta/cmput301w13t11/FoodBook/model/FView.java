package ca.ualberta.cmput301w13t11.FoodBook.model;


/**
 * Generic View interface -- has only the update() method which depends on the Model M this
 * View is observing.
 * @author mbabic
 *
 * @param <M> A Model extending the FModel superclass.
 */

//Not that it matters but this is how Dr. Hindle has it, and gets rid of one warning -Pablo
//public interface FView<M extends FModel> {
public interface FView<M> {

	/**
	 * Update the View according the changes made to model M.
	 * @param model
	 */
	public void update(M model);  
 }
