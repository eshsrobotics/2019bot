package tests;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;

import models.Constants;
import models.Graph;
import models.Node;
import models.Point;
import models.Vector2;
import tests.FakeRobotModel.FakeTankDrive;

public class WaypointSimulator {


        /**
         * The speed of animations used during some tests.
         */
        private static final double FRAMES_PER_SECOND = 20.0;
        private static final double MILLISECONDS_PER_FRAME = 1000.0 / FRAMES_PER_SECOND;


        /**
         * Runs the Waypoint Simulator. Also prints something different.
         * @param args Command-line arguments (which we do not process right now.)
         * @throws Exception Forwarded unadorned if an individual test throws an exception.
         */
        public static void main(String[] args) throws Exception {


                //testAddWaypointsFromGraph();        // Superseded by testFalseRobotMovementAnimation
                //testFalseRobotMovementAnimation();  // Superseded by testInteractiveFakeRobotDrive
                testInteractiveFakeRobotDrive();
                testFindShortestPath();
                testTankDrive();
                System.out.println("something different");
        }

        /**
         * Tests the FakeRobotDrive by actually allowing the user to drive it.
         *
         * This test relies on the ability to read characters from the console
         * without the IO blocking until the ENTER key is pressed -- this is
         * known as "raw mode."  Java does not provide a means to do this
         * portably, so as a result, this function will not work on the
         * following non-Unix terminals:
         *
         * - cmd.exe
         * - powershell.exe
         *
         * If you're on Windows, use Cmder or Cygwin's MinTTY instead.
         */
        private static final void testInteractiveFakeRobotDrive() {
                final String[] rawModeCommand    = { "/bin/sh", "-c", "stty raw </dev/tty" };
                final String[] cookedModeCommand = { "/bin/sh", "-c", "stty cooked </dev/tty" };
                final int screenWidth = 120;
                final int screenHeight = 40;

                try {
                        // Enter raw mode.
                        Runtime.getRuntime().exec(rawModeCommand).waitFor();

                        Map map = new Map();
                        map.clearScreen();
                        FakeRobotModel robot = new FakeRobotModel();
                        robot.getDrive().setPosition(new Point(0, 0));

                        final double startTimeMilliseconds = System.currentTimeMillis();
                        final double totalSimulationTimeMilliseconds = 1000 * 100;
                        double leftSpeed = 0;
                        double rightSpeed = 0;
                        double elapsedTimeMilliseconds = System.currentTimeMillis() - startTimeMilliseconds;
                        int frames = 0;
                        boolean quit = false;

                        while (!quit && elapsedTimeMilliseconds < totalSimulationTimeMilliseconds) {

                                // Move the robot.
                                while (System.in.available() > 0) {
                                        final double increment = 0.05;
                                        int c = System.in.read();
                                        switch(c) {
                                                case 'a': case 'A': case '7':
                                                        leftSpeed += increment;
                                                        break;
                                                case 's': case 'S': case '9':
                                                        rightSpeed += increment;
                                                        break;
                                                case 'z': case 'Z': case '1':
                                                        leftSpeed -= increment;
                                                        break;
                                                case 'x': case 'X': case '3':
                                                        rightSpeed -= increment;
                                                        break;
                                                case 'q': case 'Q':
                                                        quit = true;
                                                        break;
                                        }
                                }

                                // Move the robot.
                                leftSpeed = Math.max(-1, Math.min(1, leftSpeed));   // Clamp to the interval [-1, 1].
                                rightSpeed = Math.max(-1, Math.min(1, rightSpeed)); // Clamp to the interval [-1, 1].
                                robot.getDrive().tankDrive(leftSpeed, rightSpeed);

                                // Don't leave the map.
                                Point position = robot.getDrive().getPosition();
                                double w = map.getWidth();
                                double h = map.getHeight();
                                position.x = Math.max(-w/2, Math.min(position.x, w/2));
                                position.y = Math.max(-h/2, Math.min(position.y, h/2));
                                robot.getDrive().setPosition(position);

                                map.setRobotPosition(robot.getDrive().getPosition());
                                map.setRobotVector(robot.getDrive().getDirection());

                                // Draw the current frame, then wait until it's time to
                                // draw the next one.
                                map.resetCursor();
                                map.draw(screenWidth, screenHeight);
                                frames++;
                                Thread.sleep((long)MILLISECONDS_PER_FRAME);

                                elapsedTimeMilliseconds = System.currentTimeMillis() - startTimeMilliseconds;
                                System.out.printf("Tank controls: A   S      7   9   Speeds:    %.2f/%.2f  Robot heading: %s     \r\n",
                                                  leftSpeed,
                                                  rightSpeed,
                                                  robot.getDrive().getDirection());
                                System.out.printf("  Press 'Q'    |---|  or  |---|   FPS:       %.1f       Robot position: %s    \r\n",
                                                  frames * 1000.0 / elapsedTimeMilliseconds,
                                                  robot.getDrive().getPosition());
                                System.out.printf("   to quit     Z   X      1   3   Time left: %.1f       Robot speed: %.4f    ",
                                                  (totalSimulationTimeMilliseconds - elapsedTimeMilliseconds)/1000,
                                                  robot.getDrive().getSpeed());

                        } // end (while the simulation is not complete)

                } catch (InterruptedException e) {
                        System.out.printf("Caught an %s while trying to put console in raw mode: \"%s\"\n", e.getClass().getCanonicalName());
                        e.printStackTrace();
                } catch (IOException e) {
                        System.out.printf("Caught an %s while trying to put console in raw mode: \"%s\"\n", e.getClass().getCanonicalName());
                        e.printStackTrace();
                } finally {

                        // Return to cooked mode.  It's important to do this before
                        // the program exits!
                        try {
                                Runtime.getRuntime().exec(cookedModeCommand).waitFor();
                        } catch (InterruptedException e) {
                                System.out.printf("Caught an %s while trying to return console to cooked mode: \"%s\"\n", e.getClass().getCanonicalName());
                                e.printStackTrace();
                        } catch (IOException e) {
                                System.out.printf("Caught an %s while trying to return console to cooked mode: \"%s\"\n", e.getClass().getCanonicalName());
                                e.printStackTrace();
                        }
                }
        }

