package models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to generate nodes, which are specific points on the graph that represent the field.
 * @author Aidan Galbreath, Andrew McLees, Spencer Moore
 *
 */
public class Node {

	// Point where the node is located
	public Point point;
	// List of nodes adjacent to this node
	public List<Node> neighbors;
	// Unique Number for this node
	public int id;
	// Number used to allocate IDs
	private static int nextId = 0;
	
	public Node(Point point) {
		 id = nextId++;
		 this.point = point;
		 neighbors = new ArrayList<>();
	}
	
	public Node(double x, double y) {
		this(new Point(x, y));
	}
}
