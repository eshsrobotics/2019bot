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

        /**
         * Nodes which serve as Waypoints may have optional names to make them
         * easier to identify.
         */
        private String name;

        /**
         * Initializes a Node.
         *
         * @param point The Node's location on the game field.  Coordinates are in
         *              feet; (0,0) is conventionally the center of the field.
         * @param name  The name of this Node.
         */
        public Node(Point point, String name) {
            id = nextId++;
            this.point = point;
            this.neighbors = new ArrayList<>();
            this.name = name;
        }

        /**
         * Initializes a Node.
         *
         * @param x    The Node's X-coordinate, in feet.
         * @param y    The Node's Y-coordinate, in feet.
         * @param name The name of this Node.
         */
        public Node(double x, double y, String name) {
            this(new Point(x, y), name);
        }

        /**
         * Initializes an unnamed Node.
         *
         * @param point The Node's location, in feet.
         */
        public Node(Point point) {
            this(point, "");
        }

        /**
         * Initializes an unnamed Node.
         *
         * @param x The Node's X-coordinate, in feet.
         * @param y The Node's Y-coordinate, in feet.
         */
        public Node(double x, double y) {
            this(new Point(x, y), "");
        }

        /**
         * Returns this Node's unique ID.
         */
        public int getId() {
            return this.id;
        }

        /**
         * Gets this Node's location.  Coordinates are in feet.
         */
        public Point getPosition() {
            return point;
        }

        /**
         * Returns the name that was assigned to this Node, or an empty string if
         * no name was assigned.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Assigns an arbitrary name to this Node.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Get this list of neighbors that this Node has.
         *
         * You may assign new neighbors using Graph.addEdge().
         */
        public List<Node> getNeighbors() {
            return this.neighbors;
        }
}
