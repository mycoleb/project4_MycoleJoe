package GraphPackage;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class DirectedGraph<T> implements GraphInterface<T>
{
    private final HashMap<T, VertexInterface<T>> vertices;
    private int edgeCount;

    public DirectedGraph() {
        vertices = new HashMap<>();
        edgeCount = 0;
    }

    public boolean addVertex(T vertexLabel) {
        if (vertices.containsKey(vertexLabel))
            return false;
        else {
            vertices.put(vertexLabel, new Vertex<>(vertexLabel));
            return true;
        }
    }

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

    public boolean addEdge(T begin, T end) { return addEdge(begin, end, 0); }

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

    public boolean hasEdge(T begin, T end) {
        if (containsLabels(begin, end)) {
            VertexInterface<T> beginVertex = vertices.get(begin);
            VertexInterface<T> endVertex = vertices.get(end);
            ArrayList<T> neighborLabels = beginVertex.getNeighborLabels(Double.MIN_VALUE);
            return neighborLabels.contains(endVertex.getLabel());
        }
        return false;
    }

    private boolean containsLabels(T label1, T label2) {
        if (vertices.containsKey(label1) && vertices.containsKey(label2))
            return true;
        return false;
    }

    public boolean isEmpty() { return vertices.isEmpty(); }

    public void clear() {
        vertices.clear();
        edgeCount = 0;
    }

    public ArrayList<T> getNeighbors(T vertexLabel, double weightMin) {
        if (vertices.containsKey(vertexLabel)) {
            VertexInterface<T> vertexOfLabel = vertices.get(vertexLabel);
            return vertexOfLabel.getNeighborLabels(weightMin);
        }
        return null;
    }

    public int getNumberOfVertices() { return vertices.size(); }

    public int getNumberOfEdges() { return edgeCount; }

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

    public void printVertices() {
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        while (vertexIterator.hasNext())
            ((Vertex<T>)(vertexIterator.next())).printVertex();
    }

    private class EntryPQ implements Comparable<EntryPQ> {
        private VertexInterface<T> vertex;
        private VertexInterface<T> previousVertex;
        private double cost;

        private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex) {
            this.vertex = vertex;
            this.previousVertex = previousVertex;
            this.cost = cost;
        }

        public VertexInterface<T> getVertex() { return vertex; }

        public VertexInterface<T> getPredecessor() { return previousVertex; }

        public double getCost() { return cost; }

        public int compareTo(EntryPQ otherEntry) { return Double.compare(cost, otherEntry.getCost()); }

        public String toString() { return vertex.toString() + " " + cost; }
    }
}