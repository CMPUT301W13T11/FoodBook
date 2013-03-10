package ca.ualberta.cmput301w13t11.FoodBook.test;

import junit.framework.TestSuite;

import org.junit.Test;

import android.test.suitebuilder.TestSuiteBuilder;


/**
 * A test suite containing all tests for my application.
 */
public class AllTests extends TestSuite {
	
    public static TestSuite suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}
