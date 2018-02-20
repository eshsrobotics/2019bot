package tests;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;

import models.Graph;
import models.Node;
import models.Vector2;
import models.Point;

public class WaypointSimulator {

        /**
         * Runs the Waypoint Simulator. Also prints Hello World!
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {

                System.out.println("something different");
                testFalseRobotMovementAnimation();
                testAddWaypointsFromGraph();
                testFindShortestPath();
        }


        private static final void testFalseRobotMovementAnimation() throws Exception {

                Map map = new Map();
                map.clearScreen();
                Graph graph = new Graph(null);
                map.AddWaypointsFromGraph(graph);
                final int screenWidth = 120;
                final int screenHeight = 40;

                double robotData[][] = {
                        // X velocity, Y velocity; transition time stamp (milliseconds)
                        {  0.25, -0.25, 1000.000 },
                        {  0.25,  0,    2000.000 },
                        {  0.25,  0.25, 3000.000 },
                        {  0,     0.25, 4000.000 },
                        { -0.25,  0.25, 5000.000 },
                        { -0.25,  0,    6000.000 },
                        { -0.25, -0.25, 7000.000 },
                        {  0,    -0.25, 8000.000 },
                        {  0,     0,    9000.000 }, // Stop here.
                };
                Point currentPosition = new Point(0, 0);
                int robotDataIndex = 0;
                Vector2 currentVector = new Vector2(0, 0);

                final double FRAMES_PER_SECOND = 20.0;
                final double MILLISECONDS_PER_FRAME = 1000.0 / FRAMES_PER_SECOND;
                final double totalSimulationTimeMilliseconds = robotData[robotData.length - 1][2];
                final double startTimeMilliseconds = System.currentTimeMillis();
                double elapsedTimeMilliseconds = System.currentTimeMillis() - startTimeMilliseconds;

                while (elapsedTimeMilliseconds < totalSimulationTimeMilliseconds) {

                        // Move the robot.
                        currentVector = new Vector2(robotData[robotDataIndex][0], robotData[robotDataIndex][1]);
                        currentPosition = currentPosition.add(currentVector);
                        map.setRobotPosition(currentPosition);
                        map.setRobotVector(currentVector);

                        // Is it time to change the robot's direction?  (This is independent of
                        // the framerate.)
                                double nextMovementTimeMilliseconds = robotData[robotDataIndex][2];
                        if (elapsedTimeMilliseconds > nextMovementTimeMilliseconds) {
                                robotDataIndex++;
                        }

                        // Draw the current frame, then wait until it's time to draw the next one.
                        map.resetCursor();
                        map.draw(screenWidth, screenHeight);
                        Thread.sleep((long)MILLISECONDS_PER_FRAME);

                        elapsedTimeMilliseconds = System.currentTimeMillis() - startTimeMilliseconds;
                }
        }

        /**
         * Tests the Map.addWaypointsFromGraph() function.
         */
        private static final void testAddWaypointsFromFakeGraph() {
                Map map = new Map(200, 100);
                HashMap<Integer, String> nameToIdTable = new HashMap<Integer, String>();
                Graph graph = createSampleGraph(nameToIdTable);
                map.AddWaypointsFromGraph(graph);
                map.draw(120, 40);
        }

        public static final void testAddWaypointsFromGraph() throws Exception {
                Map map = new Map();
                Graph graph = new Graph(null);
                map.AddWaypointsFromGraph(graph);
                map.setRobotPosition(graph.getStartingPosition(Graph.RIGHT_POSITION, Graph.BLUE_ALLIANCE).point);
                map.setRobotVector(new Vector2(300, 200));
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
                Node h = new Node(10.6, 17.2);

                nameToIdTable.put(a.id, "A");
                nameToIdTable.put(b.id, "B");
                nameToIdTable.put(c.id, "C");
                nameToIdTable.put(d.id, "D");
                nameToIdTable.put(e.id, "E");
                nameToIdTable.put(f.id, "F");
                nameToIdTable.put(g.id, "G");
                nameToIdTable.put(h.id, "H");

                Graph graph = new Graph(null);
                graph.currentNode = h; // This disconnects all the real nodes in the graph!
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
                graph.addEdge(h, c);

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

                LinkedList <Node> findShortestPathResult = graph.findShortestPath(graph.currentNode, graph.target);
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
