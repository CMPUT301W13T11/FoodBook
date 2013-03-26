package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.app.Activity;
import android.content.Intent;

public class EmailSender{
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