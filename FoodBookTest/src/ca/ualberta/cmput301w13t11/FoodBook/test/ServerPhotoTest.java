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
		String name = "test name";
		String path = "test path";
		Photo photo = new Photo(name, path, BogoPicGen.generateBitmap(100, 100));
		ServerPhoto sp = new ServerPhoto(photo);
		
		assertTrue("Ids should be identical.", sp.getId().equals(photo.getId()));
		assertTrue("Paths should be identical.", sp.getId().equals(photo.getId()));

	}

	/**
	 * Test toPhoto() by ensuring a ServerPhoto by simply ensuring it can be called without error.
	 * Equality of the created image with have to be inspected visually at run time.
	 */
	public void testToPhoto() {
		String name = "test name";
		String path = "test path";
		Photo photo = new Photo(name, path, BogoPicGen.generateBitmap(100, 100));
		ServerPhoto sp = new ServerPhoto(photo);

		Photo newPhoto = ServerPhoto.toPhoto(sp);
	}

}
