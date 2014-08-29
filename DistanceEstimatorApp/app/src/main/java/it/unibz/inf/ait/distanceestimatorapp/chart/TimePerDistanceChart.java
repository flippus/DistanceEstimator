package it.unibz.inf.ait.distanceestimatorapp.chart;

import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;
import it.unibz.inf.ait.distanceestimatorapp.model.Training;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class TimePerDistanceChart extends Chart {

	public GraphicalView compute(Context context, Training training,
			List<MyLocation> locationList) {
		int trainingKilometers = (int) (training.getDistance());
		double[][] values = new double[trainingKilometers][2];
		double distanceSum = 0;
		double timeSum = 0;
		NumberFormat n = NumberFormat.getInstance(Locale.ENGLISH);
		n.setMaximumFractionDigits(2);
		for (int i = 0, j = 1; i < locationList.size() - 1; i++) {
			distanceSum += computeDistance(locationList.get(i),
					locationList.get(i + 1));
			double timeDifference = Double.valueOf(computeTime(
					locationList.get(i), locationList.get(i + 1)))
					/ (1000 * 60);
			if (distanceSum > j) {
				if (distanceSum - j > 0) {
					double divider = 1 - (distanceSum - j);
					double time = timeDifference * divider;
					timeSum += time;
					values[j - 1][1] = Double.valueOf(n.format(timeSum)
							.replace(",", ""));
					timeSum = timeDifference / divider;
				} else {
					timeSum += timeDifference;
					values[j - 1][1] = timeSum;
					timeSum = 0;
				}
				j++;
			} else {
				timeSum += timeDifference;
			}
			if (j > trainingKilometers) {
				break;
			}
		}

		double max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i][1] > max) {
				max = values[i][1];
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
					.setDisplayChartValues(true);

		}
		renderer.setChartTitle("Time per distance");
		renderer.setXTitle("km");
		renderer.setYTitle("Time in min");
		renderer.setXAxisMin(1);
		renderer.setXAxisMax(trainingKilometers);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(max);
		renderer.setAxesColor(Color.LTGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setXLabels(15);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(true);

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		CategorySeries series = new CategorySeries("");
		for (int i = 0; i < values.length; i++) {
			series.add(values[i][1]);
		}
		dataset.addSeries(series.toXYSeries());

		GraphicalView view = ChartFactory.getBarChartView(context, dataset,
				renderer, Type.DEFAULT);
		return view;
	}
}
