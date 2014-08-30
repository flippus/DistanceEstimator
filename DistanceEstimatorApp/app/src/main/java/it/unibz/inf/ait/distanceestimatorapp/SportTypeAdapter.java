package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.model.SportType;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Philipp Neugebauer
 */
public class SportTypeAdapter extends ArrayAdapter<SportType> {

	private Context context;
	private List<SportType> sportTypes;

	public SportTypeAdapter(Context context, int textViewResourceId,
			List<SportType> values) {
		super(context, textViewResourceId, values);
		this.context = context;
		this.sportTypes = values;
	}

	@Override
	public int getCount() {
		return sportTypes.size();
	}

	@Override
	public SportType getItem(int position) {
		return sportTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(sportTypes.get(position).getName());

		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(sportTypes.get(position).getName());

		return label;
	}
}
