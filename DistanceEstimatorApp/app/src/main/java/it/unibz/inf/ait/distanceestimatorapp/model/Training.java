package it.unibz.inf.ait.distanceestimatorapp.model;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import android.location.Location;
import android.util.Log;

/**
 * @author Philipp Neugebauer
 */
public class Training {
	private int id;
	private Duration duration;
	private double distance;
	private double altitude;
	private SportType sportType;
	private DateTime startTime;
	private DateTime endTime;
	private List<MyLocation> locationList;
	private NumberFormat n = NumberFormat.getInstance(Locale.ENGLISH);

	public Training() {
		n.setMaximumFractionDigits(2);
	}

	public Training(int id, Duration duration, double distance,
			double altitude, SportType sportType, DateTime startTime,
			DateTime endTime, List<MyLocation> locationList) {
		this.id = id;
		this.duration = duration;
		this.distance = distance;
		this.altitude = altitude;
		this.sportType = sportType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.locationList = locationList;
		n.setMaximumFractionDigits(2);
	}

	public void compute(DatabaseManager dbHelper) {
		locationList = dbHelper.getAllLocationsByTraining(this.getId());
		if (locationList.size() > 1) {
			computeAltitude();
			computeDistance();
			dbHelper.updateTraining(this);
		}
	}

	private void computeAltitude() {
		double sum = 0;
		for (int i = 0; i < locationList.size() - 1; i++) {
			double difference = 0;
			Log.d("training", "altitude i + 1 "
					+ locationList.get(i + 1).getAltitude());
			Log.d("training", "altitude i " + locationList.get(i).getAltitude());
			if ((difference = locationList.get(i + 1).getAltitude()
					- locationList.get(i).getAltitude()) > 0) {
				Log.d("training", "" + difference);
				sum += difference;
			}
		}
		Log.d("altitude sum ", "" + sum);
		altitude = Double.valueOf(n.format(sum).replace(",", ""));
		Log.d("altitude ", "" + altitude);
	}

	private void computeDistance() {
		double sum = 0;
		for (int i = 0; i < locationList.size() - 1; i++) {
			Location startLocation = new Location("");
			startLocation.setLongitude(locationList.get(i).getLongitude());
			startLocation.setLatitude(locationList.get(i).getLatitude());
			Location endLocation = new Location("");
			endLocation.setLongitude(locationList.get(i + 1).getLongitude());
			endLocation.setLatitude(locationList.get(i + 1).getLatitude());
			sum += startLocation.distanceTo(endLocation);
		}
		sum = sum / 1000;
		Log.d("training", "distance sum " + sum);
		distance = Double.valueOf(n.format(sum).replace(",", ""));
		Log.d("training", "distance " + distance);
	}

	@Override
	public String toString() {
		return new String(getStartTime() + "   " + sportType.getName());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SportType getSportType() {
		return sportType;
	}

	public void setSportType(SportType sportType) {
		this.sportType = sportType;
	}

	public String getStartTime() {
		return startTime.toString("dd.MM.yyyy HH:mm");
	}

	public String getStartTimeAsLong() {
		return String.valueOf(startTime.getMillis());
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime.toString("dd.MM.yyyy HH:mm");
	}

	public String getEndTimeAsLong() {
		return String.valueOf(endTime.getMillis());
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public List<MyLocation> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<MyLocation> trainingList) {
		this.locationList = trainingList;
	}

	public String getDuration() {
		Period durationPeriod = duration.toPeriod();
		NumberFormat n = NumberFormat.getInstance();
		n.setMinimumIntegerDigits(2);
		n.setMaximumIntegerDigits(2);
		return String.format("%s:%s:%s h", n.format(durationPeriod.getHours()),
				n.format(durationPeriod.getMinutes()),
				n.format(durationPeriod.getSeconds()));
	}

	public String getDurationAsLong() {
		return String.valueOf(duration.getMillis());
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public double getDistance() {
		return distance;
	}

	public double getAltitude() {
		return altitude;
	}

	public double getAverageKmh() {
		return Double.valueOf(n.format(
				distance / (Double.valueOf(duration.getMillis()) / 3600000))
				.replace(",", ""));
	}

	public double getAverageMinPerKm() {
		return Double.valueOf(n.format(
				(Double.valueOf(duration.getMillis()) / 60000) / distance)
				.replace(",", ""));
	}

	public double getAltitudePerKm() {
		double altitudePerDistance = altitude / distance;
		Log.d("altitudePerDistance ", "" + altitudePerDistance);
		return Double.valueOf(n.format(altitudePerDistance).replace(",", ""));
	}
}
