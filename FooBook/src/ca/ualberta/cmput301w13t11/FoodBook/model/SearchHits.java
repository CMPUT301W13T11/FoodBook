package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class modeling the hits associated with a seach query to the ElasticSearch sever.
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
public class SearchHits<T> {

	int total;
	double max_score;
	Collection<ServerResponse<T>> hits;
	
	/**
	 * 
	 * @return Returns a collection of ServerResponses from the hits.
	 */
	public Collection<ServerResponse<T>> getHits()
	{
		if (hits != null)
			return hits;
		else
			return new ArrayList<ServerResponse<T>>();
	}
	public String toString()
	{
		return (super.toString()+", "+total+", "+max_score+", "+hits);
	}
}
