package it.unibz.inf.ait.distanceestimatorapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

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
@SuppressWarnings("deprecation")
public class TrainingActivity extends TabActivity {

	public final static String EXTRA_MESSAGE = "it.unibz.inf.ait.distanceestimatorapp.MESSAGE";
	private String id = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		Intent intent = getIntent();
		id = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		TabSpec firstTabSpec = tabHost.newTabSpec("data");
		TabSpec secondTabSpec = tabHost.newTabSpec("diagram");
		TabSpec thirdTabSpec = tabHost.newTabSpec("map");

		intent = new Intent(this, TrainingDataActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(EXTRA_MESSAGE, id);

		firstTabSpec
				.setIndicator(
						getResources().getString(
								R.string.title_activity_training_data))
				.setContent(intent);

		intent = new Intent(this, TrainingDiagrammActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(EXTRA_MESSAGE, id);

		secondTabSpec.setIndicator(
				getResources().getString(
						R.string.title_activity_training_diagramm)).setContent(
				intent);

		intent = new Intent(this, TrainingMapActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(EXTRA_MESSAGE, id);

		thirdTabSpec.setIndicator(
				getResources().getString(R.string.title_activity_training_map))
				.setContent(intent);

		tabHost.addTab(firstTabSpec);
		tabHost.addTab(secondTabSpec);
		tabHost.addTab(thirdTabSpec);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_training, menu);
		return true;
	}

}
