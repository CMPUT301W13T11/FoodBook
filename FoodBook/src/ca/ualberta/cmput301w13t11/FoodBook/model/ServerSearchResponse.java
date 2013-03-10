package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Collection;

public class ServerSearchResponse<T> {

	int time_took;
	boolean time_out;
	transient Object _shards;
	SearchHits<T> hits;
	boolean exists;
	public Collection<ServerResponse<T>> getHits()
	{
		return hits.getHits();
	}
	
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
