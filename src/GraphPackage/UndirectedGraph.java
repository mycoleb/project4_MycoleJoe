package GraphPackage;

import java.util.Stack;

/**
 * UndirectedGraph: An implementation of a generic graph with bidirectional edges between vertices.
 * @param <T> The dada type to store in vertex labels.
 */
public class UndirectedGraph<T> extends DirectedGraph<T> implements GraphInterface<T> {
    /**
     * Constructor creates a new UndirectedGraph object with no vertices or edges.
     */
    public UndirectedGraph() { super(); }

    /**
     * Creates a new bidirectional edge between one vertex and another with the specified edge weight.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object, distinct from begin, that labels the end vertex of the edge.
     * @param edgeWeight The real value of the edge's weight.
     * @return True if a new edge was added, or false if one of the vertices was not found, or the edge already exists.
     */
    public boolean addEdge(T begin, T end, double edgeWeight) {
        return super.addEdge(begin, end, edgeWeight) &&
                super.addEdge(end, begin, edgeWeight);
    }

    /**
     * Creates a new bidirectional edge between one vertex and another with the default edge weight of 0.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object, distinct from begin, that labels the end vertex of the edge.
     * @return True if a new edge was added, or false if one/both of the vertices was not found, or the edge
     * already exists.
     */
    public boolean addEdge(T begin, T end) { return this.addEdge(begin, end, 0); }

    /**
     * Removes a bidirectional edge between one vertex and another.
     * @param begin An object that labels the origin vertex of the edge.
     * @param end An object, distinct from begin, that labels the end vertex of the edge.
     * @return True if the edge was removed, or false if the edge/vertex was not found.
     */
    public boolean removeEdge(T begin, T end) {
        return super.removeEdge(begin, end) && super.removeEdge(end, begin);
    }

    /**
     * Override method returns the number of bidirectional edges in the graph, which is half of the total edges
     * since two edges exist between connected vertices.
     * @return The number of bidirectional edges in the graph.
     */
    public int getNumberOfEdges() { return super.getNumberOfEdges() / 2; }

    /**
     * Override method disables the retrieval of topological ordering of the label objects because topological
     * ordering can only be determined for acyclic directional graphs.
     * @throws UnsupportedOperationException Topological ordering cannot be determined for an undirected graph.
     * @return Nothing
     */
    public Stack<T> getTopologicalOrder() {
        throw new UnsupportedOperationException("Topological sort is illegal in an undirected graph.");
    }
}
