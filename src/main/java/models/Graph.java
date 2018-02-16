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
	
	/**
	 * 
	 *Naming Convention: Assume you are viewing the field from the side. Red is to your left, Blue is to your right. The far side of the field is the top,
	 *the near side of the field is the bottom. 
	 *R: Red
	 *B: Blue
	 *S: Switch
	 *SC: Scale
	 *
	 *						Top
	 *				-------------------------
	 *				|						|
	 *			R	|	RS		SC		BS	|	B
	 *				|						|
	 *				-------------------------
	 *						Bottom
	 * Example of use for declaration of nodes and the points between them.
	 * 
	 * Node redNode3 = new Node(0, 3);
	 * Node redNode32 = new Node(0, 2.5);
	 * 
	 * addEdge(redNode3, redNode32);
	 * 
	 * @param matchData
	 */
	public Graph(MatchData matchData) {
		nodes = new ArrayList<Node>();
		Node redStartTop = new Node(-24.53, 11.28);
		Node redStartCenter = new Node(-24.53, 0);
		Node redStartBottom = new Node(-24.53, -11.28);
		Node redSwitchSetupTop = new Node(-13, 11.28);
		Node redSwitchTop = new Node(-13, 6);
		Node redSwitchBottom = new Node(-13, -6);
		Node redSwitchSetupBottom = new Node(-13, -11.28);
		Node topScale = new Node(0, 11.28);
		Node bottomScale = new Node(0, -11.28);
		Node blueSwitchSetupTop = new Node(13, 11.28);
		Node blueSwitchTop = new Node(13, 6);
		Node blueSwitchBottom = new Node(13, -6);
		Node blueSwitchSetupBottom = new Node(13, -11.28);
		Node blueStartTop = new Node(24.53, 11.28);
		Node blueStartCenter = new Node(24.53, 0);
		Node blueStartBottom = new Node(24.53, -11.28);
		
		addEdge(redStartTop, redStartCenter);
		addEdge(redStartCenter, redStartBottom);
		addEdge(redStartBottom, redSwitchSetupBottom);
		addEdge(redStartTop, redSwitchSetupTop);
		addEdge(redSwitchSetupTop, redSwitchTop);
		addEdge(redSwitchSetupBottom, redSwitchBottom);
		addEdge(redSwitchSetupBottom, bottomScale);
		addEdge(redSwitchSetupTop, topScale);
		addEdge(topScale, blueSwitchSetupTop);
		addEdge(bottomScale, blueSwitchSetupBottom);
		addEdge(blueSwitchSetupTop, blueSwitchTop);
		addEdge(blueSwitchSetupBottom, blueSwitchBottom);
		addEdge(blueSwitchSetupBottom, blueStartBottom);
		addEdge(blueSwitchSetupTop, blueStartTop);
		addEdge(blueStartTop, blueStartCenter);
		addEdge(blueStartCenter, blueStartBottom);
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
