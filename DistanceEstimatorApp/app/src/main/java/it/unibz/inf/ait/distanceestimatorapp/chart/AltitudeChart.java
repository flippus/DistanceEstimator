package it.unibz.inf.ait.distanceestimatorapp.chart;

import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * @author Philipp Neugebauer
 */
public class AltitudeChart extends Chart {

	public GraphicalView compute(Context context, Training training,
			List<MyLocation> locationList) {
		double[][] values = new double[locationList.size()][2];
		double distanceSum = 0;
		for (int i = 0; i < locationList.size(); i++) {
			values[i][0] = locationList.get(i).getAltitude();
			values[i][1] = distanceSum;
			if (i < locationList.size() - 1) {
				distanceSum += computeDistance(locationList.get(i),
						locationList.get(i + 1));
			}
		}

		double min = 0;
		double max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i][0] > max) {
				max = values[i][0];
			} else if (values[i][0] < min) {
				min = values[i][0];
			}
		}

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.BLUE);
		r.setPointStyle(PointStyle.CIRCLE);
		renderer.addSeriesRenderer(r);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		renderer.setChartTitle("Altitude progress");
		renderer.setXTitle("km");
		renderer.setYTitle("altitude in m");
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(training.getDistance());
		renderer.setYAxisMin(min);
		renderer.setYAxisMax(max);
		renderer.setAxesColor(Color.LTGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(true);

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		XYSeries series = new XYSeries("");
		for (int i = 0; i < values.length; i++) {
			series.add(values[i][1], values[i][0]);
		}
		dataset.addSeries(series);

		GraphicalView view = ChartFactory.getLineChartView(context, dataset,
				renderer);
		return view;
	}

}
