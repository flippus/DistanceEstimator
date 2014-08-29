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
