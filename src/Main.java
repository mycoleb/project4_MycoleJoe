import GraphPackage.Vertex;
import GraphPackage.VertexInterface;

public class Main {
    public static void main(String[] args) {
        Vertex<String> testVertex1 = new Vertex<>("Vertex 1");
        Vertex<String> testVertex2 = new Vertex<>("Vertex 2");
        Vertex<String> testVertex3 = new Vertex<>("Vertex 3");

        System.out.println(testVertex1.connect(testVertex2));
        System.out.println(testVertex1.connect(testVertex3));
        System.out.println(testVertex2.connect(testVertex1));
        System.out.println(testVertex2.connect(testVertex3));
        System.out.println(testVertex3.connect(testVertex1));
        System.out.println(testVertex3.connect(testVertex2));


        System.out.println(testVertex1.connect(testVertex1));
        System.out.println(testVertex3.connect(testVertex2));

        testVertex1.printVertex();
        System.out.println(testVertex1.disconnect(testVertex2));
        testVertex1.printVertex();


        //testVertex2.printVertex();
        //testVertex3.printVertex();


        /*VertexInterface<String> nextUnvisited;
        nextUnvisited = testVertex1.getUnvisitedNeighbor();
        ((Vertex<String>) nextUnvisited).printVertex();*/

    }
}
