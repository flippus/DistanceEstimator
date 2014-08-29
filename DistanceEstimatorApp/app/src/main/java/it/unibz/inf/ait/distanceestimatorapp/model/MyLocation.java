package it.unibz.inf.ait.distanceestimatorapp.model;

import org.joda.time.DateTime;

public class MyLocation {

	private int id;
	private double longitude;
	private double latitude;
	private double altitude;
	private int trainingId;
	private DateTime time;

	public MyLocation(int id, double longitude, double latitude,
			int trainingId, DateTime time, double altitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.trainingId = trainingId;
		this.time = time;
		this.altitude = altitude;
	}

	public MyLocation(double longitude, double latitude, int trainingId,
			DateTime time, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.trainingId = trainingId;
		this.time = time;
		this.altitude = altitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longtitude) {
		this.longitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public int getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(int trainingId) {
		this.trainingId = trainingId;
	}

	public DateTime getTime() {
		return time;
	}

	public String getTimeAsLong() {
		return String.valueOf(time.getMillis());
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

}
