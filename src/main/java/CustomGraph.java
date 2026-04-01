import org.apache.commons.lang3.tuple.MutablePair;

import java.io.*;
import java.util.*;

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
        HashMap<GraphObj, HashMap<GraphObj, Double>> copy = new HashMap<>();

        for (Map.Entry<GraphObj, HashMap<GraphObj, Double>> entry : adjacencyList.entrySet()) {
            GraphObj keyCopy = new GraphObj(entry.getKey().getName());
            HashMap<GraphObj, Double> neighborsCopy = new HashMap<>();

            for (Map.Entry<GraphObj, Double> neighborEntry : entry.getValue().entrySet()) {
                GraphObj neighborCopy = new GraphObj(neighborEntry.getKey());
                neighborsCopy.put(neighborCopy, neighborEntry.getValue());
            }

            copy.put(keyCopy, neighborsCopy);
        }

        return copy;
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

    // Для данной вершины орграфа вывести все «выходящие» соседние вершины
    public Set<GraphObj> returnOneSideNeighboursList(String node) {
        GraphObj graphObj = new GraphObj(node);
        if (adjacencyList.containsKey(graphObj)) {
//            System.out.println(adjacencyList.get(graphObj));
            return adjacencyList.get(graphObj).keySet();
        }
        throw new NoSuchElementException("No such node");
    }

    // Вывести те вершины орграфа, которые являются одновременно заходящими и выходящими для заданной вершины
    public Set<GraphObj> returnFullNeighboursList(String node) {
        GraphObj graphObj = new GraphObj(node);
        if (adjacencyList.containsKey(graphObj)) {
            Set<GraphObj> oneSide = adjacencyList.get(graphObj).keySet();
            Set<GraphObj> otherSide = new java.util.HashSet<>(Set.of());

            for (GraphObj key : adjacencyList.keySet()) {
                if (adjacencyList.get(key).containsKey(graphObj)) {
                    otherSide.add(key);
                }
            }

//            System.out.println("Выходящие: ");
//            System.out.println(oneSide);
//            System.out.println("Заходящие: ");
//            System.out.println(otherSide);
            Set<GraphObj> intersection = new HashSet<>(oneSide);; // копируем первое множество
            intersection.retainAll(otherSide); // оставляем только элементы, которые есть в otherSide

            return intersection;
        }

        throw new NoSuchElementException("No such node");
    }

    // Построить граф, полученный однократным удалением рёбер, соединяющих вершины одинаковой степени
    public CustomGraph removeEdgesBetweenSameDegree() {
        // Собираем рёбра для удаления
        HashMap<GraphObj, GraphObj> edgesToRemove = new HashMap<>();
        // Конструктор-копия
        CustomGraph graphCopy = new CustomGraph(this);

        for (Map.Entry<GraphObj, HashMap<GraphObj, Double>> entry : adjacencyList.entrySet()) {
            GraphObj from = entry.getKey();

            for (Map.Entry<GraphObj, Double> neighborEntry : entry.getValue().entrySet()) {
                GraphObj to = neighborEntry.getKey();

                int degreeFrom = adjacencyList.get(from).size();
                int degreeTo = adjacencyList.get(to).size();
//                System.out.println(from + " " + degreeFrom + " " + to + " " + degreeTo);

                // Если степени одинаковые - помечаем ребро для удаления
                if (degreeFrom == degreeTo) {

                    edgesToRemove.put(from, to);
                }
            }
        }

        for (Map.Entry<GraphObj, GraphObj> edge : edgesToRemove.entrySet()) {
//            System.out.println("Удаляем " + edge.getKey().toString() + " " + edge.getValue().toString());
            graphCopy.removeEdge(edge.getKey().toString(), edge.getValue().toString());
        }

        return graphCopy;
    }

    public CustomGraph orientedToNonOriented() {
        CustomGraph graphCopy = new CustomGraph(this);

        for (GraphObj nodeFrom : graphCopy.getAdjacencyList().keySet()) {
            for (GraphObj nodeTo : graphCopy.getAdjacencyList().get(nodeFrom).keySet()) {
                if (!graphCopy.getAdjacencyList().get(nodeTo).containsKey(nodeFrom)) {
                    graphCopy.getAdjacencyList().get(nodeTo).put(nodeFrom, graphCopy.getAdjacencyList().
                            get(nodeFrom).get(nodeTo));
                }
            }
        }

        return graphCopy;
    }

    // Проверка на циклы (DFS)
    private boolean hasCycle(GraphObj node, Set<GraphObj> visited, Set<GraphObj> stack) {
        if (stack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        stack.add(node);

        for (GraphObj neighbor : adjacencyList.getOrDefault(node, new HashMap<>()).keySet()) {
            if (hasCycle(neighbor, visited, stack)) return true;
        }

        stack.remove(node);
        return false;
    }

    // Топологическая сортировка
    public List<GraphObj> topologicalSort() {
        // Проверка на циклы из вспх вершин
        Set<GraphObj> visited = new HashSet<>();
        for (GraphObj node : adjacencyList.keySet()) {
            if (hasCycle(node, visited, new HashSet<>())) {
                throw new IllegalStateException("Graph has cycle, topologicalSort can not be executed");
            }
        }

        // Сортировка (алгоритм Кана)
        Map<GraphObj, Integer> inDegree = new HashMap<>();
        for (GraphObj node : adjacencyList.keySet()) {
            inDegree.putIfAbsent(node, 0);
            for (GraphObj neighbor : adjacencyList.get(node).keySet()) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        Queue<GraphObj> queue = new LinkedList<>();
        for (Map.Entry<GraphObj, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) queue.add(entry.getKey());
        }

        List<GraphObj> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            GraphObj current = queue.poll();
            result.add(current);

            for (GraphObj neighbor : adjacencyList.getOrDefault(current, new HashMap<>()).keySet()) {
                int degree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, degree);
                if (degree == 0) queue.add(neighbor);
            }
        }

        return result;
    }

    public int shortestPathLength(String u, String v) {
        GraphObj start = new GraphObj(u);
        GraphObj end = new GraphObj(v);

        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return -1;
        }

        // BFS
        Queue<GraphObj> queue = new LinkedList<>();
        Map<GraphObj, Integer> distance = new HashMap<>();

        queue.add(start);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            GraphObj current = queue.poll();
            int currentDist = distance.get(current);

            if (current.equals(end)) {
                return currentDist;
            }

            for (GraphObj neighbor : adjacencyList.get(current).keySet()) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, currentDist + 1);
                    queue.add(neighbor);
                }
            }
        }

        return -1;
    }

    private static class Edge {
        GraphObj from, to;
        double weight;
        Edge(GraphObj from, GraphObj to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public CustomGraph primMST() {
        if (adjacencyList.isEmpty()) return new CustomGraph();
        // минимальное остовное дерево
        CustomGraph mst = new CustomGraph();
        // посещенные вершины
        Set<GraphObj> visited = new HashSet<>();
        // Очередь с приоритетом по весу(отсортированная) для ребер
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Double.compare(a.weight, b.weight));

        // Первую вершину добавляем в посещенные и в каркас
        GraphObj start = adjacencyList.keySet().iterator().next();
        visited.add(start);
        mst.addNode(start.getName());

        // Для первой вершины добавляем все ребра
        for (Map.Entry<GraphObj, Double> e : adjacencyList.get(start).entrySet()) {
            pq.add(new Edge(start, e.getKey(), e.getValue()));
        }

        while (!pq.isEmpty() && visited.size() < adjacencyList.size()) {
            // Берем ребро с минимальным весом
            Edge edge = pq.poll();

            if (visited.contains(edge.to)) continue;

            // Добавляем вершину в посещенные, а ребро и вершины в каркас
            visited.add(edge.to);
            mst.addNode(edge.to.getName());
            mst.addEdge(edge.from.getName(), edge.to.getName(), edge.weight);

            // добавляем все ребра из новой вершины
            for (Map.Entry<GraphObj, Double> e : adjacencyList.get(edge.to).entrySet()) {
                if (!visited.contains(e.getKey())) {
                    pq.add(new Edge(edge.to, e.getKey(), e.getValue()));
                }
            }
        }

        return mst;
    }
}

