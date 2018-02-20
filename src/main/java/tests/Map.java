package tests;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;


import models.Graph;
import models.Node;
import models.Point;
/**
 * Draws the game field in beautiful ASCII.
 *
 * I would like to use floating-point coordinates for everything,
 * @author uakotaobi
 *
 */
public class Map {

        /***
         * Should we use ANSI SGR color codes?
         */
        private boolean useColors = true;

        private static final int BLACK = 0;
        private static final int RED = 1;
        private static final int GREEN = 2;
        private static final int ORANGE = 3;
        private static final int BLUE = 4;
        private static final int PINK = 5;
        private static final int TEAL = 6;
        private static final int WHITE = 7;
        private static final int GRAY = 8;
        private static final int BRIGHT_RED = 9;
        private static final int BRIGHT_GREEN = 10;
        private static final int YELLOW = 11;
        private static final int BRIGHT_BLUE = 12;
        private static final int BRIGHT_MAGENTA = 13;
        private static final int BRIGHT_CYAN = 14;
        private static final int BRIGHT_WHITE = 15;

        private Point robotPosition;

        private class ScreenCharacter {
                public ScreenCharacter() {
                        color = BLUE;
                        c = '.';
                }
                public ScreenCharacter(int color_, char c_) {
                        color = color_;
                        c = c_;
                }
                public int color;
                public char c;
        }

        /***
         * This maps the 16 standard ANSI colors to color code strings.
         *
         *  You can generate the sequences yourself using tput.
         */
        private String colorSequences[] = {
                        // Dull colors.
                        "\033[0;30m", // Black
                        "\033[0;31m", // Blue
                        "\033[0;32m", // Green
                        "\033[0;33m", // Teal
                        "\033[0;34m", // Red
                        "\033[0;35m", // Magenta
                        "\033[0;36m", // Dull Brown/Orange
                        "\033[0;37m", // White
                        // Bright colors.
                        "\033[1;30m", // Gray
                        "\033[1;31m", // Bright Red
                        "\033[1;32m", // Bright Green
                        "\033[1;33m", // Bright Cyan
                        "\033[1;34m", // Bright Blue
                        "\033[1;35m", // Bright Magenta
                        "\033[1;36m", // Yellow
                        "\033[1;37m", // Bright White
        };

        private double width, height;
        private ArrayList<Node> waypoints;

        public Map() {
                this(56.0, 27.0);
        }
        /**
         * "Waypoints" are the positions that our robot must visit during
         * autonomous mode.  For the simulation, we will display the positions of
         * all the actual waypoints that the actual robot will visit -- but
         * rendered in ASCII.
         *
         * This function adds these individual waypoints.  They don't move or
         * change once added.
         *
         * @param waypoint The waypoint Node to register.
         */
        public void AddWaypoint(Node waypoint) {
                waypoints.add(waypoint);

        }
        /**
         * 
         * Got a Graph object?  Did you fill it with waypoints already?  Well,
         * we can draw them.
         * @param graph " The graph whoose nodes need to be drawn.
         */
        public void AddWaypointsFromGraph(Graph graph) {
        	AddWaypointsFromGraph(graph.currentNode, new HashSet<Integer>());
        }

        /***
         * Underlying implementation of the public function.
         *
         * @param current The waypoint from which to begin a recursive walk
         *                 through the graph.
         * @param visitedNodeIds A bag of all the IDs for all the nodes that
         *                        recursion has touched so far.  It should
         *                        start out empty.
         */
        private void AddWaypointsFromGraph(Node current, HashSet<Integer> visitedNodeIds) {
                /**
                 * This says if the current starting node is already visited, then skip all of the remaining code.
                 */
                if (visitedNodeIds.contains(current.id)) {
                        return;
                }
                /**
                 * then marks as visited
                 */
                visitedNodeIds.add(current.id);
                AddWaypoint(current);

                java.util.List<Node> neighbors = current.neighbors;
                        for (int i = 0; i < neighbors.size(); i++) {
                                Node neighbor = neighbors.get(i);
                                AddWaypointsFromGraph(neighbor, visitedNodeIds);
                        }

        }

        public Map(double widthInFeet, double heightInFeet) {
                width = widthInFeet;
                height = heightInFeet;
                waypoints = new ArrayList<>();
                setRobotPosition(new Point(10.6, 17.2));
        }

        public Point setRobotPosition(Point robotPosition) {
                this.robotPosition = robotPosition;
                return robotPosition;
        }

