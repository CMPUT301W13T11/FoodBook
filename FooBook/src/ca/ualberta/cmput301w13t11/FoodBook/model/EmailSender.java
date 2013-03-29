package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.app.Activity;
import android.content.Intent;
/**
 * Class responsible for e-mailing a recipe.
 * @author Michael Shirt
 *
 */
public class EmailSender{
	/**
	 * E-mails the given Recipe using the device's default client.
	 * @param currentActivity The activity from which the method is being called.
	 * @param R The Recipe to be e-mailed.
	 * @return true on success, false on failure, user is returned to currentActivity on completion.
	 */
	public static boolean emailRecipe(Activity currentActivity, Recipe R){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_SUBJECT, R.getTitle());
		i.putExtra(Intent.EXTRA_TEXT   , R.getTextVersion());
		try {
		    currentActivity.startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    return false;
		}
		return true;
	}
}