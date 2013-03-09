package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.Collection;

public class SearchHits<T> {

	int total;
	double max_score;
	Collection<ServerResponse<T>> hits;
	public Collection<ServerResponse<T>> getHits()
	{
		return hits;
	}
	public String toString()
	{
		return (super.toString()+", "+total+", "+max_score+", "+hits);
	}
}
