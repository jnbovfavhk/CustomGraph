import org.apache.commons.lang3.tuple.MutablePair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class CustomGraph {
    private HashMap<GraphObj, HashMap<GraphObj, Double>> adjacencyList;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();
    private final Type graphType = new TypeToken<HashMap<GraphObj, HashMap<GraphObj, Double>>>() {}.getType();


    // Конструкторы
    // Пустой
    public CustomGraph() {
        adjacencyList = new HashMap<>();

    }

    // Из файла
    public CustomGraph(String fileName) throws IOException {
        adjacencyList = loadGraph(fileName);

    }

    // Конструктор-копия
    public CustomGraph(CustomGraph g) {
        adjacencyList = g.getAdjacencyList();
    }

    // Геттеры
    public Integer getNodeCount() {
        return adjacencyList.size();
    }

    public HashMap<GraphObj, HashMap<GraphObj, Double>> getAdjacencyList() {
        return adjacencyList;
    }

    // Методы
    // Добавить вершину(без соседей)
    public void addNode(String nodeName) {
        adjacencyList.put(new GraphObj(nodeName), new HashMap<>());
    }

    // Добавить двустороннее ребро без веса
    public int addEdge(String node1, String node2) {
        return addEdge(node1, node2, 0);
    }

    // Добавить двустороннее ребро с весом
    public int addEdge(String node1, String node2, double weight) {

        GraphObj node1Obj = new GraphObj(node1);
        GraphObj node2Obj = new GraphObj(node2);

        if (adjacencyList.containsKey(node1Obj) && adjacencyList.containsKey(node2Obj)) {
            adjacencyList.get(node1Obj).put(new GraphObj(node2), weight);
            adjacencyList.get(node2Obj).put(new GraphObj(node1), weight);
            return 0;
        }

        return -1;
    }

    // Добавить одностороннее ребро с весом
    public int addOrientedEdge(String node1, String node2, double weight) {
        GraphObj node1Obj = new GraphObj(node1);
        GraphObj node2Obj = new GraphObj(node2);

        if (adjacencyList.containsKey(node1Obj) && adjacencyList.containsKey(node2Obj)) {
            adjacencyList.get(node1Obj).put(new GraphObj(node2), weight);
            return 0;
        }

        return -1;
    }

    // Добавить одностороннее ребро без веса
    public int addOrientedEdge(String node1, String node2) {
        return addOrientedEdge(node1, node2, 0);
    }


    // Удалить вершину
    public int removeNode(String node) {

        GraphObj nodeObj = new GraphObj(node);

        adjacencyList.remove(nodeObj);

        removeEdgesTo(nodeObj);

        return 0;
    }

    // Удалить все ребра, связанные с вершиной
    private void removeEdgesTo(GraphObj node) {
        for (GraphObj key : adjacencyList.keySet()) {
            adjacencyList.get(key).remove(node);
        }
    }

    // Удалить двустороннее ребро
    public int removeEdge(String node1, String node2) {
        GraphObj node1Obj = new GraphObj(node1);
        GraphObj node2Obj = new GraphObj(node2);
        if (adjacencyList.containsKey(node1Obj) && adjacencyList.containsKey(node2Obj)) {
            adjacencyList.get(node1Obj).remove(node2Obj);
            adjacencyList.get(node2Obj).remove(node1Obj);
            return 0;
        }

        return -1;
    }

    // Удалить одностороннее ребро
    public int removeOrientedEdge(String node1, String node2) {
        GraphObj node1Obj = new GraphObj(node1);
        if (adjacencyList.containsKey(node1Obj)) {
            adjacencyList.get(node1Obj).remove(new GraphObj(node2));
        }
        return 0;
    }



    // Сохранить в JSON-формате
    public void saveGraph(String filename)
            throws IOException {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(adjacencyList, writer);
        }
    }

    // Загрузить из JSON-формата
    private HashMap<GraphObj, HashMap<GraphObj, Double>> loadGraph(
            String filename) throws IOException {
        try (Reader reader = new FileReader(filename)) {
            return gson.fromJson(reader, graphType);
        }
    }
}
