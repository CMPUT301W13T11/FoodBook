package ca.ualberta.cmput301w13t11.FoodBook;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * Holds common methods used by activities with popups
 * @author Michael Shirt
 *
 */
public class ActivityWithPopup extends Activity{
	PopupWindow popUp;
	/**
	 * This sets a dark overlay over the main screen so that the popup
	 * is more visible
	 */
	protected void darkenScreen(){
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 1000;
		darkenParams.width = 1000;
		darkenScreen.setLayoutParams(darkenParams);
	}
	/**
	 * This returns the screen back to normal/takes away the darken effect that
	 * darkenScreen sets
	 */
	protected void normalizeScreen(){
		ImageView darkenScreen = (ImageView) findViewById(R.id.darkenScreen);
		LayoutParams darkenParams = darkenScreen.getLayoutParams();
		darkenParams.height = 0;
		darkenParams.width = 0;
		darkenScreen.setLayoutParams(darkenParams);
	}
}