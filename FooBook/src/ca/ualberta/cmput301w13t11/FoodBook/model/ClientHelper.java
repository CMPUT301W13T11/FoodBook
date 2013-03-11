package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 *  Helper class -- responsible for transforming HTTP response into Java Objects
 *  and vice-versa in service of the ServerClient.
 * 
 * Code inspired by:
 * ESDemo with HTTP Client and GSON
 * 
 * LICENSE:
 * 
 * CC0 http://creativecommons.org/choose/zero/
 * 
 * To the extent possible under law, Abram Hindle and Chenlei Zhang has waived all copyright 
 * and related or neighboring rights to this work. This work is published from: Canada.
 *
 * @author Abram Hindle, Chenlei Zhang, Marko Babic
 *
 */
public class ClientHelper {
	
	static private Gson gson = new Gson();
	
	/**
	 * Empty constructor.
	 */
	public ClientHelper()
	{
	}
	

	/**
	 * Converts the given recipe to a JSON object (this includes re-encoding photos).
	 * @param recipe The recipe to be converted.
	 * @return A JSON version of the given recipe.
	 */
	public StringEntity toJSON(Recipe recipe)
	{
		StringEntity se = null;
		ServerRecipe sr = new ServerRecipe(recipe);
		
		try {
			se = new StringEntity(gson.toJson(sr));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return se;
		
	}
	
	/**
	 * After executing a search on the server, this method is called to 
	 * transform the search results into a list of recipes.
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Recipe> toRecipeList(String json) throws IOException
	{

		ServerSearchResponse<ServerRecipe> search_response;
		ArrayList<Recipe> search_results = new ArrayList<Recipe>();
		
		Type serverSearchResponseType = new TypeToken<ServerSearchResponse<ServerRecipe>>(){}.getType();
		try {
			search_response = gson.fromJson(json, serverSearchResponseType);
		} catch (JsonSyntaxException jse) {
			return search_results;
		}
		
		
		for (ServerResponse<ServerRecipe> sr : search_response.getHits())
		{
			ServerRecipe server_recipe = sr.getSource();
			search_results.add(ServerRecipe.toRecipe(server_recipe));
		}
		return search_results;
		
	}

}
