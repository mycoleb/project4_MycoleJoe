package GraphPackage;

import java.util.Stack;

public class UndirectedGraph<T> extends DirectedGraph<T> implements GraphInterface<T> {
    public UndirectedGraph() { super(); }

    public boolean addEdge(T begin, T end, double edgeWeight) {
        return super.addEdge(begin, end, edgeWeight) &&
                super.addEdge(end, begin, edgeWeight);
    }

    public boolean addEdge(T begin, T end) { return this.addEdge(begin, end, 0); }

    public boolean removeEdge(T begin, T end) {
        return super.removeEdge(begin, end) && super.removeEdge(end, begin);
    }

    public int getNumberOfEdges() { return super.getNumberOfEdges() / 2; }

    public Stack<T> getTopologicalOrder() {
        throw new UnsupportedOperationException("Topological sort is illegal in an undirected graph.");
    }
}
