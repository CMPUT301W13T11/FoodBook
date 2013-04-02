package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.Photo;

/**
 * Unit tests for the Photo class.
 * @author Marko Babic
 */
public class PhotoTests extends AndroidTestCase {

	protected void setUp() throws Exception
	{
		super.setUp();
	}
	
	/**
	 * Test the functionality of the constructors by ensuring that photo object are returned
	 * with expected id and path parameters and non-null bit_data parameters when one is expected
	 * to be created.
	 */
	public void testConstructors()
	{
		String testId = "test id";
		String testPath = "test path";
		Bitmap testBitmap = BogoPicGen.generateBitmap(100, 100);
		byte[] testByteArray = new byte[128];
		
		Photo test1 = new Photo(testId);
		assertTrue("test1 should have id equal to testId", test1.getId().equals(testId));
		assertTrue("test1 should have null path", test1.getPath() == null);
		assertTrue("test1 should have null bit_data", test1.getBitData() == null);
		
		Photo test2 = new Photo(testId, testPath);
		assertTrue("test2 should have id equal to testId", test2.getId().equals(testId));
		assertTrue("test2 should have path equal to testPath", test2.getPath().equals(testPath));
		assertTrue("test2 should have null bit_data", test2.getBitData() == null);
		
		Photo test3 = new Photo(testId, testPath, testBitmap);
		assertTrue("test3 should have id equal to testId", test3.getId().equals(testId));
		assertTrue("test3 should have path equal to testPath", test3.getPath().equals(testPath));
		assertTrue("test3 should have non-null bit_data", test3.getBitData() != null);
		
		Photo test4 = new Photo(testId, testByteArray);
		assertTrue("test4 should have id equal to testId", test4.getId().equals(testId));
		assertTrue("test4 should have null path", test4.getPath() == null);
		assertTrue("test4 should have non-null bit_data", test4.getBitData() != null);
		
		Photo test5 = new Photo(testId, testPath, testByteArray);
		assertTrue("test5 should have id equal to testId", test5.getId().equals(testId));
		assertTrue("test5 should have path equal to testPath", test5.getPath().equals(testPath));
		assertTrue("test5 should have non-null bit_data", test5.getBitData() != null);
	}
}
