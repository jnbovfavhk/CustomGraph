import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Ненаправленный, невзвешенный
        CustomGraph graph = new CustomGraph();
        graph.addNode("Москва");
        graph.addNode("Санкт-Петербург");
        graph.addNode("Архангельск");
        graph.addNode("Тверь");
        graph.addNode("Саратов");
        graph.addNode("Изолированный секретный город");
        graph.addNode("Новосибирск");
        graph.addNode("Астрахань");

        graph.addEdge("Москва", "Санкт-Петербург");
        graph.addEdge("Москва", "Архангельск");
        graph.addEdge("Москва", "Тверь");
        graph.addEdge("Москва", "Саратов");
        graph.addEdge("Москва", "Астрахань");
        graph.addEdge("Санкт-Петербург", "Архангельск");
        graph.addEdge("Санкт-Петербург", "Новосибирск");
        graph.addEdge("Санкт-Петербург", "Саратов");
        graph.addEdge("Саратов", "Астрахань");
        graph.addEdge("Архангельск", "Новосибирск");
        graph.addEdge("Тверь", "Тверь");


        CustomGraph graphCopy = new CustomGraph(graph);
        graphCopy.saveGraph("graphCopy.json");

        graph.removeEdge("Москва", "Санкт-Петербург");
        graph.saveGraph("graph.json");


        // Ненаправленный, взвешенный
        CustomGraph weightedGraph = new CustomGraph();
        weightedGraph.addNode("Москва");
        weightedGraph.addNode("Санкт-Петербург");
        weightedGraph.addNode("Архангельск");
        weightedGraph.addNode("Тверь");
        weightedGraph.addNode("Саратов");
        weightedGraph.addNode("Изолированный секретный город");
        weightedGraph.addNode("Новосибирск");
        weightedGraph.addNode("Астрахань");

        weightedGraph.addEdge("Москва", "Санкт-Петербург", 400);
        weightedGraph.addEdge("Москва", "Архангельск", 1000);
        weightedGraph.addEdge("Москва", "Тверь", 100);
        weightedGraph.addEdge("Москва", "Саратов", 1800);
        weightedGraph.addEdge("Москва", "Астрахань", 2000);
        weightedGraph.addEdge("Санкт-Петербург", "Архангельск", 1300);
        weightedGraph.addEdge("Санкт-Петербург", "Новосибирск", 3000);
        weightedGraph.addEdge("Санкт-Петербург", "Саратов", 1600);
        weightedGraph.addEdge("Саратов", "Астрахань", 200);
        weightedGraph.addEdge("Архангельск", "Новосибирск", 1000);
        weightedGraph.addEdge("Тверь", "Тверь", 20);

        weightedGraph.saveGraph("weightedGraph.json");

        // Направленный, взвешенный
        CustomGraph orientedWeightedGraph = new CustomGraph();
        orientedWeightedGraph.addNode("Москва");
        orientedWeightedGraph.addNode("Санкт-Петербург");
        orientedWeightedGraph.addNode("Архангельск");
        orientedWeightedGraph.addNode("Тверь");
        orientedWeightedGraph.addNode("Саратов");
        orientedWeightedGraph.addNode("Изолированный секретный город");
        orientedWeightedGraph.addNode("Новосибирск");
        orientedWeightedGraph.addNode("Астрахань");

        orientedWeightedGraph.addOrientedEdge("Москва", "Санкт-Петербург", 400);
        orientedWeightedGraph.addOrientedEdge("Москва", "Архангельск", 1000);
        orientedWeightedGraph.addOrientedEdge("Москва", "Тверь", 100);
        orientedWeightedGraph.addOrientedEdge("Москва", "Саратов", 1800);
        orientedWeightedGraph.addOrientedEdge("Москва", "Астрахань", 2000);
        orientedWeightedGraph.addOrientedEdge("Санкт-Петербург", "Архангельск", 1300);
        orientedWeightedGraph.addOrientedEdge("Санкт-Петербург", "Новосибирск", 3000);
        orientedWeightedGraph.addOrientedEdge("Санкт-Петербург", "Саратов", 1600);
        orientedWeightedGraph.addOrientedEdge("Саратов", "Астрахань", 200);
        orientedWeightedGraph.addOrientedEdge("Архангельск", "Новосибирск", 1000);
        orientedWeightedGraph.addOrientedEdge("Тверь", "Тверь", 20);

        orientedWeightedGraph.saveGraph("orientedWeightedGraph.json");

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

        newGraph.printOneSideNeighboursList("Санкт-Петербург");
        newGraph.printAllNeighboursList("Санкт-Петербург");


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

        task4Graph.removeEdgesBetweenSameDegree();
        task4Graph.saveGraph("4thTaskGraph.json");

//        ConsoleInterface.execute();
    }
}
