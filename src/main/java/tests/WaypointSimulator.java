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

        /**
         * Tests the Map.addWaypointsFRomGraph() function.
         */
        private static final void testAddWaypointsFromGraph() {
                Map map = new Map(200, 100);
                HashMap<Integer, String> nameToIdTable = new HashMap<Integer, String>();
                Graph graph = createSampleGraph(nameToIdTable);
                HashSet<Integer> visitedNodeIds = new HashSet<Integer>();
                map.AddWaypointsFromGraph(graph.currentNode, visitedNodeIds);
                map.draw(120, 40);
        }

        /***
         * Creates a little test graph that a few of our test funcitons can
         * play around with.
         *
         * This is a virtual representation of the graph we've been using on
         * the board to discuss the intricacies of graph theory.
         *
         * @param nameToIdTable Nodes don't have names--at least, not by
         *                       themselves.  We use this table to map node IDs
         *                       to names...just in case they're needed.
         * @return A graph with a handful of nodes in it.  The nameToIdTable
         *          will also be updated.
         */
        private static final Graph createSampleGraph(HashMap<Integer, String> nameToIdTable) {
                Node a = new Node(90, 0);
                Node b = new Node(80, -30);
                Node c = new Node(40, -20);
                Node d = new Node(-20, 17);
                Node e = new Node(40, 35);
                Node f = new Node(10, 5);
                Node g = new Node(-21, 30);

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

        /***
         * Tests the ability to Graph.findShortestPath() to do what it is
         * supposed to do.
         */
        private static void testFindShortestPath() {

                // Let's take those nodes and give them names.
                HashMap<Integer, String> nameToIdTable = new HashMap<Integer, String>();
                Graph graph = createSampleGraph(nameToIdTable);

                LinkedList <Node> findPathResult = graph.findPath(graph.currentNode, graph.target);
                LinkedList <Node> findShortestPathResult = graph.findShortestPath(graph.currentNode, graph.target);
                System.out.printf("find path result = %s\n", printPath(findPathResult, nameToIdTable));
                System.out.printf("findShortestPath() result = %s\n", printPath(findShortestPathResult, nameToIdTable));
        }

        /***
         * Prints a linked list of nodes...by name.
         *
         * @param path The linked list of paths -- that is, the return value
         *              from findShortestPath().
         * @param nameToIdTable A table mapping node IDs to names.
         * @return A string that looks like "Name1 > Name2 > name3 > Name4".
         */
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
