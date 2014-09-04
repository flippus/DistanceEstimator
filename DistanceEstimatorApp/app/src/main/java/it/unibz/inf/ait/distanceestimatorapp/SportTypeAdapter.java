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
 * Copyright 2014 Philipp Neugebauer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
