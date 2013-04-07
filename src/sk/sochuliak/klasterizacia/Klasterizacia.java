package sk.sochuliak.klasterizacia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.sochuliak.klasterizacia.graph.Graph;
import sk.sochuliak.klasterizacia.graph.GraphConfiguration;
import sk.sochuliak.klasterizacia.network.Network;
import sk.sochuliak.klasterizacia.network.NetworkAnalyse;
import sk.sochuliak.klasterizacia.network.NetworkUtils;

public class Klasterizacia {

	public static final int ITERATIONS = 1;
	public static final int NODES = 500;
	public static final int EDGES = 2;
	
	private Map<Integer, Double> averagedClusterDistribution;
	
	public static void main(String... args) {
		long startTime = new Date().getTime();
		Klasterizacia clustering = new Klasterizacia(Klasterizacia.ITERATIONS,
				Klasterizacia.NODES, Klasterizacia.EDGES);
		clustering.showClusterDistributionGraph();
		long endTime = new Date().getTime();
		long time = endTime - startTime;
		System.out.println("It tooks " + time + " ms.");
	}
	
	public Klasterizacia(int iterations, int nodes, int edges) {
		Map<Integer, Map<Integer, Double>> clusterDistributions = new HashMap<Integer, Map<Integer, Double>>();
		
		for (int i = 0; i < iterations; i++) {
			Network network = Network.buildNetwork(edges, nodes);
			NetworkAnalyse na = new NetworkAnalyse(network);
			clusterDistributions.put(i, na.getStandardizedClusterDistribution());
			System.out.println(na.getStandardizedClusterDistribution());
		}
		this.averagedClusterDistribution = NetworkUtils.getAveragedClusterDistribution(clusterDistributions);
		System.out.println(this.averagedClusterDistribution);
	}
	
	private void showClusterDistributionGraph() {
		Set<Integer> degrees = this.averagedClusterDistribution.keySet();
		List<Integer> degreesList = new ArrayList<Integer>(degrees);
		Collections.sort(degreesList);
		
		List<double[]> points = new ArrayList<double[]>();
		for (int i = 0; i < degreesList.size(); i++) {
			double x = (double) degreesList.get(i);
			double y = this.averagedClusterDistribution.get(degreesList.get(i));
			double logx = (x == 0) ? 0 : Math.log(x);
			double logy = (y == 0) ? 0 : Math.log(y);
			points.add(new double[]{logx, logy});
			//points.add(new double[]{x, y});
		}
		
		Map<String, List<double[]>> data = new HashMap<String, List<double[]>>();
		data.put("Distribucia stupna uzlov", points);
		
		GraphConfiguration config = GraphConfiguration.createConfiguration()
			.setTitle("Distribucia klasterizacie uzlov")
			.setxAxisLabel("Klasterizacia s")
			.setyAxisLabel("Normovany #uzlov s klasterizaciou s")
			.setData(data);
		Graph.showGraph(config);
	}
}
