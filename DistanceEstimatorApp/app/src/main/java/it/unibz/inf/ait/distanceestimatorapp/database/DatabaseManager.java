package it.unibz.inf.ait.distanceestimatorapp.database;

import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.SportType;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Philipp Neugebauer
 */
public class DatabaseManager {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "RunningApp";
	private Context context = null;
	private DatabaseHelper dbHelper = null;
	private SQLiteDatabase db = null;

	public static final String TABLE_TRAININGS = "trainings";
	public static final String TRAININGS_COLUMN_ID = "_id";
	public static final String TRAININGS_COLUMN_DURATION = "duration";
	public static final String TRAININGS_COLUMN_DISTANCE = "distance";
	public static final String TRAININGS_COLUMN_ALTITUDE = "altitude";
	public static final String TRAININGS_COLUMN_SPORT_TYPE_ID = "sport_type_id";
	public static final String TRAININGS_COLUMN_START_TIME = "start_time";
	public static final String TRAININGS_COLUMN_END_TIME = "end_time";

	public static final String TABLE_SPORT_TYPES = "sport_types";
	public static final String SPORT_TYPES_COLUMN_ID = "_id";
	public static final String SPORT_TYPES_COLUMN_NAME = "name";
	public static final String SPORT_TYPES_COLUMN_TRACKING_TIME = "tracking_time";

	public static final String TABLE_LOCATIONS = "locations";
	public static final String LOCATIONS_COLUMN_ID = "_id";
	public static final String LOCATIONS_COLUMN_LONGTITUDE = "longtitude";
	public static final String LOCATIONS_COLUMN_LATITUDE = "latitude";
	public static final String LOCATIONS_COLUMN_TRAINING_ID = "training_id";
	public static final String LOCATIONS_COLUMN_TIME = "time";
	public static final String LOCATIONS_COLUMN_ALTITUDE = "altitude";

	public static class DatabaseHelper extends SQLiteOpenHelper {

		private static DatabaseHelper dbHelperInstance = null;

		private static final String CREATE_TRAINING_TABLE = "create table "
				+ TABLE_TRAININGS + "(" + TRAININGS_COLUMN_ID
				+ " integer primary key autoincrement, "
				+ TRAININGS_COLUMN_DURATION + " text, "
				+ TRAININGS_COLUMN_DISTANCE + " text, "
				+ TRAININGS_COLUMN_ALTITUDE + " text, "
				+ TRAININGS_COLUMN_START_TIME + " text not null, "
				+ TRAININGS_COLUMN_END_TIME + " text, "
				+ TRAININGS_COLUMN_SPORT_TYPE_ID
				+ " INTEGER NOT NULL, FOREIGN KEY("
				+ TRAININGS_COLUMN_SPORT_TYPE_ID + ") REFERENCES "
				+ TABLE_SPORT_TYPES + "(_id) ON DELETE CASCADE);";

		private static final String CREATE_SPORT_TYPE_TABLE = "create table "
				+ TABLE_SPORT_TYPES + "(" + SPORT_TYPES_COLUMN_ID
				+ " integer primary key autoincrement, "
				+ SPORT_TYPES_COLUMN_NAME + " text not null, "
				+ SPORT_TYPES_COLUMN_TRACKING_TIME + " text not null);";

		private static final String CREATE_LOCATION_TABLE = "create table "
				+ TABLE_LOCATIONS + "(" + LOCATIONS_COLUMN_ID
				+ " integer primary key autoincrement, "
				+ LOCATIONS_COLUMN_LONGTITUDE + " text not null, "
				+ LOCATIONS_COLUMN_LATITUDE + " text not null, "
				+ LOCATIONS_COLUMN_TIME + "  text not null, "
				+ LOCATIONS_COLUMN_ALTITUDE + "  text not null, "
				+ LOCATIONS_COLUMN_TRAINING_ID
				+ " INTEGER NOT NULL, FOREIGN KEY("
				+ LOCATIONS_COLUMN_TRAINING_ID + ") REFERENCES "
				+ TABLE_TRAININGS + "(_id) ON DELETE CASCADE);";

