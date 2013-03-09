package ca.ualberta.cmput301w13t11.FoodBank.model;

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
