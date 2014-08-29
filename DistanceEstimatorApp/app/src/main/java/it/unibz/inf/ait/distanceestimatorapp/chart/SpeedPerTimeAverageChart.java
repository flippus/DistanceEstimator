package it.unibz.inf.ait.distanceestimatorapp.chart;

import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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

public class SpeedPerTimeAverageChart extends Chart {

	public GraphicalView compute(Context context, Training training,
			List<MyLocation> locationList) {
		long timeAsLong = Long.valueOf(training.getDurationAsLong());
		double time = Double.valueOf(timeAsLong) / 3600000;
		double timeSum = 0;
		double[][] values = new double[locationList.size() - 1][2];
		NumberFormat n = NumberFormat.getInstance(Locale.ENGLISH);
		n.setMaximumFractionDigits(2);
		for (int i = 0; i < locationList.size() - 1; i++) {
			double distanceDifference = computeDistance(locationList.get(i),
					locationList.get(i + 1));
			double timeDifference = Double.valueOf(computeTime(
					locationList.get(i), locationList.get(i + 1))) / 3600000;
			timeSum += timeDifference;
			values[i][0] = timeSum;
			values[i][1] = Double.valueOf(n.format(
					distanceDifference / timeDifference).replace(",", ""));
		}

		double min = 0;
		double max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i][1] > max) {
				max = values[i][1];
			} else if (values[i][1] < min) {
				min = values[i][1];
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
		renderer.setChartTitle("Speed per time");
		renderer.setXTitle("Time in h");
		renderer.setYTitle("Speed in km/h");
		renderer.setXAxisMin(values[0][0]);
		renderer.setXAxisMax(time);
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
			series.add(values[i][0], values[i][1]);
		}
		dataset.addSeries(series);

		GraphicalView view = ChartFactory.getLineChartView(context, dataset,
				renderer);
		return view;
	}
}
