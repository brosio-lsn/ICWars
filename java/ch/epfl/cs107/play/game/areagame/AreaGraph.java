package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.*;


/**
 * TODO
 * <p>
 * AreaGraph is a specific kind of graph apply to Area.
 * The graph is composed of AreaNodes which are defined by their position in the graph (DiscreteCoordinates)
 * and the existence of directed edge between them (from) and their four neighbors (to).
 * Nodes are stored into a Map.
 * Note: DiscreteCoordinate are serializable reimplementing hashCode() and equals() making the keys dependant only from
 * the DiscreteCoordinate x and y values and not from the object itself.
 */
public class AreaGraph {

    /// Map containing all the node or vertices of the area graph
    /**
     * TODO
     */
    private final Map<DiscreteCoordinates, AreaNode> nodes;

    /**
     * TODO
     * <p>
     * Default AreaGraph Constructor
     */
    public AreaGraph() {
        nodes = new HashMap<>();
    }

    /**
     * TODO
     */
    public void debugPrint() {
        nodes.forEach((key1, value) -> System.out.println(key1.x + ", " + key1.y));
    }

    /**
     * TODO
     * <p>
     * Add if absent a new Node into the graph.
     * Create a new Node and put it in the nodes map at given coordinates key.
     * Note: DiscreteCoordinate are serializable reimplementing hashCode() and equals() making the keys dependant only from
     * the DiscreteCoordinate x and y values and not from the object itself.
     *
     * @param coordinates  (DiscreteCoordinate): Position in the graph of the node to add, used as key for the map, not null
     * @param hasLeftEdge  (boolean): indicate if directed edge to the left direction exists
     * @param hasUpEdge    (boolean): indicate if directed edge to the up direction exists
     * @param hasRightEdge (boolean): indicate if directed edge to the right direction exists
     * @param hasDownEdge  (boolean): indicate if directed edge to the down direction exists
     */
    public void addNode(DiscreteCoordinates coordinates,
                        boolean hasLeftEdge,
                        boolean hasUpEdge,
                        boolean hasRightEdge,
                        boolean hasDownEdge) {
        nodes.putIfAbsent(coordinates, new AreaNode(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge));
    }

    /**
     * TODO
     *
     * @return
     */
    protected Map<DiscreteCoordinates, AreaNode> getNodes() {
        return nodes;
    }


    /**
     * TODO
     * <p>
     * Return if a node exists in the graph
     *
     * @param coordinates (DiscreteCoordinates): may be null
     * @return (boolean): true if the given node exists in the graph
     */
    public boolean nodeExists(DiscreteCoordinates coordinates) {
        return nodes.containsKey(coordinates);
    }

    /**
     * TODO
     *
     * @param coordinates
     * @param signal
     */
    public void setSignal(DiscreteCoordinates coordinates, Logic signal) {
        // TODO comments

        if (!nodes.containsKey(coordinates))
            throw new IllegalArgumentException("The node do not exist");
        nodes.get(coordinates).setSignal(signal);
    }

