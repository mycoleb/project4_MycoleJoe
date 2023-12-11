package GraphPackage;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * DirectedGraph: An implementation of a generic graph with directional edges between vertices.
 * @param <T> The data type to store in the vertex labels.
 */
public class DirectedGraph<T> implements GraphInterface<T>
{
    /** Contains the labels of vertices as the key, and a pointer to the vertex itself as a value. For looking up
     * the vertex object by its label. */
    private final HashMap<T, VertexInterface<T>> vertices;
    /** Stores the current number of edges in the entire graph. */
    private int edgeCount;

    /**
     * Constructor creates a new DirectedGraph object with no vertices or edges.
     */
    public DirectedGraph() {
        vertices = new HashMap<>();
        edgeCount = 0;
    }

    /**
     * Adds a new vertex with no edges to the graph.
     * @param vertexLabel The label object to store in the vertex. Must be distinct from other labels in the graph.
     * @return True if the new vertex was added to the graph, or false if the label already exists in the graph.
     */
    public boolean addVertex(T vertexLabel) {
        if (vertices.containsKey(vertexLabel))
            return false;
        else {
            vertices.put(vertexLabel, new Vertex<>(vertexLabel));
            return true;
        }
    }

    /**
     * Removes a vertex and all of its edges from the graph.
     * @param vertexLabel The label of the vertex to remove from the graph.
     * @return True if the vertex that corresponds to the label was removed, or false if the label was not found.
     */
    public boolean removeVertex(T vertexLabel) {
        if (!vertices.containsKey(vertexLabel))
            return false;
        else {
            VertexInterface<T> vertexOfLabel = vertices.get(vertexLabel);
            Iterator<T> vertexIterator = vertices.keySet().iterator();

            while (vertexIterator.hasNext())
                removeEdge(vertexIterator.next(), vertexLabel);

            vertices.remove(vertexLabel);
            return true;
        }
    }

