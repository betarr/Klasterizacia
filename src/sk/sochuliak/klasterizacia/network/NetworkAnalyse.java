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
	
	public Map<Double, List<Long>> getClusterDistribution() {
		Map<Double, List<Long>> result = new HashMap<Double, List<Long>>();
		
		this.network.updateClasterRatios();
		Map<Long, Double> clustersRatios = this.network.getNodesClusterRatios();
		
		List<Long> nodesIds = this.network.getNodesIds();
		
		for (Long nodeId : nodesIds) {
			Double nodeClusterRatio = clustersRatios.get(nodeId);
			if (result.get(nodeClusterRatio) == null) {
				result.put(nodeClusterRatio, new ArrayList<Long>());
			}
			result.get(nodeClusterRatio).add(nodeId);
		}
		return result;
	}
	
	public Map<Double, Double> getStandardizedClusterDistribution() {
		Map<Double, List<Long>> clusterDistribution = this.getClusterDistribution();
		
		double nodesCount = (double) this.network.getNodesIds().size();
		
		Map<Double, Double> result = new HashMap<Double, Double>();
		for (Double key : clusterDistribution.keySet()) {
			result.put(key, (double) clusterDistribution.get(key).size() / nodesCount);
		}
		return result;
	}

}
