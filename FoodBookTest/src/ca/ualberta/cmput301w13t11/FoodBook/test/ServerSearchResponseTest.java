package ca.ualberta.cmput301w13t11.FoodBook.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe;
import ca.ualberta.cmput301w13t11.FoodBook.model.ServerSearchResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Unit tests for the ServerSearchResponse class.
 * @author mbabic
 *
 */
public class ServerSearchResponseTest extends TestCase{

	private Gson gson = new Gson();
	
	private String getJsonServerSearchResponse()
	{
		String out, json = "";

		/* Extract the JSON string from the test file. */
		try {
			FileReader file = new FileReader("docs/JSONServerResponse.txt");
			BufferedReader br = new BufferedReader(file);
			
			while ((out = br.readLine()) != null) {
				json += out;
			}

			return json;
		} catch (FileNotFoundException fnfe) {
			fail("file not found");
		} catch (IOException ioe) {
			fail("IOException");
		}
		return "";
	}
	
	@Test
	/**
	 * Tests the getHits() method by passing a valid server search query response with a known number of results
	 * with known content.
	 */
	public void testGetHits()
	{
		ServerSearchResponse search_response;
		String json = getJsonServerSearchResponse();
		if (json.equals(""))
			fail("getJsonServerSearchResponse failed");
		
		Type serverSearchResponseType = new TypeToken<ServerSearchResponse<ServerRecipe>>(){}.getType();
		try {
			search_response = gson.fromJson(json, serverSearchResponseType);
			assertTrue("search_results should not be empty.", !search_response.getHits().isEmpty());
		} catch (JsonSyntaxException jse) {
			fail("JsonSyntaxException");
		}
		
		
	}
	@Test
	/**
	 * Tests the getSources() method by passing a valid server search query response with a known number of
	 * results with known content.
	 */
	public void testGetSources() {
		ServerSearchResponse search_response;
		String json = getJsonServerSearchResponse();
		if (json.equals(""))
			fail("getJsonServerSearchResponse failed");
		
		Type serverSearchResponseType = new TypeToken<ServerSearchResponse<ServerRecipe>>(){}.getType();
		try {
			search_response = gson.fromJson(json, serverSearchResponseType);
			assertTrue("search_results should not be empty.", !search_response.getSources().isEmpty());
			//assertTrue("search_results[0] should have author name \"tester\"",
			//		search_response.getSources().get(0).getAuthor().getName().equals("tester"));
		} catch (JsonSyntaxException jse) {
			fail("JsonSyntaxException");
		}	}

}