        // Renders the map and everything in it to standard output.
        public void draw(int screenWidth, int screenHeight) {

                // Scale so that we take up the full width or height that we are
                // allotted, preserving aspect ratio.
                double smallestPhysicalDimension = Math.min(screenWidth, screenHeight);
                double smallestVirtualDimension = Math.min(width,  height);

                // Got an 80x25 screen:
                // - A 200x75 board would have a scaleFactor of 25/75 = 1/3.
                //   That would map the board to 66.6x25.
                // - A 40x90 board would have a scaleFactor of 25/40 = 5/8.
                //   That would map the board to 25x56.25.
                double scale = smallestPhysicalDimension / smallestVirtualDimension;

                // But:
                // - A pathological 10,000x1000 board.in the above regime would have a
                //   scaleFactor of 25/1000 = 1/40, mapping the board to 250x25 --
                //   still pathological!
                //
                //   The real scale factor should have been 80/10000 = 1/125, mapping
                //   the board to 80x0.2.
                double largestVirtualDimension = Math.max(width,  height);
                double largestPhysicalDimension = Math.max(screenWidth, screenHeight);
                if (scale * largestVirtualDimension > largestPhysicalDimension) {
                        scale = largestPhysicalDimension / largestVirtualDimension;
                }

                // Allocate a virtual screen buffer to hold the stuff we're drawing.
                ArrayList<ScreenCharacter> virtualBuffer = new ArrayList<ScreenCharacter>();
                for (int i = 0; i < screenWidth * screenHeight; ++i) {
                        virtualBuffer.add(new ScreenCharacter());
                }

                // Draw the borders first.
                //
                // The borders are drawn on the edges, so it's possible for
                // things to be drawn on top of them. 
                for (int row = 0; row < screenHeight; ++row) {
                        // Left and right walls.
                        drawCharacter(virtualBuffer, screenWidth, 0,               row, WHITE, '|');
                        drawCharacter(virtualBuffer, screenWidth, screenWidth - 1, row, WHITE, '|');
                }
                for (int column = 0; column < screenWidth; ++column) {
                        drawCharacter(virtualBuffer, screenWidth, column, 0,                WHITE, '-');
                        drawCharacter(virtualBuffer, screenWidth, column, screenHeight - 1, WHITE, '-');
                }
                drawCharacter(virtualBuffer, screenWidth, 0,                0,                WHITE, '/');
                drawCharacter(virtualBuffer, screenWidth, 0,                screenHeight - 1, WHITE, '\\');
                drawCharacter(virtualBuffer, screenWidth, screenWidth - 1, 0,                 WHITE, '\\');
                drawCharacter(virtualBuffer, screenWidth, screenWidth - 1, screenHeight - 1,  WHITE, '/');
                
                // Draw the waypoints the robot will be traveling to.

                for (Node waypoint: waypoints) {
                        Point screenCoordinate = virtualCoordinateToScreenCoordinate(waypoint.point, screenWidth, screenHeight, scale);
                        drawCharacter(virtualBuffer, screenWidth, (int) Math.round(screenCoordinate.x), (int) Math.round(screenCoordinate.y), BRIGHT_GREEN, '$');
                }

                // Draw "you" (that is, draw the robot and its direction vector.)
                
                Point robotScreenPosition = virtualCoordinateToScreenCoordinate(robotPosition, screenWidth, screenHeight, scale);
                drawCharacter(virtualBuffer, screenWidth, (int) Math.round(robotScreenPosition.x), (int) Math.round(robotScreenPosition.y), BRIGHT_RED, '&');

                // Render the whole buffer.

                for (int offset = 0, row = 0; row < screenHeight; ++row) {
                        for (int column = 0; column < screenWidth; ++column, ++offset) {
                                if (useColors) {
                                        System.out.print(colorSequences[virtualBuffer.get(offset).color]);
                                        System.out.print(virtualBuffer.get(offset).c);
                                } else {
                                        System.out.print(virtualBuffer.get(offset).c);
                                }
                        }
                        System.out.print('\n');
                }
        }

        /***
         * Helper function for draw().  Converts an (x, y) coordinate in feet
         * on the virtual game arena to an integer screen coordinate.
         *
         * (0,0) always maps to the center of the screen; the rest depends on
         * the scaleFactor.
         *
         * @param virtualCoordinateInFeet The position of something on the
         *                                 virtual game arena, like a robot or
         *                                 a waypoint.
         * @param screenWidth             The width of the screen in
         *                                 characters.  Only needed to that we
         *                                 can place (0ft, 0ft) at the center
         *                                 of the screen.
         * @param screenHeight                     The height of the screen in
         *                                 characters.  Needed for the same
         *                                 reasons as screenWidth.
         * @param scaleFactor             A multiplier that converts virtual
         *                                 coordinates to screen coordinates.
         * @return                        A screen position.
         */
        private static Point virtualCoordinateToScreenCoordinate(Point virtualCoordinateInFeet, int screenWidth, int screenHeight, double scaleFactor) {
        		//We have inverted y because of how screen coordinates work. With screen coordinates, positive y goes toward the bottom of the screen.
        		//Virtual coordinates, which is human standard coordinates, have y going up.
        		//Because of this, we must invert y so that it behaves the way we expect it to.
        	    Point screenPosition = new Point(virtualCoordinateInFeet.x * scaleFactor + screenWidth/2,
                                                 -virtualCoordinateInFeet.y * scaleFactor + screenHeight/2);
                return screenPosition;
        }

        private void drawCharacter(ArrayList<ScreenCharacter> virtualBuffer, int maxX, int x, int y, int color, char c) {
                int offset = maxX * y + x;
                if (offset < 0 || offset > virtualBuffer.size()) {
                        return;
                }
                virtualBuffer.set(offset, new ScreenCharacter(color, c));
        }
}

