package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.DbManager;
import ca.ualberta.cmput301w13t11.FoodBook.model.ResultsDbManager;
/**
 * Unit tests for the ResultsDbManager class.
 * @author mbabic
 *
 */
public class ResultsDbManagerTests extends AndroidTestCase {

	private ResultsDbManager rdb = null;

	protected void setUp() throws Exception
	{
		super.setUp();
	}	
	
	/**
	 * Test both getInstance() methods ensure that a non-null instance of ResultsDbManager is returned by getInstance().
	 */
	public void testGetInstance()
	{
		rdb = ResultsDbManager.getInstance(this.getContext());
		assertTrue("getInstance should not return null.", rdb != null);
		assertTrue("getInstance w/o args should not return null now.", ResultsDbManager.getInstance() != null);
	}
	
	

}