        /***
         * Tests several different aspects of the autonomous code:
         *
         * 1) The ability to use the Map for animations;
         * 2) The ability to add waypoints from the default graph;
         * 3) The ability to set the robot position and display it correctly;
         * 4) The ability to set hte robot direction and display it correctly;
         * 5) Showing where the default waypoints are.
         *
         * @throws InterruptedException Thrown if the current thread is
         *                              interrupted during Thread.sleep()
         *                              (this is exceedingly unlikely.)
         */
        private static final void testFalseRobotMovementAnimation() throws InterruptedException {

                Map map = new Map();
                map.clearScreen();
                Graph graph = new Graph(null);
                map.AddWaypointsFromGraph(graph);
                final int screenWidth = 170;//120;
                final int screenHeight = 50;//40;

                final double robotData[][] = {
                        // X velocity (feet/frame), Y velocity(feet/frame), transition time stamp (milliseconds)
                        { -0.75,  0,    2000.000  },
                        {  0.45, -0.45, 3000.000  },
                        {  0.45,  0,    4000.000  },
                        {  0.45,  0.45, 5000.000  },
                        {  0,     0.45, 6000.000  },
                        { -0.45,  0.45, 7000.000  },
                        { -0.45,  0,    8000.000  },
                        { -0.45, -0.45, 9000.000  },
                        {  0,    -0.45, 10000.000 },
                        {  0.75,  0,    12000.000 },
                        {  0,     0,    13000.000 }, // Stop here.
                };
                Point currentPosition = new Point(0, 0);
                int robotDataIndex = 0;
                Vector2 currentVector = new Vector2(0, 0);

                final double totalSimulationTimeMilliseconds = robotData[robotData.length - 1][2];
                final double startTimeMilliseconds = System.currentTimeMillis();
                double elapsedTimeMilliseconds = System.currentTimeMillis() - startTimeMilliseconds;
                int frames = 0;

                while (elapsedTimeMilliseconds < totalSimulationTimeMilliseconds) {

                        // Move the robot.
                        currentVector = new Vector2(robotData[robotDataIndex][0], robotData[robotDataIndex][1]);
                        currentPosition = currentPosition.add(currentVector);
                        map.setRobotPosition(currentPosition);
                        map.setRobotVector(currentVector);

                        // Is it time to change the robot's direction?  (This
                        // is independent of the framerate.)
                        double nextMovementTimeMilliseconds = robotData[robotDataIndex][2];
                        if (elapsedTimeMilliseconds > nextMovementTimeMilliseconds) {
                                robotDataIndex++;
                        }

                        // Draw the current frame, then wait until it's time to
                        // draw the next one.
                        map.resetCursor();
                        map.draw(screenWidth, screenHeight);
                        frames++;
                        Thread.sleep((long)MILLISECONDS_PER_FRAME);

                        elapsedTimeMilliseconds = System.currentTimeMillis() - startTimeMilliseconds;
                        System.out.printf("%.2f frames per second", frames * 1000.0 / elapsedTimeMilliseconds);
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
                map.clearScreen();
                Graph graph = new Graph(null);
                map.AddWaypointsFromGraph(graph);
                // commented out to remove build error, will fix later
                //map.setRobotPosition(graph.getStartingPosition(Graph.RIGHT_POSITION, Graph.BLUE_ALLIANCE).point);
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

        /***
         * A test used to isolate the behavior of FakeTankDrive.tankDrive() by
         * itself.
         */
        public static final void testTankDrive() {
                // Test rotation.
                Point o = new Point(2, 2);
                Point p = new Point(5, 2);
                double theta = 45;
                Point q = p.rotatedAround(o, theta * Constants.DEGREES_TO_RADIANS);
                System.out.printf("Rotating %s %.3f degrees around %s yields %s.\n",
                                  p, theta, o, q);

                // Now test the drive.
                FakeTankDrive drive = new FakeRobotModel().new FakeTankDrive(Constants.ORIGIN, 1.5);
                drive.setDirection(new Vector2(0, 1));


                final double leftSpeed = 1.0;
                final double rightSpeed = -1.0;

                System.out.printf("Before moving the drive:\n  %s\n", drive.toString());
                drive.tankDrive(leftSpeed, rightSpeed);
                System.out.printf("After calling tankDrive(%.2f, %.2f):\n  %s\n", leftSpeed, rightSpeed, drive.toString());
        }
}