    /**
     * TODO
     * <p>
     * Compute the shortest path in this AreaGraph from given DiscreteCoordinate to given DiscreteCoordinates
     *
     * @param from (DiscreteCoordinates): source node of the desired path, not null
     * @param to   (DiscreteCoordinates): sink node of the desired path, not null
     * @return (Iterator of Orientation): return an iterator containing the shortest path from source to sink, or null if the path does not exist!
     */
    public Queue<Orientation> shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) {
        // TODO comments

        AreaNode start = nodes.get(from);
        AreaNode goal = nodes.get(to);

        if (goal == null || start == null || start == goal)
            return null;

        //System.out.println("Looking for path from: " + start.coordinates.toString() + " to : "+ goal.coordinates.toString());

        // used to size data structures appropriately
        final int size = nodes.size();
        // The set of nodes already evaluated.
        final Set<AreaNode> visitedSet = new HashSet<>(size);
        // The set of tentative nodes to be evaluated, initially containing the start node
        final List<AreaNode> toVisitSet = new ArrayList<>(size);
        toVisitSet.add(start);
        // The map of navigated nodes. <neighbor, current>
        final Map<AreaNode, AreaNode> cameFrom = new HashMap<>(size);

        while (!toVisitSet.isEmpty()) {
            // Get the first node, we will now evaluate it
            final AreaNode current = toVisitSet.get(0);

            // If the current is the goal one, we can end
            if (current.equals(goal))
                return reconstructPath(cameFrom, goal);

            // Otherwise, we remove it from non-evaluated node and put it inside evaluated node
            toVisitSet.remove(0);
            visitedSet.add(current);

            // For all its neighbors
            // Ignore the neighbor which is already evaluated.
            // Ignore inactive neighbors
            // This path is the best. Record it!
            current.getConnectedNodes()
                .stream()
                .filter(neighbor -> !visitedSet.contains(neighbor))
                .filter(AreaNode::isActive)
                .forEach(neighbor -> {
                    toVisitSet.add(neighbor);
                    cameFrom.put(neighbor, current);
                });
        }

        return null;
    }

    /**
     * TODO
     *
     * @param cameFrom
     * @param current
     * @return
     */
    private Queue<Orientation> reconstructPath(Map<AreaNode, AreaNode> cameFrom, AreaNode current) {
        // TODO comments

        final List<Orientation> totalPath = new ArrayList<>();

        while (current != null) {
            final AreaNode previous = current;
            current = cameFrom.get(current);
            if (current != null) {
                final Orientation edge = current.getOrientation(previous);
                totalPath.add(edge);
            }
        }

        Collections.reverse(totalPath);

        /*
        // Print the path for debug purpose
        System.out.println("--------------------Path : ");
        for(Orientation o : totalPath){
            System.out.println(o.toString());
        }
        */

        return new LinkedList<>(totalPath);
    }

    /**
     * TODO
     */
    protected class AreaNode {

        /// Position of the node into the graph, used as key for the map
        /**
         * TODO
         */
        private final DiscreteCoordinates coordinates;

        /// Flag: true if a directed edge between this and indicated direction (left, up, right, down) exists
        /**
         * TODO
         */
        private final boolean hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge;

        /// a List of the connectedNode. May be null if getConnectedNodes is never called
        /**
         * TODO
         */
        private List<AreaNode> connectedNodes;

        // Signal indicating it the node is active
        /**
         * TODO
         */
        private Logic isActive;

        /**
         * TODO
         * <p>
         * Default AreaNode Constructor
         *
         * @param coordinates  (DiscreteCoordinate): Position in the graph of the node to add, used as key for the map, not null
         * @param hasLeftEdge  (boolean): indicate if directed edge to the left direction exists
         * @param hasUpEdge    (boolean): indicate if directed edge to the up direction exists
         * @param hasRightEdge (boolean): indicate if directed edge to the right direction exists
         * @param hasDownEdge  (boolean): indicate if directed edge to the down direction exists
         */
        protected AreaNode(DiscreteCoordinates coordinates, boolean hasLeftEdge, boolean hasUpEdge, boolean hasRightEdge, boolean hasDownEdge) {
            this.coordinates = coordinates;
            this.hasLeftEdge = hasLeftEdge;
            this.hasUpEdge = hasUpEdge;
            this.hasRightEdge = hasRightEdge;
            this.hasDownEdge = hasDownEdge;

            isActive = Logic.TRUE;
        }

        /**
         * TODO
         * <p>
         * Neighbors getter
         * see method addNeighbor()
         *
         * @return (Array of AreaNode): the array of four neighbor Nodes. Elements may be null if no connection exists
         */
        List<AreaNode> getConnectedNodes() {
            // TODO comments

            if (connectedNodes == null) {
                connectedNodes = new ArrayList<>();
                addNeighbor("Left", hasLeftEdge, new DiscreteCoordinates(coordinates.x - 1, coordinates.y));
                addNeighbor("Up", hasUpEdge, new DiscreteCoordinates(coordinates.x, coordinates.y + 1));
                addNeighbor("Right", hasRightEdge, new DiscreteCoordinates(coordinates.x + 1, coordinates.y));
                addNeighbor("Down", hasDownEdge, new DiscreteCoordinates(coordinates.x, coordinates.y - 1));
            }
            return connectedNodes;
        }

        /**
         * TODO
         *
         * @param neighborString
         * @param hasNeighbor
         * @param c
         */
        private void addNeighbor(String neighborString, boolean hasNeighbor, DiscreteCoordinates c) {
            // TODO comments

            if (nodes.containsKey(c) && hasNeighbor)
                connectedNodes.add(nodes.get(c));
            else if (hasNeighbor) // TODO throw exception
                System.out.println(neighborString + " neighbor for " + coordinates.toString() + " Node does not exists");
        }


        /**
         * TODO
         * <p>
         * Indicate the orientation we need to follow to reach previous node from this one
         * Assume the previous node is a neighbor node
         *
         * @param previous (AreaNode), not null
         * @return (Orientation)
         */
        Orientation getOrientation(AreaNode previous) {
            // TODO comments

            if (previous.coordinates.x < coordinates.x)
                return Orientation.LEFT;
            else if (previous.coordinates.x > coordinates.x)
                return Orientation.RIGHT;
            else if (previous.coordinates.y > coordinates.y)
                return Orientation.UP;
            else if (previous.coordinates.y < coordinates.y)
                return Orientation.DOWN;
            else return null;
        }

        /**
         * TODO
         *
         * @param signal
         */
        public void setSignal(Logic signal) {
            isActive = signal;
        }

        /**
         * TODO
         *
         * @return
         */
        public boolean isActive() {
            return isActive.isOn();
        }

    }
}