		private static final String SQL_DELETE_TRAINING_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_TRAININGS;

		private static final String SQL_DELETE_SPORT_TYPE_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_SPORT_TYPES;

		private static final String SQL_DELETE_LOCATION_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_LOCATIONS;

		public static DatabaseHelper getInstance(Context ctx) {
			if (dbHelperInstance == null) {
				dbHelperInstance = new DatabaseHelper(
						ctx.getApplicationContext());
			}
			return dbHelperInstance;
		}

		private DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_SPORT_TYPE_TABLE);
			db.execSQL(CREATE_TRAINING_TABLE);
			db.execSQL(CREATE_LOCATION_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_TRAINING_TABLE);
			db.execSQL(SQL_DELETE_SPORT_TYPE_TABLE);
			db.execSQL(SQL_DELETE_LOCATION_TABLE);
			onCreate(db);
		}

	}

	public DatabaseManager(Context ctx) {
		this.context = ctx;
	}

	private DatabaseManager open() throws SQLException {
		dbHelper = DatabaseHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();

		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys = ON;");
		}

		return this;
	}

	private void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		dbHelper.close();
	}

	public void createTraining(Training training) {
		open();

		ContentValues values = new ContentValues();
		values.put(TRAININGS_COLUMN_DURATION, training.getDurationAsLong());
		values.put(TRAININGS_COLUMN_DISTANCE, training.getDistance());
		values.put(TRAININGS_COLUMN_ALTITUDE, training.getAltitude());
		values.put(TRAININGS_COLUMN_START_TIME, training.getStartTimeAsLong());
		values.put(TRAININGS_COLUMN_END_TIME, training.getEndTimeAsLong());
		values.put(TRAININGS_COLUMN_SPORT_TYPE_ID, training.getSportType()
				.getId());

		db.insert(TABLE_TRAININGS, null, values);

		close();
	}

	public void updateTraining(Training training) {
		open();

		ContentValues values = new ContentValues();
		values.put(TRAININGS_COLUMN_DURATION, training.getDurationAsLong());
		values.put(TRAININGS_COLUMN_DISTANCE, training.getDistance());
		values.put(TRAININGS_COLUMN_ALTITUDE, training.getAltitude());
		values.put(TRAININGS_COLUMN_START_TIME, training.getStartTimeAsLong());
		values.put(TRAININGS_COLUMN_END_TIME, training.getEndTimeAsLong());
		values.put(TRAININGS_COLUMN_SPORT_TYPE_ID, training.getSportType()
				.getId());

		db.update(TABLE_TRAININGS, values, TRAININGS_COLUMN_ID + "=?",
				new String[] { String.valueOf(training.getId()) });

		close();
	}

	public Training getTraining(int id) {
		open();

		Cursor cursor = db.query(TABLE_TRAININGS, new String[] {
				TRAININGS_COLUMN_ID, TRAININGS_COLUMN_DURATION,
				TRAININGS_COLUMN_DISTANCE, TRAININGS_COLUMN_ALTITUDE,
				TRAININGS_COLUMN_START_TIME, TRAININGS_COLUMN_END_TIME,
				TRAININGS_COLUMN_SPORT_TYPE_ID }, TRAININGS_COLUMN_ID + "= ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		Training training = null;

		if (cursor != null) {
			cursor.moveToFirst();

			training = buildTraining(cursor);
		}
		cursor.close();
		close();
		return training;
	}

	public int getLocationCount() {
		open();

		String query = "SELECT  COUNT(*) FROM locations";
		Cursor cursor = db.rawQuery(query, null);

		int count = -1;
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(0);
		}

		cursor.close();
		close();

		return count;
	}

	public List<MyLocation> getLocations() {
		open();

		String query = "SELECT  * FROM locations";
		Cursor cursor = db.rawQuery(query, null);

		List<MyLocation> locationList = new ArrayList<MyLocation>();

		if (cursor.moveToFirst()) {

			MyLocation location = new MyLocation(Integer.parseInt(cursor
					.getString(0)), Double.valueOf(cursor.getString(1)),
					Double.valueOf(cursor.getString(2)),
					Integer.parseInt(cursor.getString(5)), new DateTime(
							Long.valueOf(cursor.getString(3))),
					Double.valueOf(cursor.getString(4)));

			locationList.add(location);

			while (cursor.moveToNext()) {

				location = new MyLocation(
						Integer.parseInt(cursor.getString(0)),
						Double.valueOf(cursor.getString(1)),
						Double.valueOf(cursor.getString(2)),
						Integer.parseInt(cursor.getString(5)), new DateTime(
								Long.valueOf(cursor.getString(3))),
						Double.valueOf(cursor.getString(4)));

				locationList.add(location);
			}
		}
		cursor.close();
		close();

		return locationList;
	}

	public int getHighestTraining() {
		open();

		String query = "SELECT MAX(_id) AS max_training_id FROM "
				+ TABLE_TRAININGS;

		Cursor cursor = db.rawQuery(query, null);

		Training training = null;

		if (cursor != null) {
			cursor.moveToFirst();
			int id = cursor.getInt(0);
			training = getTraining(id);
		}

		cursor.close();
		close();

		return training.getId();
	}

	public List<Training> getAllTrainings() {
		List<Training> trainingList = new ArrayList<Training>();
		String selectQuery = "SELECT  * FROM " + TABLE_TRAININGS + " ORDER BY "
				+ TRAININGS_COLUMN_ID + " DESC";

		open();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {

			trainingList.add(buildTraining(cursor));

			while (cursor.moveToNext()) {

				trainingList.add(buildTraining(cursor));
			}
		}

		cursor.close();
		close();
		return trainingList;
	}

	public List<Training> getAllTrainingsBySportType(int sportTypeId) {
		List<Training> trainingList = new ArrayList<Training>();

		open();

		Cursor cursor = db.query(TABLE_TRAININGS, new String[] {
				TRAININGS_COLUMN_ID, TRAININGS_COLUMN_DURATION,
				TRAININGS_COLUMN_DISTANCE, TRAININGS_COLUMN_ALTITUDE,
				TRAININGS_COLUMN_START_TIME, TRAININGS_COLUMN_END_TIME,
				TRAININGS_COLUMN_SPORT_TYPE_ID },
				TRAININGS_COLUMN_SPORT_TYPE_ID + "= ?",
				new String[] { String.valueOf(sportTypeId) }, null, null, null,
				null);

		if (cursor.moveToFirst()) {

			trainingList.add(buildTraining(cursor));

			while (cursor.moveToNext()) {

				trainingList.add(buildTraining(cursor));
			}
		}
		cursor.close();
		close();
		return trainingList;
	}

	public void deleteTraining(Training training) {
		open();

		db.delete(TABLE_TRAININGS, TRAININGS_COLUMN_ID + " LIKE ?",
				new String[] { String.valueOf(training.getId()) });
		close();

	}

	public void createSportType(SportType sportType) {
		open();

		ContentValues values = new ContentValues();
		values.put(SPORT_TYPES_COLUMN_NAME, sportType.getName());
		values.put(SPORT_TYPES_COLUMN_TRACKING_TIME,
				sportType.getTrackingTime());

		db.insert(TABLE_SPORT_TYPES, null, values);
		close();
	}

	public void updateSportType(SportType sportType) {
		open();

		ContentValues values = new ContentValues();
		values.put(SPORT_TYPES_COLUMN_NAME, sportType.getName());
		values.put(SPORT_TYPES_COLUMN_TRACKING_TIME,
				sportType.getTrackingTime());

		db.update(TABLE_SPORT_TYPES, values, SPORT_TYPES_COLUMN_ID + " = ?",
				new String[] { String.valueOf(sportType.getId()) });

		close();
	}

	public SportType getSportType(int id) {

		open();

		Cursor cursor = db.query(TABLE_SPORT_TYPES, new String[] {
				SPORT_TYPES_COLUMN_ID, SPORT_TYPES_COLUMN_NAME,
				SPORT_TYPES_COLUMN_TRACKING_TIME }, SPORT_TYPES_COLUMN_ID
				+ "= ?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		SportType sportType = null;

		if (cursor != null) {
			cursor.moveToFirst();

			sportType = new SportType(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), Integer.parseInt(cursor.getString(2)));
		}

		cursor.close();
		close();

		return sportType;
	}

	public List<SportType> getAllSportTypes() {
		List<SportType> sportTypeList = new ArrayList<SportType>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPORT_TYPES;

		open();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {

			SportType sportType = new SportType(Integer.parseInt(cursor
					.getString(0)), cursor.getString(1),
					Integer.parseInt(cursor.getString(2)));

			sportTypeList.add(sportType);

			while (cursor.moveToNext()) {

				sportType = new SportType(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(1), Integer.parseInt(cursor
								.getString(2)));

				sportTypeList.add(sportType);
			}
		}
		cursor.close();
		close();
		return sportTypeList;
	}

	public void createLocation(MyLocation location) {
		open();

		ContentValues values = new ContentValues();
		values.put(LOCATIONS_COLUMN_LONGTITUDE,
				String.valueOf(location.getLongitude()));
		values.put(LOCATIONS_COLUMN_LATITUDE,
				String.valueOf(location.getLatitude()));
		values.put(LOCATIONS_COLUMN_TIME, location.getTimeAsLong());
		values.put(LOCATIONS_COLUMN_ALTITUDE,
				String.valueOf(location.getAltitude()));
		values.put(LOCATIONS_COLUMN_TRAINING_ID, location.getTrainingId());

		db.insert(TABLE_LOCATIONS, null, values);
		close();
	}

	public List<MyLocation> getAllLocationsByTraining(int trainingId) {
		List<MyLocation> locationList = new ArrayList<MyLocation>();

		open();

		Cursor cursor = db.query(TABLE_LOCATIONS, new String[] {
				LOCATIONS_COLUMN_ID, LOCATIONS_COLUMN_LONGTITUDE,
				LOCATIONS_COLUMN_LATITUDE, LOCATIONS_COLUMN_TIME,
				LOCATIONS_COLUMN_ALTITUDE, LOCATIONS_COLUMN_TRAINING_ID },
				LOCATIONS_COLUMN_TRAINING_ID + "= ?",
				new String[] { String.valueOf(trainingId) }, null, null,
				LOCATIONS_COLUMN_TIME + " ASC", null);

		if (cursor.moveToFirst()) {

			MyLocation location = new MyLocation(Integer.parseInt(cursor
					.getString(0)), Double.valueOf(cursor.getString(1)),
					Double.valueOf(cursor.getString(2)),
					Integer.parseInt(cursor.getString(5)), new DateTime(
							Long.valueOf(cursor.getString(3))),
					Double.valueOf(cursor.getString(4)));

			locationList.add(location);

			while (cursor.moveToNext()) {

				location = new MyLocation(
						Integer.parseInt(cursor.getString(0)),
						Double.valueOf(cursor.getString(1)),
						Double.valueOf(cursor.getString(2)),
						Integer.parseInt(cursor.getString(5)), new DateTime(
								Long.valueOf(cursor.getString(3))),
						Double.valueOf(cursor.getString(4)));

				locationList.add(location);
			}
		}
		cursor.close();
		close();
		return locationList;
	}

	private Training buildTraining(Cursor cursor) {
		int trainingId = Integer.parseInt(cursor.getString(0));

		Training training = new Training(trainingId, new Duration(
				Long.valueOf(cursor.getString(1))), Double.valueOf(cursor
				.getString(2)), Double.valueOf(cursor.getString(3)),
				getSportType(Integer.valueOf(cursor.getString(6))),
				new DateTime(Long.valueOf(cursor.getString(4))), new DateTime(
						Long.valueOf(cursor.getString(5))),
				getAllLocationsByTraining(trainingId));

		return training;
	}
}
