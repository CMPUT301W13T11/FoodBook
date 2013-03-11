package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerPhoto;
/**
 * Unit tests for the ServerPhoto class.
 * @author mbabic
 *
 */
public class ServerPhotoTest extends AndroidTestCase {

	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	
	/**
	 * Test the ServerPhoto constructor to insure a photo is transformed into a ServerPhoto without error.
	 * Equality of images will have to be inspected visually.
	 */
	public void testServerPhoto() {
		Photo photo = new Photo(BogoPicGen.generateBitmap(100, 100));
		ServerPhoto sp = new ServerPhoto(photo);
		
		assertTrue("Names not identical.", sp.getName().equals(photo.getName()));
	}

	/**
	 * Test toPhoto() by insuring a given Photo.
	 * Equality of images will have to be inspected visually.
	 */
	public void testToPhoto() {
		Photo photo = new Photo(BogoPicGen.generateBitmap(100, 100));

	}

}
