package models;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1759.robot.MatchData;

public class Graph {

	public List<Node> nodes;
	public int target; 
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
	
	
}
