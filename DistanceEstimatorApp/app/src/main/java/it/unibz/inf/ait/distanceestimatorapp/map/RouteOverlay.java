package it.unibz.inf.ait.distanceestimatorapp.map;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {

	private List<GeoPoint> geoPointList = new ArrayList<GeoPoint>();

	public void addGeoPoint(GeoPoint geoPoint) {
		geoPointList.add(geoPoint);
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		if (shadow == false) {
			Projection projection = mapView.getProjection();
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLUE);
			paint.setStrokeWidth(5);
			paint.setAlpha(120);

			Point startPoint = new Point();
			Point endPoint = new Point();

			for (int i = 0; i < geoPointList.size() - 1; i++) {
				projection.toPixels(geoPointList.get(i), startPoint);
				projection.toPixels(geoPointList.get(i + 1), endPoint);

				canvas.drawLine(startPoint.x, startPoint.y, endPoint.x,
						endPoint.y, paint);
			}

		}
		return super.draw(canvas, mapView, shadow, when);
	}
}
