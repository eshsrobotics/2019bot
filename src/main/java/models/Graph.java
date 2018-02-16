package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.usfirst.frc.team1759.robot.MatchData;

public class Graph {

	public List<Node> nodes;
	public Node target;
	public Node currentNode;

	public Graph(MatchData matchData) {
		nodes = new ArrayList<Node>();

		Node redNode3 = new Node(0, 3);
		Node redNode32 = new Node(0, 2.5);

		addEdge(redNode3, redNode32);
	}

	public void addEdge(Node node1, Node node2) {
		node1.neighbors.add(node2);
		node2.neighbors.add(node1);
	}

	public static LinkedList<Node> findPath(Node start, Node target) {
		Map<Node, Node> previousNode = new HashMap<>();

		Queue<Node> toVisit = new LinkedList<>();
		toVisit.add(start);

		while (!toVisit.isEmpty()) {
			Node currentNode = toVisit.poll();

			for (Node neighbor : currentNode.neighbors) {
				if (previousNode.containsKey(neighbor)) {
					continue;
				}
				previousNode.put(neighbor, currentNode);
				toVisit.add(neighbor);
			}
		}
		
		LinkedList<Node> path = new LinkedList<>();
		
		Node currentNode = target;
		while (currentNode != start) {
			path.add(currentNode);
			currentNode = previousNode.get(currentNode);
		}
		Collections.reverse(path);

		return path;
	}

}
