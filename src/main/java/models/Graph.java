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
        private ArrayList<Node> redStartingPositions;
        private ArrayList<Node> blueStartingPositions;

        public static final int RED_ALLIANCE = 0;
        public static final int BLUE_ALLIANCE = 1;

        public static final int LEFT_POSITION = 0;
        public static final int CENTER_POSITION = 1;
        public static final int RIGHT_POSITION = 2;

        /**
         * Naming Convention: Assume you are viewing the field from the side. Red is
         * to your left, Blue is to your right. The far side of the field is the
         * top, the near side of the field is the bottom.
         *
         * R: Red
         * B: Blue
         * S: Switch
         * SC: Scale
         *
         *                             Top
         *      -------------------------------------------------
         *      |                                               |
         *    R |       RS              SC              BS      | B
         *      |                                               |
         *      -------------------------------------------------
         *                            Bottom
         *
         * Example of use for declaration of nodes and the points between them.
         *
         *   Node redNode3 = new Node(0, 3);
         *   Node redNode32 = new Node(0, 2.5);
         *   addEdge(redNode3, redNode32);
         *
         * @param matchData
         *            Since the scales that can score for our alliance are
         *            randomized at the start of the match, we need this object to
         *            tell us where to go.
         */
        public Graph(MatchDataInterface matchData) {
            // All of these values are in feet, with (0, 0)
            // representing the center of the game field.

            // Red (left) side starting positions.
            Node redStartTop = new Node(-25.86, 10.30);
            Node redStartCenter = new Node(-25.86, 0);
            Node redStartBottom = new Node(-25.86, -10.30);
            
            // Red Nodes, shifted a few feet for turning capability.
            final double SHIFT = 3.0; 
            Node redTurnTop = new Node(redStartTop.getPosition().x + SHIFT, 10.30);
            Node redTurnCenter = new Node(redStartCenter.getPosition().x + SHIFT, 0);
            Node redTurnBottom = new Node(redStartBottom.getPosition().x + SHIFT, -10.30);

            Node redSwitchSetupTop = new Node(-13, 11.28);     // Far above the left switch
            Node redSwitchTop = new Node(-13, 7.33);           // Just above the left switch: scoring position.
            Node redSwitchBottom = new Node(-13, -7.33);       // Just below the left switch: scoring position.
            Node redSwitchSetupBottom = new Node(-13, -11.28); // Far below the left switch.

            // Neutral zone positions -- above and below the giant scale, and
            // between the red and blue SwitchSetup{Top,Bottom} positions.
            Node topScale = new Node(0, 11.28);
            Node bottomScale = new Node(0, -11.28);

            Node blueSwitchSetupTop = new Node(13, 11.28);     // Far above the right switch
            Node blueSwitchTop = new Node(13, 7.33);           // Just above the right switch: scoring position.
            Node blueSwitchBottom = new Node(13, -7.33);       // Just below the right switch: scoring position.
            Node blueSwitchSetupBottom = new Node(13, -11.28); // Far below the right switch.

            // Blue (right) side starting positions.
            Node blueStartTop = new Node(25.86, 10.30);
            Node blueStartCenter = new Node(25.86, 0);
            Node blueStartBottom = new Node(25.86, -10.30);
            
            Node blueTurnTop = new Node(blueStartTop.getPosition().x - SHIFT, 10.30);
            Node blueTurnCenter = new Node(blueStartCenter.getPosition().x - SHIFT, 0);
            Node blueTurnBottom = new Node(blueStartBottom.getPosition().x - SHIFT, -10.30);

            // Set the names in case we want to display them during the
            // simulation.
            redStartTop.setName("redStartTop");
            redStartCenter.setName("redStartCenter");
            redStartBottom.setName("redStartBottom");
            redSwitchSetupTop.setName("redSwitchSetupTop");
            redSwitchTop.setName("redSwitchTop");
            redSwitchBottom.setName("redSwitchBottom");
            redSwitchSetupBottom.setName("redSwitchSetupBottom");
            topScale.setName("topScale");
            bottomScale.setName("bottomScale");
            blueSwitchSetupTop.setName("blueSwitchSetupTop");
            blueSwitchTop.setName("blueSwitchTop");
            blueSwitchBottom.setName("blueSwitchBottom");
            blueSwitchSetupBottom.setName("blueSwitchSetupBottom");
            blueStartTop.setName("blueStartTop");
            blueStartCenter.setName("blueStartCenter");
            blueStartBottom.setName("blueStartBottom");

            // The primary node network connects all the starting
            // positions, the top and bottom scale positions, and
            // the top and bottom switch setup positions in a
            // giant rectangle.
            addEdge(redStartTop, redTurnTop);
            addEdge(redStartCenter, redTurnCenter);
            addEdge(redStartBottom, redTurnBottom);
            addEdge(redTurnTop, redTurnCenter);
            addEdge(redTurnBottom, redTurnCenter);
            addEdge(redTurnTop, redSwitchSetupTop);
            addEdge(redTurnBottom, redSwitchSetupBottom);
            addEdge(redStartBottom, redSwitchSetupBottom);
            addEdge(redStartTop, redSwitchSetupTop);
            addEdge(redSwitchSetupTop, redSwitchTop);         // Move to the nearest scoring position.
            addEdge(redSwitchSetupBottom, redSwitchBottom);   // Move to the nearest scoring position.
            addEdge(redSwitchSetupBottom, bottomScale);
            addEdge(redSwitchSetupTop, topScale);
            addEdge(topScale, blueSwitchSetupTop);
            addEdge(bottomScale, blueSwitchSetupBottom);
            addEdge(blueSwitchSetupTop, blueSwitchTop);       // Move to the nearest scoring position.
            addEdge(blueSwitchSetupBottom, blueSwitchBottom); // Move to the nearest scoring position.
            addEdge(blueSwitchSetupBottom, blueStartBottom);
            addEdge(blueSwitchSetupTop, blueStartTop);
            addEdge(blueStartTop, blueTurnTop);
            addEdge(blueStartCenter, blueTurnCenter);
            addEdge(blueStartBottom, blueTurnBottom);
            addEdge(blueTurnCenter, blueTurnBottom);
            addEdge(blueTurnCenter, blueTurnTop);
            addEdge(blueTurnTop, blueSwitchSetupTop);
            addEdge(blueTurnBottom, blueSwitchSetupBottom);

            // Bypass positions: These make it easier to go from
            // one node to a "far" one without an
            // accelerate/decelerate cycle for the nodes
            // inbetween.

            // Eases driving from top to bottom.
            addEdge(redStartTop, redStartBottom);
            addEdge(blueStartTop, blueStartBottom);

            // Eases driving to the center.
            addEdge(redStartTop, topScale);
            addEdge(blueStartTop, topScale);
            addEdge(redStartBottom, bottomScale);
            addEdge(blueStartBottom, bottomScale);

            // Eases driving straight across to enemy switches.
            addEdge(redStartTop, blueSwitchSetupTop);
            addEdge(blueStartTop, redSwitchSetupTop);
            addEdge(redStartBottom, blueSwitchSetupBottom);
            addEdge(blueStartBottom, redSwitchSetupBottom);

            // Eases getting from the center straight to the
            // switch scoring positions. (We're not so sure about
            // these last ones, as they mandate diagonal
            // movement.)
            addEdge(topScale, redSwitchTop);
            addEdge(topScale, blueSwitchTop);
            addEdge(bottomScale, redSwitchBottom);
            addEdge(bottomScale, blueSwitchBottom);

            // Some of these waypoints are special because the robot will
            // start the simulation there.
            redStartingPositions = new ArrayList<Node>();
            redStartingPositions.add(redStartTop);
            redStartingPositions.add(redStartCenter);
            redStartingPositions.add(redStartBottom);

            blueStartingPositions = new ArrayList<Node>();
            blueStartingPositions.add(blueStartTop);
            blueStartingPositions.add(blueStartCenter);
            blueStartingPositions.add(blueStartBottom);

            if (matchData != null) {
                this.currentNode = getStartingNode(matchData);

                if (matchData.getAlliance() ==  DriverStation.Alliance.Red) {
                    switch (matchData.getTarget()) {
                        case CLOSE_SWITCH:
                            target = matchData.getNearSwitchPosition() == MatchData.Position.LEFT ? redSwitchTop : redSwitchBottom;
                            break;
                        case SCALE:
                            target = matchData.getScalePosition() == MatchData.Position.LEFT ? topScale : bottomScale;
                            break;
                        case FAR_SWITCH:
                            target = matchData.getFarSwitchPosition() == MatchData.Position.LEFT ? blueSwitchTop : blueSwitchBottom;
                            break;
                    }
                } else {
                    switch (matchData.getTarget()) {
                        case CLOSE_SWITCH:
                            target = matchData.getNearSwitchPosition() == MatchData.Position.LEFT ? blueSwitchBottom : blueSwitchTop;
                            break;
                        case SCALE:
                            target = matchData.getScalePosition() == MatchData.Position.LEFT ? bottomScale : topScale;
                            break;
                        case FAR_SWITCH:
                            target = matchData.getFarSwitchPosition() == MatchData.Position.LEFT ? redSwitchBottom : redSwitchTop;
                            break;

                    }
                }

                if (this.currentNode == null) {
                    System.out.println("Current node is null. Check match data config");
                }

                if (this.target == null) {
                    System.out.println("Target node is null. Check match data config");
                }

            } else {
                // During pure simulation testing, the match data sometimes
                // doesn't exist.  In that case, invent it.
                final Node potentialTargets[] = {
                    redSwitchTop,
                    redSwitchBottom,
                    blueSwitchTop,
                    blueSwitchBottom,
                    topScale,
                    bottomScale
                };
                ArrayList<Node> startingPositions = (Math.random() > 0.5 ? redStartingPositions : blueStartingPositions);
                int randomStartingPosition = (int) Math.floor(Math.random() * 3);
                this.currentNode = startingPositions.get(randomStartingPosition);

                int randomTarget = (int) Math.floor(Math.random() * potentialTargets.length);
                this.target = potentialTargets[randomTarget];
            }
        }


        /**
         * Constructs a graph to use for testing purposes.
         *
         * You may optionally supply Nodes that belong to this graph if you
         * wish to use it for waypoint finding; if you don't care about that,
         * pass null for the arguments.
         *
         * @param startingNode The Node that the graph will consider to be its
         *                      starting position.  Optional.
         * @param targetNode   The node that the graph will consider to be its
         *                      ending position.  Optional, and can be equal to
         *                      the startingNode if you wish.
         */
        public Graph(Node startingNode, Node targetNode) {
                this.currentNode = startingNode;
                this.target = targetNode;
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
            int alliance = matchData.getAlliance() == DriverStation.Alliance.Red ? 0 : 1;
            int position = -1;
            switch (matchData.getOwnStartPosition()) {
                case LEFT:
                    position = 0;
                    break;
                case CENTER:
                    position = 1;
                    break;
                case RIGHT:
                    position = 2;
                    break;
            }
            switch ((3 * alliance) + position) {
                case 0:
                    return redStartingPositions.get(0); // Red Top
                case 1:
                    return redStartingPositions.get(1); // Red Center
                case 2:
                    return redStartingPositions.get(2); // Red Bottom
                case 3:
                    return blueStartingPositions.get(2); // Blue Bottom
                case 4:
                    return blueStartingPositions.get(1); // Blue Center
                case 5:
                    return blueStartingPositions.get(0); // Blue Top
                default:
                    return null;
            }
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
