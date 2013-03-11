package ca.ualberta.cmput301w13t11.FoodBook.test;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.FModel;
import ca.ualberta.cmput301w13t11.FoodBook.model.FView;
/**
 * Unit tests for the FModel class.
 * @author mbabic
 *
 */
public class FModelTests extends AndroidTestCase {

	/**
	 * Mock View implementing the FView interface for testing purposes.
	 * @author mbabic
	 *
	 */
	public class MockView implements FView<FModel>
	{
		public int x;
		public MockView(int x)
		{
			this.x = x;
		}
		
		@Override
		public void update(FModel m)
		{
			this.x++;
		}
	}
	
	protected void setUp() throws Exception
	{
		super.setUp();
	}
	
	/**
	 * Test addView() by adding a known number of views to the array making sure there are no errors
	 * and the size is as expected -- also make sure that it does not the same view more than once.
	 */
	public void testAddView()
	{
		FModel m = new FModel();
		MockView v1 = new MockView(1);
		MockView v2 = new MockView(2);
		MockView v3 = new MockView(3);
		
		m.addView(v1);
		m.addView(v2);
		m.addView(v3);
		
		assertTrue("views should have size == 3", m.getViews().size() == 3);
		
		m.addView(v2);
		assertTrue("views should still have size == 3", m.getViews().size() == 3);

	}
	
	/**
	 * Test deleteView() by adding a single view, then deleting it.
	 */
	public void testDeleteView()
	{
		FModel m = new FModel();
		MockView v1 = new MockView(1);
		m.addView(v1);
		
		assertTrue("If failed here, the method addView() failed to execute properly.", m.getViews().size() == 1);
		
		m.deleteView(v1);
		assertTrue("views should be empty now", m.getViews().isEmpty());
	}
	
	/**
	 * Test notifyViews() by adding views, calling notify views, and ensuring that the observing
	 * Views called their update method.
	 */
	public void testNotifyViews()
	{
		FModel m = new FModel();
		
		MockView v1 = new MockView(1);
		MockView v2 = new MockView(2);
		MockView v3 = new MockView(3);
		
		m.addView(v1);
		m.addView(v2);
		m.addView(v3);
		
		assertTrue("If failed here, the method addView() failed to execute properly.", m.getViews().size() == 3);
		
		m.notifyViews();
		assertTrue("View 1 failed to update", v1.x == 2);
		assertTrue("View 2 failed to update", v2.x == 3);
		assertTrue("View 3 failed to update", v3.x == 4);

		
	}
}
