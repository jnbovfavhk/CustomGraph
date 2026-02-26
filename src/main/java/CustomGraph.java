import org.apache.commons.lang3.tuple.MutablePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class CustomGraph {
    private static HashMap<GraphObj, List<Edge>> adjacencyList;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type graphType = new TypeToken<ArrayList<Pair>>() {}.getType();


    // Конструкторы
    // Пустой
    public CustomGraph() {
        adjacencyList = new ArrayList<>();

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

    public List<Pair> getAdjacencyList() {
        return adjacencyList;
    }

    // Методы
    // Добавить вершину(без соседей)
    public void addNode(String nodeName) {
        adjacencyList.add(new Pair(new GraphObj(nodeName), new ArrayList<>()));
    }

    // Добавить двустороннее ребро без веса
    public int addEdge(String node1, String node2) {
        return addEdge(node1, node2, 0);
    }

    // Добавить двустороннее ребро с весом
    public int addEdge(String node1, String node2, int weight) {
        int breakCounter = 2;
        for (Pair graphObjListMutablePair : adjacencyList) {


            if (graphObjListMutablePair.getKey().getName().equals(node1)) {
                graphObjListMutablePair.getValue().add(new Edge(new GraphObj(node2), weight));
                breakCounter -= 1;
            } else if (graphObjListMutablePair.getKey().getName().equals(node2)) {
                graphObjListMutablePair.getValue().add(new Edge(new GraphObj(node1), weight));
                breakCounter -= 1;
            }

            if (breakCounter == 0) {
                return 0;
            }
        }

        return -1;
    }

    // Добавить одностороннее ребро с весом
    public int addOrientedEdge(String node1, String node2, int weight) {
        for (Pair graphObjListMutablePair : adjacencyList) {
            if (graphObjListMutablePair.getKey().getName().equals(node1)) {
                graphObjListMutablePair.getValue().add(new Edge(new GraphObj(node2), weight));
                return 0;
            }
        }

        return -1;
    }

    // Добавить одностороннее ребро без веса
    public int addOrientedEdge(String node1, String node2) {
        return addOrientedEdge(node1, node2, 0);
    }


    // Удалить вершину
    public int removeNode(String node) {
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.get(i).getKey().getName().equals(node)) {
                removeEdgesTo(adjacencyList.get(i).getKey());
                adjacencyList.remove(i);
                return 0;
            }
        }
        return -1;
    }

    // Удалить все ребра, связанные с вершиной
    private void removeEdgesTo(GraphObj node) {
        for (Pair graphObjListMutablePair : adjacencyList) {
            for (int j = 0; j < graphObjListMutablePair.getValue().size(); j++) {
                if (graphObjListMutablePair.getValue().get(j).getTarget().equals(node)) {
                    graphObjListMutablePair.getValue().remove(j);
                    break;
                }
            }

        }
    }

    // Удалить двустороннее ребро
    public int removeEdge(String node1, String node2) {
        int breakCounter = 2;

        for (Pair graphObjListMutablePair : adjacencyList) {


            if (graphObjListMutablePair.getKey().getName().equals(node1)) {
                for (int j = 0; j < graphObjListMutablePair.getValue().size(); j++) {
                    if (graphObjListMutablePair.getValue().get(j).getTarget().getName().equals(node2)) {
                        graphObjListMutablePair.getValue().remove(j);
                        breakCounter -= 1;
                        break;
                    }
                }

            } else if (graphObjListMutablePair.getKey().getName().equals(node2)) {
                for (int j = 0; j < graphObjListMutablePair.getValue().size(); j++) {
                    if (graphObjListMutablePair.getValue().get(j).getTarget().getName().equals(node1)) {
                        graphObjListMutablePair.getValue().remove(j);
                        breakCounter -= 1;
                        break;
                    }
                }
            }

            if (breakCounter == 0) {
                return 0;
            }
        }

        return -1;
    }

    // Удалить одностороннее ребро
    public int removeOrientedEdge(String node1, String node2) {
        for (Pair graphObjListMutablePair : adjacencyList) {
            if (graphObjListMutablePair.getKey().getName().equals(node1)) {
                for (int j = 0; j < graphObjListMutablePair.getValue().size(); j++) {
                    if (graphObjListMutablePair.getValue().get(j).getTarget().getName().equals(node2)) {
                        graphObjListMutablePair.getValue().remove(j);
                        break;
                    }
                }

            }
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
    private ArrayList<Pair> loadGraph(
            String filename) throws IOException {
        try (Reader reader = new FileReader(filename)) {
            return gson.fromJson(reader, graphType);
        }
    }
}
