package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.usfirst.frc.team1759.robot.MatchData;

import edu.wpi.first.wpilibj.DriverStation;

public class Graph {

        private Node target;
        private Node currentNode;

	public Graph() {
           Node origin = new Node (0, 0);
	   Node easy = new Node (5, 5);
	   Node altered = new Node (7, 3);
	   Node far = new Node (10.5, 13.5);

	   addEdge(origin, easy);
	   addEdge(origin, altered);
	   addEdge(easy, altered);
	   addEdge(altered, far);
	   addEdge(easy, far);

	   currentNode = origin;
	   target = far;
        }
	
	public Graph(MatchDataInterface matchDataInterface) {

	}
        /**
         * This is meant to return the starting position of the robot. If you are
         * standing in your alliance station and are looking onto the field, 0 is to
         * the left, 1 is directly ahead, and 2 is to the right.
         *
         * @param matchData The data structure that stores the alliance
         *                   assignments for the major locations in a given
         *                   match.
         * @return The Node where your team's robot will start in this match.
         */
        private Node getStartingNode(MatchDataInterface matchData) {
		return null;
        }


        /**
         * Returns the robot's starting node for this particular match if we
         * were constructed with valid match data, or null otherwise.
         */
        public Node getStartingNode() {
                return this.currentNode;
        }

        /**
         * Returns the robot's target node for this particular match if we were
         * constructed with valid match data, or null otherwise.
         */
        public Node getTargetNode() {
                return this.target;
        }

        /**
         * Links two nodes to each other, whether they belong to this Graph or
         * not.  Each node will become the other node's neighbor.
         */
        public void addEdge(Node node1, Node node2) {
            node1.neighbors.add(node2);
            node2.neighbors.add(node1);
        }

        /***
         * Returns a linked list consisting of the shortest path from start to
         * target.
         *
         * @param start
         *            The node to start our search from.  It does not need to
         *            belong to this Graph object.
         * @param target
         *            The node we are trying to reach.  It does not need to
         *            belong to this Graph object.
         * @return A LinkedList<Node>. If the linked list has one element, the
         *          start *IS* the target; if the linked list is null, there is
         *          no path from the start to the target (in other words, they
         *          are not linked by any number of neighbors.)
         */
        public LinkedList<Node> findShortestPath(Node start, Node target) {
            Set<Integer> visitedNodeIds = new HashSet<Integer>();
            return findShortestPathRecursive(start, target, visitedNodeIds);
        }

        /***
         * A recursive flood-fill algorithm that implements the actual
         * findShortestPath() algorithm.
         *
         * Preconditions:
         * - The current node should not already be in the visitedNodeIds set.
         * - Current and target should not be null.
         *
         * Postconditions:
         * - The current node will be in the visitedNodeIds set.
         *
         * @param current The node that is currently being flooded.
         * @param target The node that, if reached, will terminate the flood early.
         * @return A LinkedList<Node> consisting of the shortest path that
         *         reached the target, if any path was found from current to
         *         target; null otherwise.
         */
        private LinkedList<Node> findShortestPathRecursive(Node current,
                                                            Node target,
                                                            Set<Integer> visitedNodeIds) {

            if (current.id == target.id) {
                // Direct hit.
                LinkedList<Node> result = new LinkedList<Node>();
                result.addFirst(current);
                return result;
            }

            // *This* node has been visited.
            visitedNodeIds.add(current.id);
            LinkedList<Node> shortestPath = null;

            // Visit all the neighbors in turn.
            for (Node neighbor : current.neighbors) {
                if (!visitedNodeIds.contains(neighbor.id)) {
                    LinkedList<Node> path = findShortestPathRecursive(neighbor,
                                                                      target,
                                                                      visitedNodeIds);
                    if (path != null) {
                        // A route was found to the target, and this node is
                        // now part of it.
                        path.addFirst(current);

                        if (shortestPath == null || path.size() < shortestPath.size()) {
                            shortestPath = path;
                        }
                    } else {
                        // No route to target from current neighbor.
                    }
                }
            }

            if (shortestPath == null) {
                // No route to target from any neighbor.
            }

            return shortestPath;
        }
}
