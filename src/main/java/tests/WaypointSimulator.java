package tests;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.AbstractMap;

import models.Graph;
import models.Node;

public class WaypointSimulator {

        /**
         * Runs the Waypoint Simulator. Also prints Hello World!
         * @param args
         */
        public static void main(String[] args) {

                Map map = new Map();
                Node h = new Node(40, 10);
                map.AddWaypoint(h);
                map.draw(40, 20);
                System.out.println("something different");
                testFindShortestPath();
                testAddWaypointsFromGraph();
        }
        
        private static final void testAddWaypointsFromGraph() {
        	Map foo = new Map(59, 8);
        	HashMap<Integer, String> nameToIdTable = new HashMap<Integer, String>();
        	Graph graph = createSampleGraph(nameToIdTable);
        	HashSet<Integer> bar = new HashSet<Integer>();
        	foo.AddWaypointsFromGraph(graph.currentNode, bar);
        	foo.draw(40, 50);
        }
        
        private static final Graph createSampleGraph(HashMap<Integer, String> nameToIdTable) {
        	Node a = new Node(2, 1); 
        	Node b = new Node(18, 2); 
        	Node c = new Node(28, 5); 
        	Node d = new Node(9, 6); 
        	Node e = new Node(31, 3); 
        	Node f = new Node(52, 4); 
        	Node g = new Node(42, 7);
        	
        	nameToIdTable.put(a.id, "A");
        	nameToIdTable.put(b.id, "B");
        	nameToIdTable.put(c.id, "C");
        	nameToIdTable.put(d.id, "D");
        	nameToIdTable.put(e.id, "E");
        	nameToIdTable.put(f.id, "F");
        	nameToIdTable.put(g.id, "G");
        	
        	Graph graph = new Graph(null);
        	graph.currentNode = a; 
        	graph.target = d;
        	graph.addEdge(a, e);
           	graph.addEdge(a, b);
           	graph.addEdge(e, c);
           	graph.addEdge(c, d);
           	graph.addEdge(d, b);
           	graph.addEdge(d, g);
           	graph.addEdge(g, b);
           	graph.addEdge(f, g);
           	graph.addEdge(e, d);
           	
           	return graph;
        }
        
        private static void testFindShortestPath() {
    
        	// Let's take those nodes and give them names.
        	HashMap<Integer, String> nameToIdTable = new HashMap<Integer, String>();
        	Graph graph = createSampleGraph(nameToIdTable);
           	
           	LinkedList <Node> findPathResult = graph.findPath(graph.currentNode, graph.target);
           	LinkedList <Node> findShortestPathResult = graph.findShortestPath(graph.currentNode, graph.target);
           	System.out.printf("find path result = %s\n", printPath(findPathResult, nameToIdTable));
           	System.out.printf("findShortestPath() result = %s\n", printPath(findShortestPathResult, nameToIdTable));
        }
        
        private static String printPath(LinkedList <Node> path, AbstractMap<Integer, String> nameToIdTable) {
        	StringBuilder result = new StringBuilder();
        	for (Node n: path) {
        		result.append(nameToIdTable.get(n.id));
        		if (n.id != path.peekLast().id) {
        			// If this is not the last element, print a connecting arrow.
        			result.append(" > ");
        		}
        		
        	}
        	return result.toString();
        }

}
