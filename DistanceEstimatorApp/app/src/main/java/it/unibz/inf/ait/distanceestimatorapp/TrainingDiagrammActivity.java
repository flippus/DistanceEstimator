package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.chart.AltitudeChart;
import it.unibz.inf.ait.distanceestimatorapp.chart.SpeedPerTimeAverageChart;
import it.unibz.inf.ait.distanceestimatorapp.chart.TimePerDistanceChart;
import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.util.List;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
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
public class TrainingDiagrammActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training_diagramm);

		Intent intent = getIntent();
		int id = Integer.valueOf(intent
				.getStringExtra(MainActivity.EXTRA_MESSAGE));
		DatabaseManager db = new DatabaseManager(this);
		Training training = db.getTraining(id);
		List<MyLocation> locationList = db.getAllLocationsByTraining(id);

		if (locationList.size() > 0) {
			LinearLayout speedPerTimeLayout = (LinearLayout) findViewById(R.id.chart_1);
			SpeedPerTimeAverageChart speedPerTimeChart = new SpeedPerTimeAverageChart();
			GraphicalView speedPerTimeView = speedPerTimeChart.compute(this,
					training, locationList);
			speedPerTimeLayout.addView(speedPerTimeView);

			LinearLayout timePerDistanceLayout = (LinearLayout) findViewById(R.id.chart_2);
			TimePerDistanceChart timePerDistanceChart = new TimePerDistanceChart();
			GraphicalView timePerDistanceView = timePerDistanceChart.compute(
					this, training, locationList);
			timePerDistanceLayout.addView(timePerDistanceView);

			LinearLayout altitudeLayout = (LinearLayout) findViewById(R.id.chart_3);
			AltitudeChart altitudeChart = new AltitudeChart();
			GraphicalView altitudeView = altitudeChart.compute(this, training,
					locationList);
			altitudeLayout.addView(altitudeView);
		} else {
			showToast("There is no data available to create the diagrams!");
		}
	}

	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_training_diagramm, menu);
		return true;
	}
}
