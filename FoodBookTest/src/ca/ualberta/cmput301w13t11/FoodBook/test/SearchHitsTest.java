package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.test.AndroidTestCase;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerResponse;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerSearchResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Unit tests for the SearchHits class.
 * @author Marko Babic
 *
 */
public class SearchHitsTest extends AndroidTestCase {

	private Gson gson = new Gson();
	protected void setUp() throws Exception
	{
		super.setUp();
		
	}
	
	/**
	 * Tests the getHits() method by passing a valid server search query response with a known number of results
	 * with known content.
	 */
	public void testGetHits()
	{
		ServerSearchResponse<ServerRecipe> search_response;
		String json = "{\"took\":5,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\""
				+ ":{\"total\":1,\"max_score\":1.5397208,\"hits\":[{\"_index\":\"testing\",\"_type\":\"cmput301w13t11\",\"_id\"" +
				":\"test\",\"_score\":1.5397208, \"_source\" : {\"author\":{\"name\":\"tester\"},\"title\":\"test\",\"instructions\"" +
				":\"\",\"ingredients\":[],\"photos\":[],\"uri\":0}}]}}";;
		
		Type serverSearchResponseType = new TypeToken<ServerSearchResponse<ServerRecipe>>(){}.getType();
		try {
			search_response = gson.fromJson(json, serverSearchResponseType);
			ArrayList<ServerRecipe> hits = new ArrayList<ServerRecipe>();
	
			for (ServerResponse<ServerRecipe> sr : search_response.getHits()) {
				hits.add(sr.getSource());
			}
			
			assertTrue("getHits() should not return empty", !hits.isEmpty());
			
			assertTrue("getHits() should return server recipe with title \"test\".", 
					hits.get(0).getTitle().equals("test"));
			
			assertTrue("getHits() should return server recipe with author name \"tester\"", 
					hits.get(0).getAuthor().getName().equals("tester"));
			
			assertTrue("getHits() should return server recipe with blank instructions",
					hits.get(0).getInstructions().equals(""));
			
			assertTrue("getHits() should return server recipe with no ingredients",
					hits.get(0).getIngredients().isEmpty());
			
			assertTrue("getHits() should return server recipe with no photos",
					hits.get(0).getPhotos().isEmpty());
		} catch (JsonSyntaxException jse) {
			fail("JsonSyntaxException");
		}
		
		
	}
}
