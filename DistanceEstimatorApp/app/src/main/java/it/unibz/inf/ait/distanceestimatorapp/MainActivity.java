package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.model.SportType;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "it.unibz.inf.ait.distanceestimatorapp.MESSAGE";
	private boolean selected = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// just for creating some data for testing
		// this.deleteDatabase("RunningApp");

		DatabaseManager db = new DatabaseManager(this);
		if (db.getAllSportTypes().size() <= 0) {
			SportType sportType = new SportType("Joggen", 10);
			db.createSportType(sportType);
		}

		selected = false;

		List<SportType> sportTypeList = new ArrayList<SportType>();
		sportTypeList.add(new SportType("All", 5));

		for (SportType type : db.getAllSportTypes()) {
			sportTypeList.add(type);
		}

		Spinner spinner = (Spinner) findViewById(R.id.sport_type_spinner);

		SportTypeAdapter arrayAdapter = new SportTypeAdapter(this,
				android.R.layout.simple_spinner_item, sportTypeList);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				if (selected) {
					SportType sportType = (SportType) adapterView
							.getItemAtPosition(position);
					DatabaseManager db = new DatabaseManager(view.getContext());
					List<Training> trainingList = null;
					if (sportType.getName().equals("All")) {
						trainingList = db.getAllTrainings();
					} else {
						trainingList = db.getAllTrainingsBySportType(sportType
								.getId());
					}
					createTrainingList(trainingList);
				}
				selected = true;
			}

			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});

		List<Training> trainingFromDb = db.getAllTrainings();
		createTrainingList(trainingFromDb);
	}

	private void createTrainingList(List<Training> trainingList) {
		ListAdapter adapter = new ArrayAdapter<Training>(this,
				android.R.layout.simple_list_item_1, trainingList);
		ListView lv = (ListView) findViewById(R.id.training_list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				ListView lv = (ListView) findViewById(R.id.training_list);
				Training training = (Training) lv.getItemAtPosition((int) id);
				openTraining(String.valueOf(training.getId()));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// will be executed, if the user wants to track a new training
	public void startTracking(View view) {
		Intent intent = new Intent(this, SelectTrackingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	// will be executed, if the user wants to see a tracked training
	public void openTraining(String selectedTraining) {
		Intent intent = new Intent(this, TrainingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(EXTRA_MESSAGE, selectedTraining);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, CreateSportActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
