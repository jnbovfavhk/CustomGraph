import org.apache.commons.lang3.tuple.MutablePair;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        // Направленный, невзвешенный
        CustomGraph orientedGraph = new CustomGraph();
        orientedGraph.addNode("Москва");
        orientedGraph.addNode("Санкт-Петербург");
        orientedGraph.addNode("Архангельск");
        orientedGraph.addNode("Тверь");
        orientedGraph.addNode("Саратов");
        orientedGraph.addNode("Изолированный секретный город");
        orientedGraph.addNode("Новосибирск");
        orientedGraph.addNode("Астрахань");

        orientedGraph.addOrientedEdge("Москва", "Санкт-Петербург");
        orientedGraph.addOrientedEdge("Москва", "Архангельск");
        orientedGraph.addOrientedEdge("Москва", "Тверь");
        orientedGraph.addOrientedEdge("Москва", "Саратов");
        orientedGraph.addOrientedEdge("Москва", "Астрахань");
        orientedGraph.addOrientedEdge("Санкт-Петербург", "Архангельск");
        orientedGraph.addOrientedEdge("Санкт-Петербург", "Новосибирск");
        orientedGraph.addOrientedEdge("Санкт-Петербург", "Саратов");
        orientedGraph.addOrientedEdge("Саратов", "Астрахань");
        orientedGraph.addOrientedEdge("Архангельск", "Новосибирск");
        orientedGraph.addOrientedEdge("Тверь", "Тверь");

        orientedGraph.saveGraph("orientedGraph.json");


        CustomGraph newGraph = new CustomGraph("orientedGraph.json");
        newGraph.saveGraph("og2save.json");

        Set<GraphObj> fromSP = newGraph.returnOneSideNeighboursList("Санкт-Петербург");
        System.out.println("Вершины, выходящие из Санкт-Перетрбурга:");
        System.out.println(fromSP);
        MutablePair<Set<GraphObj>, Set<GraphObj>> twoSidesFromSP = newGraph.returnAllNeighboursList("Санкт-Петербург");
        System.out.println("Выходящие: ");
        System.out.println(twoSidesFromSP.left);
        System.out.println("Заходящие: ");
        System.out.println(twoSidesFromSP.right);


        CustomGraph task4Graph = new CustomGraph();
        task4Graph.addNode("a");
        task4Graph.addNode("b");
        task4Graph.addNode("c");
        task4Graph.addNode("d");
        task4Graph.addNode("e");
        task4Graph.addNode("f");
        task4Graph.addNode("g");
        task4Graph.addNode("h");
        task4Graph.addNode("j");
        task4Graph.addNode("k");
        task4Graph.addEdge("a", "b");
        task4Graph.addEdge("c", "b");
        task4Graph.addEdge("c", "d");
        task4Graph.addEdge("d", "g");
        task4Graph.addEdge("j", "k");
        task4Graph.addEdge("h", "f");
        task4Graph.addEdge("f", "a");
        task4Graph.addEdge("e", "e");
        task4Graph.addEdge("g", "k");
        task4Graph.addEdge("e", "f");

        CustomGraph removedGraph = task4Graph.removeEdgesBetweenSameDegree();
        removedGraph.saveGraph("4thTaskGraph.json");

        ConsoleInterface.execute();
    }
}
