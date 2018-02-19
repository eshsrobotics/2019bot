package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.usfirst.frc.team1759.robot.MatchData;

import edu.wpi.first.wpilibj.DriverStation;

public class Graph {

        public List<Node> nodes;
        public Node target;
        public Node currentNode;

        /**
         *
         *Naming Convention: Assume you are viewing the field from the side. Red is to your left, Blue is to your right. The far side of the field is the top,
         *the near side of the field is the bottom.
         *R: Red
         *B: Blue
         *S: Switch
         *SC: Scale
         *
         *                                                     Top
         *                              -------------------------------------------------
         *                              |                                               |
         *                      R       |       RS              SC              BS      |       B
         *                              |                                               |
         *                              -------------------------------------------------
         *                                                    Bottom
         * Example of use for declaration of nodes and the points between them.
         *
         * Node redNode3 = new Node(0, 3);
         * Node redNode32 = new Node(0, 2.5);
         *
         * addEdge(redNode3, redNode32);
         *
         * @param matchData Since the scales that can score for our
         *                  alliance are randomized at the start of
         *                  the match, we need this object to tell us
         *                  where to go.
         */
        public Graph(MatchData matchData) {
                nodes = new ArrayList<Node>();

                // All of these values are in feet, with (0, 0)
                // representing the center of the game field.

                // Red (left) side starting positions.
                Node redStartTop = new Node(-24.53, 11.28);
                Node redStartCenter = new Node(-24.53, 0);
                Node redStartBottom = new Node(-24.53, -11.28);

                Node redSwitchSetupTop = new Node(-13, 11.28);      // Far above the left switch
                Node redSwitchTop = new Node(-13, 6);               // Just above the left switch: scoring position.
                Node redSwitchBottom = new Node(-13, -6);           // Just below the left switch: scoring position.
                Node redSwitchSetupBottom = new Node(-13, -11.28);  // Far below the left switch.

                // Neutral zone positions -- above and below the giant scale, and
                // between the red and blue SwitchSetup{Top,Bottom} positions.
                Node topScale = new Node(0, 11.28);
                Node bottomScale = new Node(0, -11.28);


                Node blueSwitchSetupTop = new Node(13, 11.28);     // Far above the right switch
                Node blueSwitchTop = new Node(13, 6);              // Just above the right switch: scoring position.
                Node blueSwitchBottom = new Node(13, -6);          // Just below the right switch: scoring position.
                Node blueSwitchSetupBottom = new Node(13, -11.28); // Far below the right switch.

                // Blue (right) side starting positions.
                Node blueStartTop = new Node(24.53, 11.28);
                Node blueStartCenter = new Node(24.53, 0);
                Node blueStartBottom = new Node(24.53, -11.28);

                // The primary node network connects all the starting
                // positions, the top and bottom scale positions, and
                // the top and bottom switch setup positions in a
                // giant rectangle.
                addEdge(redStartTop, redStartCenter);
                addEdge(redStartCenter, redStartBottom);
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
                addEdge(blueStartTop, blueStartCenter);
                addEdge(blueStartCenter, blueStartBottom);

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
                // switch scoring positions.  (We're not so sure about
                // these last ones, as they mandate diagonal
                // movement.)
                addEdge(topScale, redSwitchTop);
                addEdge(topScale, blueSwitchTop);
                addEdge(bottomScale, redSwitchBottom);
                addEdge(bottomScale, blueSwitchBottom);

                if(matchData.getAlliance() == DriverStation.Alliance.Blue) {
                        currentNode = blueStartBottom;
                        target = redSwitchSetupTop;
                } else {
                        currentNode = redStartBottom;
                        target = blueSwitchSetupTop;
                }
        }

        public void addEdge(Node node1, Node node2) {
                node1.neighbors.add(node2);
                node2.neighbors.add(node1);
        }

        public static LinkedList<Node> findPath(Node start, Node target) {
                Map<Node, Node> previousNode = new HashMap<>();

                Queue<Node> toVisit = new LinkedList<>();
                toVisit.add(start);

                while (!toVisit.isEmpty()) {
                        Node currentNode = toVisit.poll();

                        for (Node neighbor : currentNode.neighbors) {
                                if (previousNode.containsKey(neighbor)) {
                                        continue;
                                }
                                previousNode.put(neighbor, currentNode);
                                toVisit.add(neighbor);
                        }
                }

                LinkedList<Node> path = new LinkedList<>();

                Node currentNode = target;
                while (currentNode != start) {
                        path.add(currentNode);
                        currentNode = previousNode.get(currentNode);
                }
                Collections.reverse(path);

                return path;
        }

}
