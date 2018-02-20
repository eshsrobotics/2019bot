package tests;
import java.util.HashMap;
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
                map.draw(40, 20);
                System.out.println("something different");
                testFindShortestPath();
        }
        private static void testFindShortestPath() {
        	Node a = new Node(0, 11); 
        	Node b = new Node(0, 12); 
        	Node c = new Node(0, 13); 
        	Node d = new Node(0, 14); 
        	Node e = new Node(0, 15); 
        	Node f = new Node(0, 16); 
        	Node g = new Node(0, 17);
        	
        	// Let's take those nodes and give them names.
        	HashMap<Integer, String> nameToIdTable = new HashMap<Integer, String>();
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
           	
           	LinkedList <Node> findPathResult = graph.findPath(a, d);
           	LinkedList <Node> findShortestPathResult = graph.findShortestPath(a, d);
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
