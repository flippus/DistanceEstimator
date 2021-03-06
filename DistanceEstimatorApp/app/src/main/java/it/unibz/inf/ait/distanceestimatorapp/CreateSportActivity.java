package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.model.SportType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
public class CreateSportActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_sport);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_sport, menu);
		return true;
	}

	public void saveSportType(View view) {
		TextView sportNameText = (TextView) findViewById(R.id.sport_name);
		TextView trackingTimeText = (TextView) findViewById(R.id.tracking_time);
		if (sportNameText.getText().toString().equals("")) {
			Toast.makeText(this, "There has to be a name!", Toast.LENGTH_LONG)
					.show();
		} else {
			try {
				SportType sportType = new SportType(sportNameText.getText()
						.toString(), Integer.valueOf(trackingTimeText.getText()
						.toString()));
				DatabaseManager db = new DatabaseManager(view.getContext());
				db.createSportType(sportType);
				Intent intent = new Intent(view.getContext(),
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
			} catch (NumberFormatException e) {
				Toast.makeText(this, "Tracking_time must be a number!",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
