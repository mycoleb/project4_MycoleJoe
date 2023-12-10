package GraphPackage;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.ArrayList;

/**
 * Vertex: An implementation of a generic graph vertex that stores a label object of the specified type.
 * @param <T> The data type of the label object.
 */
class Vertex<T> implements VertexInterface<T> {
    /** The label object to store in this vertex. */
    private final T label;
    /** A list of Edge objects that point to other vertices. */
    private final ArrayList<Edge> edgeList;
    /** Stores the visited status of this vertex for graph traversal.  */
    private boolean visited;
    /** Stores a reference to the previous vertex in the path to this vertex. */
    private VertexInterface<T> previousVertex;
    /** Stores the traversal cost for the path to this vertex. */
    private double cost;

    /**
     * Constructor creates a new Vertex option, storing the specified label object.
     * @param vertexLabel The label object to store in this vertex.
     */
    public Vertex(T vertexLabel) {
        label = vertexLabel;
        edgeList = new ArrayList<>();
        visited = false;
        previousVertex = null;
        cost = 0;
    }

    /**
     * Retrieves the label object stored in this vertex.
     * @return The stored label object.
     */
    public T getLabel() { return label; }

    /**
     * Indicates whether this vertex has a predecessor set.
     * @return True if there is a predecessor vertex, or false otherwise.
     */
    public boolean hasPredecessor() { return previousVertex != null; }

    /**
     * Sets the predecessor vertex to this vertex along a path.
     * @param predecessor The vertex previous to this one along a path.
     */
    public void setPredecessor(VertexInterface<T> predecessor) { previousVertex = predecessor; }

    /**
     * Retrieves the predecessor vertex to this vertex along a path.
     * @return The vertex previous to this one along a path.
     */
    public VertexInterface<T> getPredecessor() { return previousVertex; }

    /**
     * Sets this vertex to indicate that it has been visited during traversal.
     */
    public void visit() { visited = true; }

    /**
     * Sets this vertex to indicate that it has not been visited during traversal.
     */
    public void unvisit() { visited = false; }

    /**
     * Determines whether this node has been visited during traversal.
     * @return True if this node has been visited, or false if it has not been visited.
     */
    public boolean isVisited() { return visited; }

    /**
     * Sets the traversal cost to reach this vertex.
     * @param newCost The traversal cost of the path to this vertex.
     */
    public void setCost(double newCost) { cost = newCost; }

    /**
     * Retrieves the traversal cost to reach this vertex.
     * @return The traversal cost of the path to this vertex.
     */
    public double getCost() { return cost; }

    /**
     * Creates a textual representation of the label object stored in this vertex.
     * @return A textual representation of the label object.
     */
    public String toString() { return label.toString(); }

    /**
     * Creates a new edge from this vertex to the specified end vertex, with the specified edge weight.
     * @param endVertex A vertex in the graph that ends the edge.
     * @param edgeWeight A real-valued edge weight.
     * @return True if there was a new edge added, or false if the edge points to this vertex or already exists.
     */
    public boolean connect(VertexInterface<T> endVertex, double edgeWeight) {
        if (this.equals(endVertex))
            return false;
        if (this.hasNeighbor(endVertex.getLabel()))
            return false;

        edgeList.add(new Edge(endVertex, edgeWeight));
        return true;
    }

    /**
     * Creates a new edge from this vertex to the specified end vertex, with the default weight of 0.
     * @param endVertex A vertex in the graph that ends the edge.
     * @return True if there was a new edge added, or false if the edge points to this vertex or already exists.
     */
    public boolean connect(VertexInterface<T> endVertex) { return connect(endVertex, 0); }

