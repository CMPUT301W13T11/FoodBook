package ca.ualberta.cmput301w13t11.FoodBook;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.ualberta.cmput301w13t11.FoodBook.model.Ingredient;

/**
 * This array adapter is for ingredients. It shows ingredient components in a preferred order
 * and in a smaller font than the default (see xml layout ingredients_list_item for more info)
 * @author jaramill
 *
 */

public class MySimpleArrayAdapter extends ArrayAdapter<Ingredient>{
	private final Context context;
	private final ArrayList<Ingredient> values;

	public MySimpleArrayAdapter(Context context, ArrayList<Ingredient> values) {
		super(context, R.layout.ingredient_list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//LayoutInflater inflater = (LayoutInflater).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView = inflater.inflate(R.layout.ingredient_list_item, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.rowText);
		Ingredient ingred = values.get(position);
		textView.setText(ingred.getQuantity()+" "+ingred.getUnit()+" "+ingred.getName());

		return rowView;
	}
}