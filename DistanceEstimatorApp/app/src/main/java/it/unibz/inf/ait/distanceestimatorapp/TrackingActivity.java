package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.model.SportType;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
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
public class TrackingActivity extends Activity {

	public final static String EXTRA_MESSAGE = "it.unibz.inf.ait.distanceestimatorapp.MESSAGE";
	private Chronometer chronometer = null;
	private TextView kilometerView = null;
	private DatabaseManager dbHelper = null;
	private Button pauseButton = null;
	private int trainingId;
	private TrackingServiceReceiver receiver;
	private boolean wrongStartTime = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tracking);
		chronometer = (Chronometer) findViewById(R.id.tracking_chronometer);
		kilometerView = (TextView) findViewById(R.id.tracking_text);
		pauseButton = (Button) findViewById(R.id.pause_tracking_button);

		Intent intent = getIntent();
		int id = Integer.valueOf(intent
				.getStringExtra(MainActivity.EXTRA_MESSAGE));

		dbHelper = new DatabaseManager(this);
		SportType sportType = dbHelper.getSportType(id);

		Training training = new Training();
		training.setSportType(sportType);
		training.setStartTime(DateTime.now());
		training.setEndTime(DateTime.now());
		training.setDuration(new Duration(DateTime.now().getMillis()
				- DateTime.now().getMillis()));
		dbHelper.createTraining(training);
		kilometerView.setText(String.valueOf(training.getDistance() + " km"));
		trainingId = dbHelper.getHighestTraining();

		Intent serviceIntent = new Intent(this, TrackingService.class);
		startService(serviceIntent);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showToast("You need to enable your GPS!");
			Log.d("gps on?",
					"" + lm.isProviderEnabled(LocationManager.GPS_PROVIDER));
		}
		Log.d("locationCount", "" + dbHelper.getLocationCount());

		pauseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (pauseButton.getText().equals("Pause")) {
					chronometer.stop();
					stopService(new Intent(v.getContext(),
							TrackingService.class));
					pauseButton.setText("Resume");
				} else {
					String time = (String) chronometer.getText();
					String[] array = time.split(":");
					int milliseconds = Integer.valueOf(array[0]) * 60 * 1000
							+ Integer.valueOf(array[1]) * 1000;
					chronometer.setBase(SystemClock.elapsedRealtime()
							- milliseconds);
					chronometer.start();
					Intent serviceIntent = new Intent(TrackingActivity.this,
							TrackingService.class);
					startService(serviceIntent);
					pauseButton.setText("Pause");
				}

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter trackingFilter = new IntentFilter(
				TrackingService.EXTRA_MESSAGE);
		receiver = new TrackingServiceReceiver();
		registerReceiver(receiver, trackingFilter);
		Log.d("broadcast started", "onResume");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, TrackingService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_tracking, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
	}

	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public void saveTrack(View view) {
		unregisterReceiver(receiver);
		stopService(new Intent(this, TrackingService.class));
		chronometer.stop();

		dbHelper = new DatabaseManager(this);
		Training training = dbHelper.getTraining(trainingId);
		String time = (String) chronometer.getText();
		String[] array = time.split(":");
		Long milliseconds = Long.valueOf(array[1]) * 1000
				+ Long.valueOf(array[0]) * 1000 * 60;
		training.setDuration(new Duration(milliseconds));
		training.setEndTime(DateTime.now());
		training.compute(dbHelper);
		if (training.getDistance() > 0) {
			dbHelper.updateTraining(training);
		} else {
			dbHelper.deleteTraining(training);
			Toast.makeText(this,
					"Training hasn't been saved, because the distance is 0",
					Toast.LENGTH_SHORT).show();
		}
		Log.d("savedTrack", "track saved");
		Log.d("locationCount", "" + dbHelper.getLocationCount());

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	public class TrackingServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (wrongStartTime) {
				showToast("The tracking has been started!");
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();
				wrongStartTime = false;
			}
			Bundle bundle = intent.getExtras();

			kilometerView.setText(String.valueOf(bundle.get("kilometers")
					+ " km"));
			Log.d("receive?", "yes!!");
		}
	}

}
