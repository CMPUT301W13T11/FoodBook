package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.Type;

import ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerResponse;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerSearchResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.test.AndroidTestCase;

/**
 * Unit tests for the ServerSearchResponse class.
 * @author Marko Babic
 *
 */
public class ServerSearchResponseTests extends AndroidTestCase {
	
	private Gson gson = new Gson();
	protected void setUp() throws Exception
	{
		super.setUp();
		
	}
	
	/**
	 * Tests the getSources() method by passing a valid server search query response with a known number of
	 * results with known content.
	 */
	public void testGetSources() {
		ServerSearchResponse<ServerResponse<ServerRecipe>> search_response;
		String json = "{\"took\":5,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\""
				+ ":{\"total\":1,\"max_score\":1.5397208,\"hits\":[{\"_index\":\"testing\",\"_type\":\"cmput301w13t11\",\"_id\"" +
				":\"test\",\"_score\":1.5397208, \"_source\" : {\"author\":{\"name\":\"tester\"},\"title\":\"test\",\"instructions\"" +
				":\"\",\"ingredients\":[],\"photos\":[],\"uri\":0}}]}}";;
		if (json.equals(""))
			fail("getJsonServerSearchResponse failed");
		
		Type serverSearchResponseType = new TypeToken<ServerSearchResponse<ServerRecipe>>(){}.getType();
		try {
			search_response = gson.fromJson(json, serverSearchResponseType);
			assertTrue("getSources() should not return be empty.", !search_response.getSources().isEmpty());
			assertTrue("getSource() should return only one result.", search_response.getSources().size() == 1);
		} catch (JsonSyntaxException jse) {
			fail("JsonSyntaxException");
		}	
		
	}
	
}
