package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerResponse;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerSearchResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.test.AndroidTestCase;

/**
 * Unit tests for the ServerResponse class.
 * @author Marko Babic
 *
 */
public class ServerResponseTests extends AndroidTestCase {

	private Gson gson = new Gson();
	protected void setUp() throws Exception
	{
		super.setUp();
		
	}
	
	/**
	 * Test the functionality of getSource() by ensuring that a recipe of known parameters
	 * is correctly extracted from a search response.
	 */
	public void testGetSource()
	{
		ServerSearchResponse<ServerRecipe> search_response;
		String json = "{\"took\":5,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\""
				+ ":{\"total\":1,\"max_score\":1.5397208,\"hits\":[{\"_index\":\"testing\",\"_type\":\"cmput301w13t11\",\"_id\"" +
				":\"test\",\"_score\":1.5397208, \"_source\" : {\"author\":{\"name\":\"tester\"},\"title\":\"test\",\"instructions\"" +
				":\"\",\"ingredients\":[],\"photos\":[],\"uri\":0}}]}}";;
		
		Type serverSearchResponseType = new TypeToken<ServerSearchResponse<ServerRecipe>>(){}.getType();
		try {
			search_response = gson.fromJson(json, serverSearchResponseType);
			ServerRecipe source = null;
			
			for (ServerResponse<ServerRecipe> sr : search_response.getHits()) {
				source = sr.getSource();
			}
			
			assertTrue("getSource() should not return null server recipe", source != null);
			
			assertTrue("getSource() should return server recipe with title \"test\".", 
					source.getTitle().equals("test"));
			
			assertTrue("getSource() should return server recipe with author name \"tester\"", 
					source.getAuthor().getName().equals("tester"));
			
			assertTrue("getSource() should return server recipe with blank instructions",
					source.getInstructions().equals(""));
			
			assertTrue("getSource() should return server recipe with no ingredients",
					source.getIngredients().isEmpty());
			
			assertTrue("getSource() should return server recipe with no photos",
					source.getPhotos().isEmpty());
		} catch (JsonSyntaxException jse) {
			fail("JsonSyntaxException");
		}
	}
}
