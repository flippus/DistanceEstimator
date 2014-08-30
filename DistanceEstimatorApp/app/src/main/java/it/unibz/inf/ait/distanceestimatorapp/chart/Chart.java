package it.unibz.inf.ait.distanceestimatorapp.chart;

import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import android.location.Location;

/**
 * @author Philipp Neugebauer
 */
public class Chart {

	public double computeDistance(MyLocation startPoint, MyLocation endPoint) {
		Location startLocation = new Location("");
		startLocation.setLongitude(startPoint.getLongitude());
		startLocation.setLatitude(startPoint.getLatitude());
		Location endLocation = new Location("");
		endLocation.setLongitude(endPoint.getLongitude());
		endLocation.setLatitude(endPoint.getLatitude());
		return startLocation.distanceTo(endLocation) / 1000;
	}

	public long computeTime(MyLocation startPoint, MyLocation endPoint) {
		long difference = endPoint.getTime().getMillis()
				- startPoint.getTime().getMillis();
		return difference;
	}

}
