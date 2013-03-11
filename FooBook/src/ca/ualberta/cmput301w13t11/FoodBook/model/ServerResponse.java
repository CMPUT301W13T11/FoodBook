package ca.ualberta.cmput301w13t11.FoodBook.model;

/**
 * A class modeling a response to a POST by the server.
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
public class ServerResponse<T> {

	String _index;
	String _type;
	String _id;
	int _version;
	boolean exists;
	T _source;
	double max_score;
	public T getSource()
	{
		return _source;
	}
}
