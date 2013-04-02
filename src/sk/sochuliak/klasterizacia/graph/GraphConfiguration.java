package sk.sochuliak.klasterizacia.graph;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphConfiguration {

	private String title = "Graph title";
	
	private Dimension graphSize = new Dimension(640, 480);
	
	private String xAxisLabel = "X axis label";
	
	private String yAxisLabel = "Y axis label";
	
	private Map<String, List<double[]>> data = new HashMap<String, List<double[]>>();
	
	public static GraphConfiguration createConfiguration() {
		return new GraphConfiguration();
	}
	
	public static GraphConfiguration createConfiguration(
			String title,
			Dimension graphSize,
			String xAxisLabel,
			String yAxisLabel,
			Map<String, List<double[]>> data) {
		return GraphConfiguration.createConfiguration()
				.setTitle(title)
				.setGraphSize(graphSize)
				.setxAxisLabel(xAxisLabel)
				.setyAxisLabel(yAxisLabel)
				.setData(data);
	}

	public String getTitle() {
		return this.title;
	}

	public GraphConfiguration setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public Dimension getGraphSize() {
		return this.graphSize;
	}
	
	public GraphConfiguration setGraphSize(Dimension graphSize) {
		this.graphSize = graphSize;
		return this;
	}

	public String getxAxisLabel() {
		return this.xAxisLabel;
	}

	public GraphConfiguration setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	public String getyAxisLabel() {
		return this.yAxisLabel;
	}

	public GraphConfiguration setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	public Map<String, List<double[]>> getData() {
		return this.data;
	}

	public GraphConfiguration setData(Map<String, List<double[]>> data) {
		this.data = data;
		return this;
	}
}
