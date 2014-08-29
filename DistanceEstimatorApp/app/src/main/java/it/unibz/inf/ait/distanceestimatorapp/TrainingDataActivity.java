package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.export.SendXml;
import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TrainingDataActivity extends Activity {

	private int id;
	private DatabaseManager db = null;

	@TargetApi(8)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_training_data);

		Intent intent = getIntent();
		id = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));

		db = new DatabaseManager(this);
		Training training = db.getTraining(id);

		List<String> list = new ArrayList<String>();
		list.add("Sport type:   " + training.getSportType().getName());
		list.add("Start time:   " + training.getStartTime());
		list.add("End time:   " + training.getEndTime());
		list.add("Duration:   " + training.getDuration());
		list.add("Distance:   " + training.getDistance() + " km");
		list.add("Average km/h:   " + training.getAverageKmh());
		list.add("Average Min/km:   " + training.getAverageMinPerKm());
		list.add("Altitude:   " + training.getAltitude());
		list.add("Altitude/km:   " + training.getAltitudePerKm());

		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		ListView lv = (ListView) findViewById(R.id.training_data_list);
		lv.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void deleteTraining(View view) {
		db = new DatabaseManager(this);
		Training training = db.getTraining(id);
		db.deleteTraining(training);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void exportTraining(View view) {
		db = new DatabaseManager(this);
		List<MyLocation> locationList = db.getAllLocationsByTraining(id);
		new ExportTask(this).execute(locationList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_training_data, menu);
		return true;
	}

	private class ExportTask extends AsyncTask<List<MyLocation>, Integer, Void> {
		private Context context;

		public ExportTask(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected Void doInBackground(List<MyLocation>... params) {

			List<MyLocation> locationList = params[0];
			SendXml export = new SendXml();
			int status = export.exportXml(locationList);
			Log.d("status", "" + status);
			publishProgress(status);

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... params) {
			Log.d("progress update?", "" + params[0]);
			if (params[0] != 200) {
				Toast.makeText(TrainingDataActivity.this,
						"Export has failed. Try it later again",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TrainingDataActivity.this,
						"Export was successful", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
