package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class modeling a response to a search query returned from the server.
 * Original code from:
 * ESDemo with HTTP Client and GSON
 * 
 * LICENSE:
 * 
 * CC0 http://creativecommons.org/choose/zero/
 * 
 * To the extent possible under law, Abram Hindle and Chenlei Zhang has waived all copyright and related or neighboring rights to this work. This work is published from: Canada.
 * @author Abram Hindle, Chenlei Zhang, Marko Babic
 *
 * @param <T> Generic -- the type Class type we expect to receive in _source.
 */
public class ServerSearchResponse<T> {

	int time_took;
	boolean time_out;
	transient Object _shards;
	SearchHits<T> hits;
	boolean exists;
	/**
	 * Returns a Collection of Server Responses 
	 * @return
	 */
	public Collection<ServerResponse<T>> getHits()
	{
		return hits.getHits();
	}
	
	/**
	 * @return Returns a collection of source objects from the server search response.
	 */
	public Collection<T> getSources()
	{
		Collection<T> out = new ArrayList<T>();
		for (ServerResponse<T> sr : getHits())
		{
			out.add(sr.getSource());
		}
		return out;
	}
	
	public String toString()
	{
		//TODO
		return "needs to implemented";
	}
}