    /**
     * Removes an existing edge from this vertex to the specified end vertex.
     * @param endVertex A vertex in the graph that ends the edge.
     * @return True if there was an edge removed, or false if there is no edge connecting this vertex to the
     * specified end vertex.
     */
    public boolean disconnect(VertexInterface<T> endVertex) {
        for (Edge curEdge : edgeList) {
            if (curEdge.getEndVertex() == endVertex) {
                edgeList.remove(curEdge);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a list of all the label objects of all vertices that this vertex has an edge pointing to.
     * @return A list of the label objects of all neighboring vertices.
     */
    public ArrayList<T> getNeighborLabels(double minWeight) {
        if (!this.hasNeighbor())
            return null;

        Iterator<Edge> neighborIter = edgeList.iterator();
        ArrayList<T> returnList = new ArrayList<>();

        while (neighborIter.hasNext()) {
            Edge curEdge = neighborIter.next();
            if (curEdge.getWeight() >= minWeight)
                returnList.add(curEdge.getEndVertex().getLabel());
        }

        return returnList;
    }

    /**
     * Indicates whether this vertex has a neighbor containing the specified label object.
     * @param searchLabel The label object to search for.
     * @return True if this vertex has a neighbor with the specified label object, or false otherwise.
     */
    public boolean hasNeighbor(T searchLabel) {
        if (!this.hasNeighbor())
            return false;

        return this.getNeighborLabels(Double.MIN_VALUE).contains(searchLabel);
    }

    /**
     * Indicates whether this vertex has any edges pointing to other vertices.
     * @return True if this vertex has any neighbors, or false if it does not.
     */
    public boolean hasNeighbor() { return !edgeList.isEmpty(); }

    /**
     * Retrieves a reference to the first neighboring vertex that has not been marked as visited.
     * @return The first neighboring vertex that has not been marked as visited.
     */
    public VertexInterface<T> getUnvisitedNeighbor() {
        Iterator<VertexInterface<T>> neighbors = getNeighborIterator();

        while (neighbors.hasNext()) {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (!nextNeighbor.isVisited())
                return nextNeighbor;
        }
        return null;
    }

    /**
     * Compares the label object of this vertex with the label object of another vertex.
     * @param other The other vertex to compare to.
     * @return True if the two label objects are the same, or false if they are not.
     */
    public boolean equals(Object other) {
        if ((other == null) || (getClass() != other.getClass()))
            return false;
        else {
            // The cast is safe within this else clause
            @SuppressWarnings("unchecked")
            Vertex<T> otherVertex = (Vertex<T>)other;
            return label.equals(otherVertex.label);
        }
    }

    /**
     * Prints a text representation of the contents of this vertex's label object and the label objects of all
     * of the neighbors of this vertex, along with their respective edge weights.
     */
    public void printVertex() {
        StringJoiner vertexString = new StringJoiner(", ");

        System.out.print("Vertex: " + label + " - Edges: " );
        Iterator<VertexInterface<T>> vertexIter = getNeighborIterator();
        Iterator<Double> weightIter = getWeightIterator();
        if (!vertexIter.hasNext())
            vertexString.add("None; terminal");

        while (vertexIter.hasNext())
            vertexString.add(vertexIter.next().getLabel() + " - W: " + weightIter.next());

        System.out.println(vertexString);
    }

    /**
     * Creates a new iterator that iterates through all vertices that neighbor this one.
     * @return An iterator object, ready for use.
     */
    public Iterator<VertexInterface<T>> getNeighborIterator() { return new NeighborIterator(); }

    /**
     * Creates a new iterator that iterates through the edge weights of all vertices that neighbor this one.
     * @return An iterator object, ready for use.
     */
    public Iterator<Double> getWeightIterator() { return new WeightIterator(); }

    /**
     * NeighborIterator: An implementation of an iterator that iterates through all vertices that neighbor this one.
     */
    private class NeighborIterator implements Iterator<VertexInterface<T>> {
        /** An iterator to traverse the list of edges. */
        private final Iterator<Edge> edges;

        /**
         * Creates a new NeighborIterator object by instantiating the edge list iterator.
         */
        private NeighborIterator() { edges = edgeList.iterator(); }

        /**
         * Indicates whether there are more neighboring vertices to iterate to.
         * @return True if there are more neighboring vertices to iterate to, false if not.
         */
        public boolean hasNext() { return edges.hasNext(); }

        /**
         * Retrieves a reference to the next vertex that neighbors this one.
         * @return A reference to the next neighbor of this vertex.
         */
        public VertexInterface<T> next() {
            VertexInterface<T> nextNeighbor = null;

            if (edges.hasNext()) {
                Edge edgeToNextNeighbor = edges.next();
                nextNeighbor = edgeToNextNeighbor.getEndVertex();
            }
            else
                throw new NoSuchElementException();

            return nextNeighbor;
        }
    }

    /**
     * WeightIterator: An implementation of an iterator that iterates through all the edges that start with this
     * vertex and retrieves their edge weights.
     */
    private class WeightIterator implements Iterator<Double> {
        /** An iterator to traverse the list of edges. */
        private final Iterator<Edge> edges;

        /**
         * Creates a new WeightIterator object by instantiating the edge list iterator.
         */
        private WeightIterator() { edges = edgeList.iterator(); }

        /**
         * Indicates whether there are any more edges to retrieve the weights from.
         * @return True if there are more edges to retrieve, false if there are not.
         */
        public boolean hasNext() { return edges.hasNext(); }

        /**
         * Retrieves the weight of the next edge starting with this vertex.
         * @return The weight of the next edge starting with this vertex.
         */
        public Double next() {
            double edgeWeight = 0.0;
            if (edges.hasNext())
            {
                Edge edgeToNextNeighbor = edges.next();
                edgeWeight = edgeToNextNeighbor.getWeight();
            }
            else
                throw new NoSuchElementException();

            return edgeWeight;
        }
    }

    /**
     * Edge: An implementation of a graph edge which directionally connects two vertices.
     */
    protected class Edge {
        /** A reference to the vertex that this edge points to. */
        private final VertexInterface<T> vertex;
        /** The weight of this edge. */
        private final double weight;

        /**
         * Constructor creates a new edge which points to the specified vertex, with the specified weight.
         * @param endVertex The vertex this edge points to.
         * @param edgeWeight The weight of this edge.
         */
        protected Edge(VertexInterface<T> endVertex, double edgeWeight) {
            vertex = endVertex;
            weight = edgeWeight;
        }

        /**
         * Constructor creates a new edge which points to the specified verted, with the default weight of 0.
         * @param endVertex The vertex this edge points to.
         */
        protected Edge(VertexInterface<T> endVertex) {
            vertex = endVertex;
            weight = 0;
        }

        /**
         * Retrieves the vertex that this edge points to.
         * @return A reference to the vertex that this edge points to.
         */
        protected VertexInterface<T> getEndVertex() { return vertex; }

        /**
         * Retrieves the weight of this edge.
         * @return The weight of this edge.
         */
        protected double getWeight() { return weight; }
    }
}