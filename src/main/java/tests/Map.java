package tests;

import java.util.ArrayList;
import java.util.HashSet;

import models.Constants;
import models.Graph;
import models.Node;
import models.Point;
import models.Vector2;
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

        // We support the 16 standard ANSI colors by default.  We could support
        // up to 256, but that would require me to care more than I currently
        // do.
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

        /**
         * The "arrow character" to use for each octant, starting
         * counterclockwise with octant 0 (between the positive X and positive
         * Y axes, with -22.5 < theta <= 22.5 degrees.)
         */
        private static final String DIRECTION_ARROWS_PER_OCTANT = "-\\|/-\\|/";

        /***
         * The Map can plot a point in space in a special color and pretend
         * it's "the robot."
         */
        private Point robotPosition;

        /***
         * The Map can indicate "the robot's" current direction vector --
         * somewhat curdely, though, given that this is ASCII.
         */
        private Vector2 robotVector;

        /**
         * Octant will return a value between 0 and 7. 0 is anything
         * between 0 and 45 degrees. The octants move in 45 degree
         * increments counterclockwise until it reaches 360 degrees
         * and wraps around to 0.
         */
        private int octant;

        /***
         * If set to true by enableRawMode(), every newline emitted during
         * draw() will be preceded by a carriage return.
         */
        private boolean addCarriageReturns;

        /***
         * A ScreenCharacter is a struct with a color code and a character.
         * @author uakotaobi
         */
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
         *  You can sample the sequences yourself from an ANSI terminal running
         *  the Bash shell using:
         *
         *    echo "$(tput setaf N)Testing"
         *
         *  for the dull colors, and
         *
         *    echo "$(tput bold)$(tput setaf N)Testing brightly"
         *
         *  for the bright ones.  N ranges from 0 to 7 here.
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

        /**
         * The width and height of the game field in *feet*.  (FRC likes
         * imperial measurements.)
         */
        private double width, height;

        /**
         * The list of waypoints we've been asked to render during draw().
         */
        private ArrayList<Node> waypoints;

        /***
         * Constructs a virtual field as wide and high as the real thing.  It
         * will start with no waypoints (either add them one by one yourself or
         * add a Graph with the correct waypoints in it.)
         */
        public Map() {
                this(56.0, 27.0);
        }

        /***
         * Constructs a Map with arbitrary dimensions and an empty set of
         * waypoints.
         *
         * @param widthInFeet *Virtual* width of the game arena, in feet.
         * @param heightInFeet *Virtual* height of the game area, in feet.
         */
        public Map(double widthInFeet, double heightInFeet) {
                width = widthInFeet;
                height = heightInFeet;
                waypoints = new ArrayList<>();
                setRobotPosition(new Point(10.6, 17.2));
        }

        /**
         * Returns the virtual width of the map, in feet.
         */
        public double getWidth() {
                return width;
        }

        /**
         * Returns the virtual height of the map, in feet.
         */
        public double getHeight() {
                return height;
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
        public void addWaypoint(Node waypoint) {
                waypoints.add(waypoint);
        }

        /**
         * Got a Graph object?  Did you fill it with waypoints already?  Well,
         * we can draw them.

         * @param graph: The graph whose nodes need to be drawn.
         */
        public void addWaypointsFromGraph(Graph graph) {
                addWaypointsFromGraphRecursive(graph.currentNode, new HashSet<Integer>());
        }

        /***
         * Underlying implementation of the public AddWaypointsFromGraph
         * function.
         *
         * @param current The waypoint from which to begin a recursive walk
         *                 through the graph.
         * @param visitedNodeIds A bag of all the IDs for all the nodes that
         *                        recursion has touched so far.  It should
         *                        start out empty.
         */
        private void addWaypointsFromGraphRecursive(Node current, HashSet<Integer> visitedNodeIds) {
                // If the current node is already visited, there's nothing more
                // to do.
                if (visitedNodeIds.contains(current.id)) {
                        return;
                }

                // The current node is officially visited.
                visitedNodeIds.add(current.id);
                addWaypoint(current);

                // Walk to the neighbors in turn and add them if they haven't
                // been visited.
                java.util.List<Node> neighbors = current.neighbors;
                for (int i = 0; i < neighbors.size(); i++) {
                        Node neighbor = neighbors.get(i);
                        addWaypointsFromGraphRecursive(neighbor, visitedNodeIds);
                }
        }

        /**
         * Allows you to change the position of the robot marker that we render
         * on the map.
         *
         * @param robotPosition The virtual position of the robot, in feet.
         * @return Aforesaid position, jsut in case you need it for something.
         */
        public Point setRobotPosition(Point robotPosition) {
                this.robotPosition = robotPosition;
                return robotPosition;
        }

        /***
         * Changes the direction in which our imaginary robot points.
         *
         * @param robotVector The new direction to face.
         * @return The normalized version of the vector we were given, just in
         *         case you need it for something.
         */
        public Vector2 setRobotVector(Vector2 robotVector) {
                this.robotVector = robotVector.normalized();

                // The angle between the X axis and the normalized vector will
                // be a value between 0 and 2π radians.
                double thetaRadians = -Math.atan2(this.robotVector.y, this.robotVector.x);
                if (thetaRadians < 0) {
                    // Shift the range of atan2() from [-π, π] to [0, 2π].
                    thetaRadians += 2 * Math.PI;
                }

                // Right now, anything between 0 degrees and 45 degrees is
                // quadrant 0 (which maps to "-".)
                //
                // Let's displace the quadrants by 45/2 degrees to make this
                // look more intuitive.

                thetaRadians = (thetaRadians + Math.PI / 8) % (2 * Math.PI); // Java does floating-point modulus, a la fmod().  Who knew?

                double normalizedTheta = thetaRadians / (2 * Math.PI);
                // System.out.printf("%s: %.2fπ radians      ", this.robotVector.toString(), thetaRadians/Math.PI);
                this.octant = (int) Math.floor(normalizedTheta * 8);
                return this.robotVector;
        }


        /***
         * Clears the screen in a roughly terminal-independent way and calls
         * resetCursor() on your behalf.
         */
        public void clearScreen() {
                System.out.printf("\033[2J");
                resetCursor();
        }

        /***
         * Returns the cursor to a home position in a roughly
         * terminal-independent way.
         */
        public void resetCursor() {
            System.out.printf("\033[H");
        }

        /**
         * If the terminal is in raw mode, printing newlines ('\n') does not
         * work in the way you'd expect: the cursor is advanced by one line,
         * but it is not returned to the beginning of the line.  So the Map
         * object needs to know to emit carriage returns before newlines when
         * rendering.
         */
        public void enableRawMode() {
            addCarriageReturns = true;
        }

        /***
         * Renders the map and everything in it to standard output.
         *
         * Right now, the screen will be rendered from the cursor's current
         * position.  (Should you want that to be at the upper left corner,
         * call resetCursor() first.)
         *
         * @param screenWidth  The desired width of the rendered screen, in
         *                     characters.
         * @param screenHeight The desired height of the rendered screen, in
         *                     characters.
         */
        public void draw(int screenWidth, int screenHeight) {

                // Scale so that we take up the full width or height that we are
                // allotted, preserving aspect ratio.
                double smallestScreenDimension = Math.min(screenWidth, screenHeight);
                double smallestVirtualDimension = Math.min(width,  height);

                // Calculate a scaling factor that can convert the virtual
                // coordinates (in feet) to screen coordinates (in characters.)
                //
                // Let's test our assumptions with a thought exercise, shall
                // we?  If we have an 80x25 screen, then, using the formula
                // below:
                //   - A 200x75 board would have a scale of 25/75 = 1/3.
                //     That would map the board to 66.6x25.  That's correct!
                //   - A 40x90 board would have a scaleFactor of 25/40 = 5/8.
                //     That would map the board to 25x56.25.  That's correct,
                //     too!

                double scale = smallestScreenDimension / smallestVirtualDimension;

                // But:
                // - A pathological 10,000x1000 board in the above
                //   regime would have a scaleFactor of 25/1000 =
                //   1/40, mapping the board to 250x25 -- still
                //   pathological!
                //
                //   The real scale factor should have been 80/10000 = 1/125, mapping
                //   the board to 80x0.2.  We had better catch that.
                double largestVirtualDimension = Math.max(width,  height);
                double largestScreenDimension = Math.max(screenWidth, screenHeight);
                if (scale * largestVirtualDimension > largestScreenDimension) {
                        scale = largestScreenDimension / largestVirtualDimension;
                }

                // Allocate a virtual screen buffer to hold the stuff we're
                // drawing.
                //
                // I suppose we could draw directly on the screen, but that
                // might look choppier.
                ArrayList<ScreenCharacter> screenBuffer = new ArrayList<ScreenCharacter>();
                for (int i = 0; i < screenWidth * screenHeight; ++i) {
                        screenBuffer.add(new ScreenCharacter());
                }

                // Draw the borders first.
                //
                // The borders are drawn on the edges, so it's possible for
                // things to be drawn on top of them.
                Point upperLeft  = virtualCoordinateToScreenCoordinate(new Point(-width/2, -height/2), screenWidth, screenHeight, scale);
                Point lowerRight = virtualCoordinateToScreenCoordinate(new Point(width/2, height/2), screenWidth, screenHeight, scale);
                int   bottom     = Math.min(screenHeight - 1, (int) Math.floor(upperLeft.y));  // -y is _down_ on the screen
                int   top        = Math.max(0,                (int) Math.ceil(lowerRight.y));  // +y is _up_ on the screen
                int   left       = Math.max(0,                (int) Math.ceil(upperLeft.x));
                int   right      = Math.min(screenWidth - 1,  (int) Math.floor(lowerRight.x));
                for (int row = top; row < bottom; ++row) {
                        // Left and right walls.
                        drawCharacter(screenBuffer, screenWidth, left,  row, WHITE, '|');
                        drawCharacter(screenBuffer, screenWidth, right, row, WHITE, '|');
                }
                for (int column = left; column < right; ++column) {
                        drawCharacter(screenBuffer, screenWidth, column, top,    WHITE, '-');
                        drawCharacter(screenBuffer, screenWidth, column, bottom, WHITE, '-');
                }
                drawCharacter(screenBuffer, screenWidth, left,  top,    WHITE, '/');
                drawCharacter(screenBuffer, screenWidth, left,  bottom, WHITE, '\\');
                drawCharacter(screenBuffer, screenWidth, right, top,    WHITE, '\\');
                drawCharacter(screenBuffer, screenWidth, right, bottom, WHITE, '/');

                // Draws the waypoints the robot will be traveling to.

                for (Node waypoint: waypoints) {
                        Point screenCoordinate = virtualCoordinateToScreenCoordinate(waypoint.point, screenWidth, screenHeight, scale);
                        drawCharacter(screenBuffer, screenWidth, (int) Math.round(screenCoordinate.x), (int) Math.round(screenCoordinate.y), BRIGHT_GREEN, '$');
                }

                // Draws "you" (that is, draws the robot and its direction vector.)

                Point robotScreenPosition = virtualCoordinateToScreenCoordinate(robotPosition, screenWidth, screenHeight, scale);
                drawCharacter(screenBuffer, screenWidth, (int) Math.round(robotScreenPosition.x), (int) Math.round(robotScreenPosition.y), BRIGHT_RED, '&');

                if (robotVector.length() > Constants.EPSILON) {
                    Vector2 screenVector = new Vector2(robotVector.x, -robotVector.y); // Positive Y needs to point up.
                    Point frontOfRobot = robotScreenPosition.add(screenVector);
                    // Character arrowCharacter = new Integer(octant).toString().charAt(0);
                    Character arrowCharacter = DIRECTION_ARROWS_PER_OCTANT.charAt(octant);
                    drawCharacter(screenBuffer, screenWidth, (int)Math.round(frontOfRobot.x), (int)Math.round(frontOfRobot.y), YELLOW, arrowCharacter);
                }

                // Render the whole screen buffer.
                StringBuilder s = new StringBuilder();
                for (int offset = 0, row = 0; row < screenHeight; ++row) {
                        for (int column = 0; column < screenWidth; ++column, ++offset) {
                                if (useColors) {
                                        s.append(colorSequences[screenBuffer.get(offset).color]);
                                }
                                s.append(screenBuffer.get(offset).c);
                        }
                        if (addCarriageReturns) {
                            s.append('\r');
                        }
                        s.append('\n');
                }
                System.out.print(s.toString());
                System.out.print(colorSequences[WHITE]);
        }

        /***
         * Helper function for draw().  Converts an (x, y) coordinate in feet
         * on the virtual game arena to an integer screen coordinate.
         *
         * (0,0) always maps to the center of the screen; the rest depends on
         * the scaleFactor.
         *
         * @param virtualCoordinateInFeet The position of something on the
         *                                virtual game arena, like a robot or
         *                                a waypoint.
         * @param screenWidth             The width of the screen in
         *                                characters.  Only needed to that we
         *                                can place (0ft, 0ft) at the center
         *                                of the screen.
         * @param screenHeight            The height of the screen in
         *                                characters.  Needed for the same
         *                                reasons as screenWidth.
         * @param scaleFactor             A multiplier that converts virtual
         *                                coordinates to screen coordinates.
         * @return                        A screen position.
         */
        private static Point virtualCoordinateToScreenCoordinate(Point virtualCoordinateInFeet, int screenWidth, int screenHeight, double scaleFactor) {
                // We have inverted y because of how screen coordinates
                // work. With screen coordinates, positive y goes toward the
                // bottom of the screen.  Virtual coordinates, which are human
                // standard coordinates, have y going up.  Because of this, we
                // must invert y so that it behaves the way we humans expect
                // it to.

                Point screenPosition = new Point(virtualCoordinateInFeet.x * scaleFactor + screenWidth/2,
                                                 -virtualCoordinateInFeet.y * scaleFactor + screenHeight/2);
                return screenPosition;
        }

        private void drawCharacter(ArrayList<ScreenCharacter> screenBuffer, int maxX, int x, int y, int color, char c) {
                int offset = maxX * y + x;
                if (offset < 0 || offset > screenBuffer.size()) {
                        return;
                }
                screenBuffer.set(offset, new ScreenCharacter(color, c));
        }
}
