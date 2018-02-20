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
        
        private Point robotPosition;

        private class ScreenCharacter {
                public ScreenCharacter() {
                        color = BLUE;
                        c = 'x';
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
                        "\033[1;30m", // Gray.
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
        
        public void AddWaypointsFromGraph(Node current, HashSet<Integer> visitedNodeIds) {
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

                // The real screen dimensions have to account for borders.
                int realScreenWidth = screenWidth - 2;
                int realScreenHeight = screenHeight - 2;

                // Draw the borders first.
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

                // TODO: Draw waypoints.
                
                for (Node waypoint: waypoints) {
                	
                	/**
                	 * creates the screen coordinates based off of the virtual field coordinates
                	 */
                	Point screenCoordinate = new Point(waypoint.point.x * scale, waypoint.point.y * scale);
                	drawCharacter(virtualBuffer, screenWidth, (int) Math.round(screenCoordinate.x), (int) Math.round(screenCoordinate.y), GREEN, '$');
                	
                }

                // TODO: Draw "you" (the robot and its direction vector.)
                
                drawCharacter(virtualBuffer, screenWidth, (int) Math.round(robotPosition.x * scale), (int) Math.round(robotPosition.y * scale), 1, '&');

                // Render the whole buffer.

                for (int offset = 0, row = 0; row < screenHeight; ++row) {
                        for (int column = 0; column < screenWidth; ++column, ++offset) {
                                if (useColors) {
                                        int color = virtualBuffer.get(offset).color;
                                        System.out.print(colorSequences[virtualBuffer.get(offset).color]);
                                        System.out.print(virtualBuffer.get(offset).c);
                                } else {
                                        System.out.print(virtualBuffer.get(offset).c);
                                }
                        }
                        System.out.print('\n');
                }
        }

        private void drawCharacter(ArrayList<ScreenCharacter> virtualBuffer, int maxX, int x, int y, int color, char c) {
                int offset = maxX * y + x;
                if (offset < 0 || offset > virtualBuffer.size()) {
                        return;
                }
                virtualBuffer.set(offset, new ScreenCharacter(color, c));
        }
}