    /**
     * Creates a new directional edge from one vertex to another with the specified edge weight.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object, distinct from begin, that labels the end vertex of the edge.
     * @param edgeWeight The real value of the edge's weight.
     * @return True if a new edge was added, or false if one of the vertices was not found, or the edge already exists.
     */
    public boolean addEdge(T begin, T end, double edgeWeight) {
        if (containsLabels(begin, end)) {
            VertexInterface<T> beginVertex = vertices.get(begin);
            VertexInterface<T> endVertex = vertices.get(end);

            if (beginVertex.connect(endVertex, edgeWeight)) {
                edgeCount++;
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new directional edge from one vertex to another with the default edge weight of 0.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object, distinct from begin, that labels the end vertex of the edge.
     * @return True if a new edge was added, or false if one/both of the vertices was not found, or the edge
     * already exists.
     */
    public boolean addEdge(T begin, T end) { return addEdge(begin, end, 0); }

    /**
     * Removes a directional edge from one vertex to another.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object, distinct from begin, that labels the end vertex of the edge.
     * @return True if the edge was removed, or false if the edge/vertex was not found.
     */
    public boolean removeEdge(T begin, T end) {
        if (containsLabels(begin, end)) {
            VertexInterface<T> beginVertex = vertices.get(begin);
            VertexInterface<T> endVertex = vertices.get(end);

            if (beginVertex.disconnect(endVertex)) {
                edgeCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if there is an edge between two specified vertices.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object that labels the end vertex of the edge.
     * @return True if there is an edge between the two vertices, or false otherwise.
     */
    public boolean hasEdge(T begin, T end) {
        if (containsLabels(begin, end)) {
            VertexInterface<T> beginVertex = vertices.get(begin);
            VertexInterface<T> endVertex = vertices.get(end);
            ArrayList<T> neighborLabels = beginVertex.getNeighborLabels(Double.MIN_VALUE);
            return neighborLabels.contains(endVertex.getLabel());
        }
        return false;
    }

    /**
     * Determines if the graph contains the two specified label objects, corresponding to two vertices.
     * @param label1 The label object of the first vertex.
     * @param label2 The label object of the second vertex.
     * @return True if both of the labels are found in the graph, or false otherwise.
     */
    private boolean containsLabels(T label1, T label2) {
        if (vertices.containsKey(label1) && vertices.containsKey(label2))
            return true;
        return false;
    }

    /**
     * Determines if the graph is empty, containing no vertices.
     * @return True if the graph is empty, or false otherwise.
     */
    public boolean isEmpty() { return vertices.isEmpty(); }

    /**
     * Removes all vertices and edges from the graph, resetting it to an empty state.
     */
    public void clear() {
        vertices.clear();
        edgeCount = 0;
    }

    /**
     * Retrieves a list of label objects containing the labels of any neighbors of the specified vertex connected
     * by an edge greater than or equal to the specified edge weight.
     * @param vertexLabel The label object that corresponds to the vertex for which to retrieve neighbors.
     * @param weightMin The minimum weight of the edge connection between the specified vertex and it's neighbors.
     * @return A list of any vertex label objects which the specified vertex has an edge pointing to, where that edge's
     * weight is greater than or equal to the weight minimum.
     */
    public ArrayList<T> getNeighbors(T vertexLabel, double weightMin) {
        if (vertices.containsKey(vertexLabel)) {
            VertexInterface<T> vertexOfLabel = vertices.get(vertexLabel);
            return vertexOfLabel.getNeighborLabels(weightMin);
        }
        return null;
    }

    /**
     * Retrieves the number of vertices stored in the graph.
     * @return The number of vertices stored in the graph.
     */
    public int getNumberOfVertices() { return vertices.size(); }

    /**
     * Retrieves the number of edges between vertices stored in the graph.
     * @return The number of edges between vertices stored in the graph.
     */
    public int getNumberOfEdges() { return edgeCount; }

    /**
     * Resets the visited status, cost, and predecessor values of all vertices in the graph to prepare the graph for
     * a new traversal or search.
     */
    protected void resetVertices() {
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        while (vertexIterator.hasNext())
        {
            VertexInterface<T> nextVertex = vertexIterator.next();
            nextVertex.unvisit();
            nextVertex.setCost(0);
            nextVertex.setPredecessor(null);
        }
    }

    /**
     * Retrieves a queue of label objects representing the breadth-first traversal of the graph. This starts at
     * the vertex corresponding to the origin label, and visits any other vertices connected to the origin.
     * @param origin An object that labels the origin vertex of the traversal.
     * @return A queue of label objects in the order desired for breadth-first traversal from the origin.
     */
    public Queue<T> getBreadthFirstTraversal(T origin) {
        if (isEmpty())
            return null;

        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>();
        Queue<VertexInterface<T>> vertexQueue = new LinkedList<>();

        VertexInterface<T> originVertex = vertices.get(origin);
        originVertex.visit();

        traversalOrder.add(origin);
        vertexQueue.add(originVertex);

        while (!vertexQueue.isEmpty()) {
            VertexInterface<T> frontVertex = vertexQueue.remove();
            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

            while (neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    traversalOrder.add(nextNeighbor.getLabel());
                    vertexQueue.add(nextNeighbor);
                }
            }
        }
        return traversalOrder;
    }

    /**
     * Retrieves a queue of label objects representing the depth-first traversal of the graph. This starts at
     * the vertex corresponding to the origin label, and visits any other vertices connected to the origin.
     * @param origin An object that labels the origin vertex of the traversal.
     * @return A queue of label objects in the order desired for depth-first traversal from the origin.
     */
    public Queue<T> getDepthFirstTraversal(T origin) {
        if (isEmpty())
            return null;

        resetVertices();
        Queue<T> traversalOrder = new LinkedList<T>();
        Stack<VertexInterface<T>> vertexStack = new Stack<>();

        VertexInterface<T> originVertex = vertices.get(origin);
        originVertex.visit();
        traversalOrder.add(origin);
        vertexStack.push(originVertex);

        while (!vertexStack.isEmpty()) {
            VertexInterface<T> topVertex = vertexStack.peek();
            VertexInterface<T> nextNeighbor = topVertex.getUnvisitedNeighbor();

            if (nextNeighbor != null) {
                nextNeighbor.visit();
                traversalOrder.add(nextNeighbor.getLabel());
                vertexStack.push(nextNeighbor);
            }
            else
                vertexStack.pop();
        }
        return traversalOrder;
    }

    /**
     * Retrieves a stack of label objects representing the topological order of the vertices in an acyclic directed
     * graph. This traversal is impossible for undirected graphs or graphs where any vertices are connected by
     * edges in a loop.
     * @return A stack of label objects representing a valid topological ordering of the vertices' label objects.
     */
    public Stack<T> getTopologicalOrder() {
        resetVertices();
        Stack<T> vertexStack = new Stack<>();
        int numberOfVertices = getNumberOfVertices();
        for (int counter = 1; counter <= numberOfVertices; counter++) {
            VertexInterface<T> nextVertex = findTerminal();
            if (nextVertex == null)
                throw new UnsupportedOperationException("Cannot get topological order for cyclic graph.");
            nextVertex.visit();
            vertexStack.push(nextVertex.getLabel());
        }
        return vertexStack;
    }

    /**
     * Locates a terminal vertex in the graph which has no edges pointing to other vertexes. Used for retrieving the
     * topological order.
     * @return
     */
    protected VertexInterface<T> findTerminal() {
        VertexInterface<T> returnVertex;
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        while (vertexIterator.hasNext()) {
            returnVertex = vertexIterator.next();
            if (!returnVertex.isVisited() && returnVertex.getUnvisitedNeighbor() == null)
                return returnVertex;
        }
        return null;
    }

    /**
     * Calculates the shortest path between two specified vertices in the graph in number of vertices.
     * @param begin An object that labels the path's origin vertex.
     * @param end An object that labels the path's destination vertex.
     * @param path A stack of labels that is empty initially; at the completion of the method, this stack contains
     * the labels of the vertices along the shortest path; the label of the origin vertex is at the top, and
     * the label of the destination vertex is at the bottom
     * @return The length of the shortest path between the two specified vertices in number of vertices.
     */
    public int getShortestPath(T begin, T end, Stack<T> path) {
        resetVertices();
        boolean done = false;
        Queue<VertexInterface<T>> vertexQueue = new LinkedList<>();
        VertexInterface<T> originVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        originVertex.visit();

        vertexQueue.add(originVertex);
        while (!done && !vertexQueue.isEmpty()) {
            VertexInterface<T> frontVertex = vertexQueue.remove();
            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
            while (!done && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.add(nextNeighbor);
                }

                if (nextNeighbor.equals(endVertex))
                    done = true;
            }
        }

        int pathLength = (int)endVertex.getCost();
        path.push(endVertex.getLabel());

        VertexInterface<T> vertex = endVertex;
        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        return pathLength;
    }

    /** Finds the least-cost path between two given vertices in this graph.
     * @param begin An object that labels the path's origin vertex.
     * @param end An object that labels the path's destination vertex.
     * @param path A stack of labels that is empty initially; at the completion of the method, this stack contains
     * the labels of the vertices along the cheapest path; the label of the origin vertex is at the top, and
     * the label of the destination vertex is at the bottom
     * @return  The cost of the cheapest path.
     */
    public double getCheapestPath(T begin, T end, Stack<T> path) {
        if (path == null)
            return 0;

        resetVertices();
        boolean done = false;

        PriorityQueue<EntryPQ> priorityQueue = new PriorityQueue<>();

        VertexInterface<T> originVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);

        priorityQueue.add(new EntryPQ(originVertex, 0, null));

        while (!done && !priorityQueue.isEmpty()) {
            EntryPQ frontEntry = priorityQueue.remove();
            VertexInterface<T> frontVertex = frontEntry.getVertex();

            if (!frontVertex.isVisited()) {
                frontVertex.visit();
                frontVertex.setCost(frontEntry.getCost());
                frontVertex.setPredecessor(frontEntry.getPredecessor());

                if (frontVertex.equals(endVertex))
                    done = true;
                else {
                    Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
                    Iterator<Double> edgeWeights = frontVertex.getWeightIterator();
                    while (neighbors.hasNext()) {
                        VertexInterface<T> nextNeighbor = neighbors.next();
                        Double weightOfEdgeToNeighbor = edgeWeights.next();

                        if (!nextNeighbor.isVisited()) {
                            double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
                            priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
                        }
                    }
                }
            }
        }

        double pathCost = endVertex.getCost();
        path.push(endVertex.getLabel());

        VertexInterface<T> vertex = endVertex;
        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        return pathCost;
    }

    /**
     * Prints a textual representation of all the vertices in the graph, along with their edge destinations
     * and weights. For testing.
     */
    public void printVertices() {
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        while (vertexIterator.hasNext())
            ((Vertex<T>)(vertexIterator.next())).printVertex();
    }

    /**
     * EntryPQ: An implementation of a priority queue entry used for getCheapestPath method. We use EntryPQ instead of
     * queueing vertices directly because multiple entries contain the same vertex but different costs. Cost of path
     * to vertex is each EntryPQ's priority value in getCheapestPath's queue.
     */
    private class EntryPQ implements Comparable<EntryPQ> {
        /** The destination vertex of this segment of the path. */
        private VertexInterface<T> vertex;
        /** The origin vertex of this segment of the path. */
        private VertexInterface<T> previousVertex;
        /** The traversal cost of the edge between the previous two paths. */
        private double cost;

        /**
         * Constructor creates a new EntryPQ object with the specified origin and destination vertices and the
         * traversal cost of the edge between them.
         * @param vertex The destination vertex of this segment of the path.
         * @param cost The traversal cost between the two vertices.
         * @param previousVertex The origin vertex of this segment of the path.
         */
        private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex) {
            this.vertex = vertex;
            this.previousVertex = previousVertex;
            this.cost = cost;
        }

        /**
         * Retrieves the destination vertex of this segment of the path.
         * @return The destination vertex of this segment of the path.
         */
        public VertexInterface<T> getVertex() { return vertex; }

        /**
         * Retrieves the origin vertex of this segment of the path.
         * @return The origin vertex of this segment of the path.
         */
        public VertexInterface<T> getPredecessor() { return previousVertex; }

        /**
         * Retrieves the traversal cost for this segment of the path.
         * @return The traversal cost for this segment of the path.
         */
        public double getCost() { return cost; }

        /**
         * Compares the traversal cost of this path to the traversal cost of another path segment, in order to
         * calculate the least expensive path.
         * @param otherEntry The other path segment to compare to.
         * @return -1 if this segment is less expensive, 1 if the other segment is less expensive, or 0 if they
         * are equal.
         */
        public int compareTo(EntryPQ otherEntry) { return Double.compare(cost, otherEntry.getCost()); }

        /**
         * Creates a textual representation of this path segment's destination vertex and traversal cost.
         * @return A textual representation of this path segment's destination vertex and traversal cost.
         */
        public String toString() { return vertex.toString() + " " + cost; }
    }
}