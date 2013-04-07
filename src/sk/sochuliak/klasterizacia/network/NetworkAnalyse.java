package sk.sochuliak.klasterizacia.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkAnalyse {
	
	private Network network;

	public NetworkAnalyse(Network network) {
		this.network = network;
	}
	
	public Map<Integer, List<Long>> getDegreeDistribution() {
		Map<Integer, List<Long>> result = new HashMap<Integer, List<Long>>();
		
		List<Long> nodes = this.network.getNodesIds();
		
		for (Long nodeId : nodes) {
			int adjacentNodeCount = this.network.getAdjacentNodesCount(nodeId);
			if (result.get(adjacentNodeCount) == null) {
				result.put(adjacentNodeCount, new ArrayList<Long>());
			}
			result.get(adjacentNodeCount).add(nodeId);
		}
		
		return result;
	}
	
	public Map<Integer, Double> getStandardizedClusterDistribution() {
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		
		this.network.updateClasterRatios();
		Map<Long, Double> clustersRatios = this.network.getNodesClusterRatios();
		
		Map<Integer, List<Long>> degreeDistribution = this.getDegreeDistribution();
		
		for (Integer degree : degreeDistribution.keySet()) {
			List<Long> nodes = degreeDistribution.get(degree);
			
			double clusterSum = 0d;
			for (Long node : nodes) {
				clusterSum += clustersRatios.get(node);
			}
			double averageCluster = clusterSum / (double) nodes.size();
			if (averageCluster != 0d) {
				result.put(degree, clusterSum / (double) nodes.size());				
			}
		}
		return result;
	}

}
