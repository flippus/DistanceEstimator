package it.unibz.inf.ait.distanceestimatorapp;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import org.joda.time.DateTime;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Philipp Neugebauer
 */
public class TrackingService extends Service {

	public final static String EXTRA_MESSAGE = "it.unibz.inf.ait.distanceestimatorapp.MESSAGE";
	private LocationManager lm;
	private LocationListener gpsLocationListener;
	private LocationListener networkLocationListener;
	private DatabaseManager dbHelper;
	private Training training;

	private static long minDistanceMeters = 5;
	private static String networkLocationProvider = LocationManager.NETWORK_PROVIDER;
	private static String gpsLocationProvider = LocationManager.GPS_PROVIDER;

	private void startLoggerService(long minTimeMillis) {

		Log.d("service", "loggerservice started");

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gpsLocationListener = new MyLocationListener();
		networkLocationListener = new MyLocationListener();

		if (lm.isProviderEnabled(gpsLocationProvider)) {
			Log.d("service", "gps enabled and request updates");
			lm.requestLocationUpdates(gpsLocationProvider, minTimeMillis,
					minDistanceMeters, gpsLocationListener);
		}
		if (lm.isProviderEnabled(networkLocationProvider)) {
			Log.d("service", "network enabled and request updates");
			lm.requestLocationUpdates(networkLocationProvider, minTimeMillis,
					minDistanceMeters, networkLocationListener);
		}
	}

	private void shutdownLoggerService() {
		lm.removeUpdates(gpsLocationListener);
		lm.removeUpdates(networkLocationListener);
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			if (loc != null) {
				Log.d("service", "location != null");
				MyLocation location = new MyLocation(loc.getLongitude(),
						loc.getLatitude(), training.getId(), DateTime.now(),
						loc.getAltitude());
				Log.d("service", "location.getAltitude " + loc.getAltitude());
				Log.d("service", "location.getLatitude " + loc.getLatitude());
				Log.d("service", "location.getlongitude " + loc.getLongitude());
				dbHelper.createLocation(location);
				training.compute(dbHelper);
				Log.d("service", "locationCount " + dbHelper.getLocationCount());
				Log.d("service", "" + training.getDistance());

				Intent intent = new Intent(EXTRA_MESSAGE);
				intent.putExtra("kilometers", training.getDistance());
				sendBroadcast(intent);
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		dbHelper = new DatabaseManager(this);
		training = dbHelper.getTraining(dbHelper.getHighestTraining());
		Log.d("service", "service started");

		Notification notification = new Notification(R.drawable.ic_launcher,
				getText(R.string.ticker_text), System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, TrackingService.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this,
				getText(R.string.notification_title),
				getText(R.string.notification_message), pendingIntent);
		startForeground(1, notification);

		startLoggerService(training.getSportType().getTrackingTime());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
		shutdownLoggerService();
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		TrackingService getService() {
			return TrackingService.this;
		}
	}

}
