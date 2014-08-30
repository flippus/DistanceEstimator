package it.unibz.inf.ait.distanceestimatorapp.model;

/**
 * @author Philipp Neugebauer
 */
public class SportType {

	private int id;
	private String name;
	private int trackingTime;

	public SportType(int id, String name, int trackingTime) {
		this.id = id;
		this.name = name;
		this.trackingTime = trackingTime;
	}

	public SportType(String name, int trackingTime) {
		this.name = name;
		this.trackingTime = trackingTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getTrackingTime() {
		return trackingTime;
	}

	public void setTrackingTime(int trackingTime) {
		this.trackingTime = trackingTime;
	}

	@Override
	public String toString() {
		return name;
	}

}
