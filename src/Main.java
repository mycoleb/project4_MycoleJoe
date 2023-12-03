
import GraphPackage.*;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        testGraph();
    }

    public static void testGraph() {
        DirectedGraph<String> testDG = new DirectedGraph<>();

        System.out.println("Testing adding vertexes:");
        System.out.println("Adding vertex Seattle; result: " + testDG.addVertex("Seattle"));
        System.out.println("Adding vertex Barcelona; result: " + testDG.addVertex("Barcelona"));
        System.out.println("Adding vertex Paris; result: " + testDG.addVertex("Paris"));
        System.out.println("Adding vertex Mumbai; result: " + testDG.addVertex("Mumbai"));
        System.out.println("Adding vertex Tokyo; result: " + testDG.addVertex("Tokyo"));
        System.out.println("Adding vertex Lima; result: " + testDG.addVertex("Lima"));
        System.out.println("Adding vertex Seattle (duplicate); result: " + testDG.addVertex("Seattle"));

        System.out.println("\nTesting adding edges:");
        System.out.println("Adding edge from Seattle to Barcelona with a weight of 5; result: " +
                testDG.addEdge("Seattle", "Barcelona", 5));
        System.out.println("Adding edge from Barcelona to Tokyo with a weight of 6; result: " +
                testDG.addEdge("Barcelona", "Tokyo", 10));
        System.out.println("Adding edge from Tokyo to Barcelona with a weight of 1; result: " +
                testDG.addEdge("Tokyo", "Barcelona", 1));
        System.out.println("Adding edge from Mumbai to Seattle with a weight of 9; result: " +
                testDG.addEdge("Mumbai", "Seattle", 9));
        System.out.println("Adding edge from Paris to Mumbai with a weight of 7; result: " +
                testDG.addEdge("Paris", "Mumbai", 7));
        System.out.println("Adding edge from Paris to Seattle with a weight of 8; result: " +
                testDG.addEdge("Paris", "Seattle", 8));
        System.out.println("Adding edge from Barcelona to Paris with a weight of 3; result: " +
                testDG.addEdge("Barcelona", "Paris", 3));
        System.out.println("Adding edge from Paris to Tokyo with a weight of 2; result: " +
                testDG.addEdge("Paris", "Tokyo", 2));
        System.out.println("Adding edge from Tokyo to Lima with a weight of 7; result: " +
                testDG.addEdge("Tokyo", "Lima", 7));
        System.out.println("Adding edge from Mumbai to Lima with a weight of 2; result: " +
                testDG.addEdge("Mumbai", "Lima", 2));

        System.out.println("\nAdding edge from Seattle to Seattle (cyclic); result: " +
                testDG.addEdge("Seattle", "Seattle"));
        System.out.println("Adding edge from Barcelona to Tokyo (duplicate); result: " +
                testDG.addEdge("Barcelona", "Tokyo"));
        System.out.println("Adding edge from Moscow (not in graph) to Tokyo; result: " +
                testDG.addEdge("Moscow", "Tokyo"));
        System.out.println("Adding edge from Mumbai to Shanghai (not in graph); result: " +
                testDG.addEdge("Mumbai", "Shanhghai"));

        System.out.println("\nCurrent state of the graph:");
        testDG.printVertices();

        System.out.println("\nIs the graph empty: " + testDG.isEmpty());
        System.out.println("Number of vertices in the graph: " + testDG.getNumberOfVertices());
        System.out.println("Number of edges in the graph: " + testDG.getNumberOfEdges());

        System.out.println("\nTesting edge finding:");
        System.out.println("Does Paris have an edge pointing to Seattle? " + testDG.hasEdge("Paris", "Seattle"));
        System.out.println("Does Seattle have an edge pointing to Paris? " + testDG.hasEdge("Seattle", "Paris"));
        System.out.println("Does Barcelona have an edge pointing to Mumbai? " + testDG.hasEdge("Barcelona", "Mumbai"));
        System.out.println("Does Tokyo have an edge pointing to Barcelona? " + testDG.hasEdge("Tokyo", "Barcelona"));
        System.out.println("Does Barcelona have an edge pointing to Tokyo? " + testDG.hasEdge("Barcelona", "Tokyo"));

        System.out.println("\nDoes Seattle have an edge pointing to Moscow (not in graph)? " +
                testDG.hasEdge("Seattle", "Moscow"));
        System.out.println("Does Shanghai (not in graph) have an edge pointing to Tokyo? " +
                testDG.hasEdge("Shangai", "Tokyo"));
        System.out.println("Does Barcelona have an edge pointing to Barcelona (cyclic)? " +
                testDG.hasEdge("Barcelona", "Barcelona"));

        System.out.println("\nTesting breadth-first traversal starting from Mumbai:");
        Queue<String> locationQueue = testDG.getBreadthFirstTraversal("Mumbai");
        while (!locationQueue.isEmpty())
            System.out.print(locationQueue.remove() + " ");
        System.out.print("\n");

        System.out.println("\nTesting depth-first traversal starting from Mumbai:");
        locationQueue = testDG.getDepthFirstTraversal("Mumbai");
        while (!locationQueue.isEmpty())
            System.out.print(locationQueue.remove() + " ");
        System.out.print("\n");

        System.out.println("\nTesting shortest path from Seattle to Lima...");
        Stack<String> locationStack = new Stack<>();
        int pathLength = testDG.getShortestPath("Seattle", "Lima", locationStack);
        System.out.println("Path length: " + pathLength);
        System.out.print("Locations along path: ");
        while (!locationStack.isEmpty())
            System.out.print(locationStack.pop() + " ");
        System.out.print("\n");

        System.out.println("\nTesting cheapest path from Seattle to Lima...");
        locationStack = new Stack<>();
        double pathCost = testDG.getCheapestPath("Seattle", "Lima", locationStack);
        System.out.println("Path cost: " + pathCost);
        System.out.print("Locations along path: ");
        while (!locationStack.isEmpty())
            System.out.print(locationStack.pop() + " ");
        System.out.print("\n");

        System.out.println("\nTesting neighbor retrieval:");
        System.out.print("Neighbors of Paris: ");
        ArrayList<String> parisNeighbors = testDG.getNeighbors("Paris");
        for (String neighbor : parisNeighbors)
            System.out.print(neighbor + " ");
        System.out.print("\n");

        System.out.println("\nTesting edge/vertex removal:");
        System.out.println("Removing edge from Mumbai to Lima; result: " + testDG.removeEdge("Mumbai", "Lima"));
        System.out.println("Removing edge from Seattle to Tokyo (edge does not exist); result: " +
                testDG.removeEdge("Seattle", "Tokyo"));
        System.out.println("Removing edge from Shanghai (not in graph) to Barcelona; result: " +
                testDG.removeEdge("Shanghai", "Barcelona"));
        System.out.println("Removing vertex Tokyo from the graph; result: " +
                testDG.removeVertex("Tokyo"));
        System.out.println("Removing vertex Lima from the graph; result: " +
                testDG.removeVertex("Lima"));
        System.out.println("Removing vertex Moscow (not in graph) from the graph; result: " +
                testDG.removeVertex("Moscow"));

        System.out.println("\nCurrent state of the graph:");
        testDG.printVertices();

        System.out.println("\nClearing...");
        testDG.clear();
        System.out.println("Is the graph empty: " + testDG.isEmpty());

        System.out.println("\nCreating acyclic graph to test topological order...");
        DirectedGraph<Integer> acyclicDG = new DirectedGraph<>();

        acyclicDG.addVertex(5);
        acyclicDG.addVertex(7);
        acyclicDG.addVertex(3);
        acyclicDG.addVertex(11);
        acyclicDG.addVertex(8);
        acyclicDG.addVertex(2);
        acyclicDG.addVertex(9);
        acyclicDG.addVertex(10);
        acyclicDG.addEdge(5, 11);
        acyclicDG.addEdge(7, 11);
        acyclicDG.addEdge(7, 8);
        acyclicDG.addEdge(3, 8);
        acyclicDG.addEdge(3, 10);
        acyclicDG.addEdge(11, 2);
        acyclicDG.addEdge(11, 9);
        acyclicDG.addEdge(11, 10);
        acyclicDG.addEdge(8, 9);

        System.out.println("\nState of acyclic graph:");
        acyclicDG.printVertices();

        System.out.println("\nTesting topological order retrieval:");
        Stack<Integer> numStack = acyclicDG.getTopologicalOrder();
        while (!numStack.isEmpty())
            System.out.print(numStack.pop() + " ");
        System.out.print("\n");
    }
}
