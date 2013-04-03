package sk.sochuliak.klasterizacia.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Network class represents network as set of nodes and set of edges.
 * 
 * @author Betarr
 *
 */
public class Network {

	/**
	 * Nodes are represented by ids.
	 */
	private List<Long> nodesIds = new ArrayList<Long>();
	
	
	private Map<Long, Double> nodesClusterRatios = new HashMap<Long, Double>();
	
	/**
	 * Edges are represented as pair of incidences nodes ids.
	 */
	private List<long[]> edges = new ArrayList<long[]>();
	
	public Network() {
	}
	
	public static Network buildNetwork(int edgesCount, long nodesCount) {
		Network network = new Network();
		
		for (long i = 0; i < nodesCount; i++) {
			List<Long> adjacentNodes = network.calculateAdjacentNodes(edgesCount);
			network.addNode(i);
			for (int j = 0; j < adjacentNodes.size(); j++) {
				network.addEdge(i, adjacentNodes.get(j));
			}
		}
		
		return network;
	}
	
	public void addNode(long nodeId) {
		if (this.nodesIds.contains(nodeId)) {
			System.err.println("Adding node: Node with id " + nodeId + " already exists.");
			return;
		}
		this.nodesIds.add(nodeId);			
	}
	
	public void addEdge(long nodeIdFrom, long nodeIdTo) {
		if (!this.nodesIds.contains(nodeIdFrom)) {
			System.err.println("Adding edge: Node with id " + nodeIdFrom + " does not exist");
			return;
		}
		if (!this.nodesIds.contains(nodeIdTo)) {
			System.err.println("Adding edge: Node with id " + nodeIdTo + " does not exist");
			return;
		}
		this.edges.add(new long[]{nodeIdFrom, nodeIdTo});
	}
	
	public List<Long> calculateAdjacentNodes(int edgesCount) {
		List<Long> adjacentNodes = new ArrayList<Long>();
		int nodesCount = this.nodesIds.size();
		
		if (nodesCount <= edgesCount) {
			for (Long nodeId : this.nodesIds) {
				adjacentNodes.add(nodeId);
			}
			return adjacentNodes;
		}
		
		this.updateClasterRatios();
		double sumOfClusterRatios = this.getSumOfClusterRatios();
		while (adjacentNodes.size() != edgesCount) {
			double randomDouble = Math.random() * sumOfClusterRatios;
			
			double areaCounter = 0d;
			for (int i = 0; i < this.nodesIds.size(); i++) {
				long candidateNode = this.nodesIds.get(i);
				double clusterRatio = this.nodesClusterRatios.get(candidateNode);
				double rangeFrom = areaCounter;
				double rangeTo = areaCounter + clusterRatio;
				if (randomDouble >= rangeFrom && randomDouble < rangeTo) {
					if (!adjacentNodes.contains(candidateNode)) {
						adjacentNodes.add(candidateNode);
						break;
					}
				}
				areaCounter = rangeTo;
			}
		}
		
		return adjacentNodes;
	}
	

	public void updateClasterRatios() {
		this.nodesClusterRatios.clear();
		for (int i = 0; i < this.nodesIds.size(); i++) {
			long nodeId = this.nodesIds.get(i);
			List<Long> adjacentNodes = this.getAdjacentNodesIds(nodeId);
			int numberOfExistingEdges = this.getNumberOfExistingEdgesBetweenNodes(adjacentNodes);
			int numberOfAllEdges = this.getNumberOfAllEdgesBetweenNodes(adjacentNodes);
			double clusterRatio = (double) numberOfExistingEdges / (double) numberOfAllEdges;
			this.nodesClusterRatios.put(nodeId, clusterRatio);
		}
	}
	
	private int getNumberOfExistingEdgesBetweenNodes(List<Long> nodesIds) {
		int numberOfEdges = 0;
		List<String> countedEdges = new ArrayList<String>();
		
		for (Long nodeId1 : nodesIds) {
			for (Long nodeId2 : nodesIds) {
				if (!nodeId1.equals(nodeId2)) {
					
					String edgeId = "";
					if (nodeId1 < nodeId2) {
						edgeId = nodeId1 + "|" + nodeId2;
					} else {
						edgeId = nodeId2 + "|" + nodeId1;
					}
					
					if (!countedEdges.contains(edgeId)) {
						if (this.isEdgeBetweenNodes(nodeId1, nodeId2)) {
							numberOfEdges++;
							countedEdges.add(edgeId);
						}
					}
				}
			}
		}
		return numberOfEdges;
	}
	
	private int getNumberOfAllEdgesBetweenNodes(List<Long> nodesIds) {
		int numberOfNodes = nodesIds.size();
		if (numberOfNodes == 0 || numberOfNodes == 1) {
			return 0;
		} else if (numberOfNodes == 2) {
			return 1;
		} else {
			return ((numberOfNodes-1)*numberOfNodes) / 2;
		}
	}
	
	private double getSumOfClusterRatios() {
		double sum = 0d;
		for (Long key : this.nodesClusterRatios.keySet()) {
			sum += this.nodesClusterRatios.get(key);
		}
		return sum;
	}

	public List<Long> getAdjacentNodesIds(long nodeId) {
		if (!this.nodesIds.contains(nodeId)) {
			System.err.println("Get nodes adjacent nodes ids: Node with id: " + nodeId + " does not exist");
			return null;
		}
		List<Long> adjacentNodes = new ArrayList<Long>();
		for (long[] edge : this.edges) {
			if (edge[0] == nodeId) {
				adjacentNodes.add(edge[1]);
			}
			if (edge[1] == nodeId) {
				adjacentNodes.add(edge[0]);
			}
		}
		return adjacentNodes;
	}
	
	public boolean isEdgeBetweenNodes(long node1, long node2) {
		for (long[] edge : this.edges) {
			if (edge[0] == node1) {
				if (edge[1] == node2) {
					return true;
				}
			} else if (edge[1] == node1) {
				if (edge[0] == node2) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getAdjacentNodesCount(long nodeId) {
		List<Long> adjacentNodesIds = this.getAdjacentNodesIds(nodeId);
		return adjacentNodesIds != null ? adjacentNodesIds.size() : 0;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("\n");
		sb.append("Nodes: ").append(this.nodesIds).append("\n");
		sb.append("Edges: ").append("\n");
		for (int i = 0; i < this.edges.size(); i++) {
			long[] edge = this.edges.get(i);
			sb.append("\t").append("[ ").append(edge[0]).append(" - ").append(edge[1]).append(" ]\n");
		}
		return sb.toString();
	}

	public List<Long> getNodesIds() {
		return nodesIds;
	}

	public List<long[]> getEdges() {
		return edges;
	}

	public Map<Long, Double> getNodesClusterRatios() {
		return nodesClusterRatios;
	}
}
