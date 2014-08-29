package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.model.SportType;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectTrackingActivity extends Activity {

	public final static String EXTRA_MESSAGE = "it.unibz.inf.ait.distanceestimatorapp.MESSAGE";
	SportType sportType = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tracking);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		DatabaseManager db = new DatabaseManager(this);
		List<SportType> sportTypeList = db.getAllSportTypes();

		SportTypeAdapter arrayAdapter = new SportTypeAdapter(this,
				android.R.layout.simple_spinner_item, sportTypeList);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				sportType = (SportType) adapterView.getItemAtPosition(position);
				TextView editText = (TextView) findViewById(R.id.editText1);
				editText.setText(String.valueOf(sportType.getTrackingTime()),
						TextView.BufferType.EDITABLE);
			}

			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_select_tracking, menu);
		return true;
	}

	// will be executed, if the user wants to track a new training
	public void startTracking(View view) {
		TextView editText = (TextView) findViewById(R.id.editText1);
		sportType.setTrackingTime(Integer
				.valueOf(editText.getText().toString()));
		DatabaseManager db = new DatabaseManager(view.getContext());
		db.updateSportType(sportType);
		Intent intent = new Intent(this, TrackingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(EXTRA_MESSAGE, String.valueOf(sportType.getId()));
		startActivity(intent);
	}
}
