package sk.sochuliak.klasterizacia.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graph extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1833301929093016983L;

	public Graph(final GraphConfiguration config) {
		this.setTitle(config.getTitle());
		
		final JFreeChart chart = this.createChart(config);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(640, 480));
		this.setContentPane(chartPanel);
		
	}
	
	private XYDataset createDataset(final Map<String, List<double[]>> data) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		Set<String> seriesNames = data.keySet();
		for (String serieName : seriesNames) {
			List<double[]> points = data.get(serieName);
			if (points.size() > 0) {
				XYSeries series = new XYSeries(serieName);
				for (double[] point : points) {
					series.add(point[0], point[1]);
				}
				dataset.addSeries(series);
			}		
		}	
		return dataset;
	}

	private JFreeChart createChart(final GraphConfiguration config) {
		final XYDataset dataset = this.createDataset(config.getData());
		
		final JFreeChart chart = ChartFactory.createXYLineChart(
				config.getTitle(),
				config.getxAxisLabel(),
				config.getyAxisLabel(),
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
		
		chart.setBackgroundPaint(Color.WHITE);
		
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.WHITE);
		
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer);
		
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		return chart;
	}

	public static void showGraph(final GraphConfiguration config) {
		Graph graph = new Graph(config);
		graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graph.setSize(config.getGraphSize());
		graph.setLocationRelativeTo(null);
		graph.setVisible(true);
	}

}
