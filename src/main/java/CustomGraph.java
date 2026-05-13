import org.apache.commons.lang3.tuple.MutablePair;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.print.attribute.standard.MediaSize;
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

        // минимальный остовной лес
        CustomGraph mstForest = new CustomGraph();
        // посещенные вершины
        Set<GraphObj> visited = new HashSet<>();

        // Пока есть непосещенные вершины (для каждой компоненты связности)
        while (visited.size() < adjacencyList.size()) {
            // Находим стартовую вершину для текущей компоненты
            GraphObj start = null;
            for (GraphObj vertex : adjacencyList.keySet()) {
                if (!visited.contains(vertex)) {
                    start = vertex;
                    break;
                }
            }

            if (start == null) break;

            // Применяем алгоритм Прима для текущей компоненты
            primForComponent(start, visited, mstForest);
        }

        return mstForest;
    }

    private void primForComponent(GraphObj start, Set<GraphObj> visited, CustomGraph mstForest) {
        // Очередь с приоритетом по весу для ребер
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Double.compare(a.weight, b.weight));

        // Добавляем стартовую вершину в посещенные и в лес
        visited.add(start);
        mstForest.addNode(start.getName());

        // Для стартовой вершины добавляем все ребра
        for (Map.Entry<GraphObj, Double> e : adjacencyList.get(start).entrySet()) {
            if (!visited.contains(e.getKey())) {
                pq.add(new Edge(start, e.getKey(), e.getValue()));
            }
        }

        // Алгоритм Прима для текущей компоненты
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();

            if (visited.contains(edge.to)) continue;

            // Добавляем вершину в посещенные, а ребро и вершины в лес
            visited.add(edge.to);
            mstForest.addNode(edge.to.getName());
            mstForest.addEdge(edge.from.getName(), edge.to.getName(), edge.weight);

            // Добавляем все ребра из новой вершины
            for (Map.Entry<GraphObj, Double> e : adjacencyList.get(edge.to).entrySet()) {
                if (!visited.contains(e.getKey())) {
                    pq.add(new Edge(edge.to, e.getKey(), e.getValue()));
                }
            }
        }
    }

    // Вспомогательный класс
    private static class Node implements Comparable<Node> {
        GraphObj v;
        double d;
        Node(GraphObj v, double d) { this.v = v; this.d = d; }
        public int compareTo(Node o) { return Double.compare(this.d, o.d); }
    }

    // Алгоритм Дейкстры
    private HashMap<GraphObj, Double> dijkstra(GraphObj start_node) {
        HashMap<GraphObj, Double> dist = new HashMap<>();
        for (GraphObj v : adjacencyList.keySet()) {
            dist.put(v, Double.POSITIVE_INFINITY);
        }

        dist.put(start_node, 0.0);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(start_node, 0.0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.d > dist.get(cur.v)) continue;
            for (var e : adjacencyList.get(cur.v).entrySet()) {
                double new_distance = cur.d + e.getValue();
                if (new_distance < dist.get(e.getKey())) {
                    dist.put(e.getKey(), new_distance);
                    pq.add(new Node(e.getKey(), new_distance));
                }
            }
        }
        return dist;
    }

    // Определить, есть ли в графе вершина, минимальные стоимости путей от которой до остальных в сумме не превосходят P
    public GraphObj nodeThatSumOfMinimumPathsLessThan(double p) {
        for (GraphObj v : getAdjacencyList().keySet()) {
            HashMap<GraphObj, Double> dist = dijkstra(v);

            double sum = 0;
            boolean ok = true;
            for (var e : dist.entrySet()) {
                if (e.getValue() == Double.POSITIVE_INFINITY) { ok = false; break; }
                if (!e.getKey().equals(v)) sum += e.getValue();
            }
            if (ok && sum <= p) return v;
        }
        return null;
    }

    private int edgesCount() {
        int count = 0;
        for (GraphObj node : adjacencyList.keySet()) {
            count += adjacencyList.get(node).size();
        }

        return count;
    }

    private HashMap<GraphObj, HashMap<GraphObj, Double>> invertAdjacencyList() {
        HashMap<GraphObj, HashMap<GraphObj, Double>> inverted = new HashMap<>();

        // Инициализируем все вершины в новом списке
        for (GraphObj node : adjacencyList.keySet()) {
            inverted.put(node, new HashMap<>());
        }

        // Для каждого ребра from -> to с весом weight
        // добавляем ребро to -> from с тем же весом
        for (Map.Entry<GraphObj, HashMap<GraphObj, Double>> entry : adjacencyList.entrySet()) {
            GraphObj from = entry.getKey();

            for (Map.Entry<GraphObj, Double> edge : entry.getValue().entrySet()) {
                GraphObj to = edge.getKey();
                Double weight = edge.getValue();

                // Инвертируем направление: to -> from
                inverted.get(to).put(from, weight);
            }
        }

        return inverted;
    }

    private HashMap<GraphObj, Double> bellmanFord(GraphObj givenNode) {
        // Инвертирую список смежности чтобы искать минимальные расстояния к вершине а не от неё
        var inverseAdjacencyList = invertAdjacencyList();

        HashMap<GraphObj, Double> dist = new HashMap<>();
        for (GraphObj v : inverseAdjacencyList.keySet()) {
            if (!v.equals(givenNode)) {
                dist.put(v, Double.POSITIVE_INFINITY);
            }
            else {
                dist.put(v, (double) 0);
            }
        }

        HashMap<GraphObj, GraphObj> parents = new HashMap<>();
        for (GraphObj v : inverseAdjacencyList.keySet()) {
            parents.put(v, null);
        }

        int edgesCount = edgesCount();
        for (int i = 0; i < edgesCount - 1; i++) {
            boolean updated = false;

            for (var entry : inverseAdjacencyList.entrySet()) {
                GraphObj from = entry.getKey();
                HashMap<GraphObj, Double> edges = entry.getValue();

                for (var edge : edges.entrySet()) {
                    GraphObj to = edge.getKey();
                    Double weight = edge.getValue();

                    // Релаксация
                    if (dist.get(from) != Double.POSITIVE_INFINITY &&
                            dist.get(from) + weight < dist.get(to)) {

                        dist.put(to, dist.get(from) + weight);
                        parents.put(to, from);
                        updated = true;
                    }
                }
            }
            // Если за итерацию не было обновлений, то выходим
            if (!updated) {
                break;
            }


        }
        return dist;

    }

    public Set<GraphObj> allNodesWithDistanceToNodeLessThanN(String givenNodeName, double n) {
        GraphObj givenNode = new GraphObj(givenNodeName);
        if (!adjacencyList.containsKey(givenNode)) {
            throw new NoSuchElementException("No such node: " + givenNodeName);
        }

        HashMap<GraphObj, Double> distances = bellmanFord(givenNode);
        Set<GraphObj> answerSet = new HashSet<>();

        for (var node : distances.entrySet()) {
            if (node.getValue() < n) {
                answerSet.add(node.getKey());
            }
        }

        return answerSet;
    }

    // Алгоритм Флойда для нахождения кратчайших путей между всеми парами вершин
    public HashMap<GraphObj, HashMap<GraphObj, Double>> floyd() {

        List<GraphObj> nodes = new ArrayList<>(adjacencyList.keySet());
        int n = nodes.size();

        // матрица расстояний
        HashMap<GraphObj, HashMap<GraphObj, Double>> dist = new HashMap<>();

        // Заполняем матрицу начальными значениями
        for (GraphObj node : nodes) {
            HashMap<GraphObj, Double> distances = new HashMap<>();
            distances.put(node, 0.0);
            dist.put(node, distances);
        }

        // Заполняем известные расстояния из списка смежности
        for (Map.Entry<GraphObj, HashMap<GraphObj, Double>> entry : adjacencyList.entrySet()) {
            GraphObj from = entry.getKey();

            for (Map.Entry<GraphObj, Double> edge : entry.getValue().entrySet()) {
                GraphObj to = edge.getKey();
                double weight = edge.getValue();

                // Если есть несколько ребер между вершинами, берем минимальный вес
                HashMap<GraphObj, Double> fromDistances = dist.get(from);
                if (!fromDistances.containsKey(to) || weight < fromDistances.get(to)) {
                    fromDistances.put(to, weight);
                }
            }
        }

        // Основной цикл алгоритма
        for (int k = 0; k < n; k++) {
            GraphObj intermediateNode = nodes.get(k);

            for (int i = 0; i < n; i++) {
                GraphObj fromNode = nodes.get(i);

                // Получаем расстояние от fromNode до intermediateNode
                Double distToIntermediate = dist.get(fromNode).get(intermediateNode);
                if (distToIntermediate == null || distToIntermediate == Double.POSITIVE_INFINITY) {
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    GraphObj toNode = nodes.get(j);

                    // Получаем расстояние от intermediateNode до toNode
                    Double distFromIntermediate = dist.get(intermediateNode).get(toNode);
                    if (distFromIntermediate == null || distFromIntermediate == Double.POSITIVE_INFINITY) {
                        continue;
                    }

                    double newDistance = distToIntermediate + distFromIntermediate;
                    Double currentDistance = dist.get(fromNode).get(toNode);

                    if (currentDistance == null || newDistance < currentDistance) {
                        dist.get(fromNode).put(toNode, newDistance);
                    }
                }
            }
        }


        // Проверка на отрицательные циклы
        for (GraphObj node : nodes) {
            Double distToSelf = dist.get(node).get(node);
            if (distToSelf != null && distToSelf < 0) {
                var resultDist = editEndFloydMatrix(dist);
                System.out.println(resultDist);
                return resultDist;
            }
        }
        System.out.println(dist);
        return dist;
    }

    private HashMap<GraphObj, HashMap<GraphObj, Double>> editEndFloydMatrix(HashMap<GraphObj, HashMap<GraphObj, Double>> dist) {
        int n = dist.size();
        for (GraphObj k : dist.keySet()) {
            Double kDistToSelf = dist.get(k).get(k);
            // если в промежуточной вершине на главной диагонали матрицы отрицательное число
            if (kDistToSelf != null && kDistToSelf < 0) {
                for (GraphObj from : dist.keySet()) {
                    // если есть путь от from до k
                    Double fromToKDist = dist.get(from).get(k);
                    if (fromToKDist != null) {
                        for (GraphObj to : dist.keySet()) {
                            // если есть путь от k до to
                            Double kToToDist = dist.get(k).get(to);
                            if (kToToDist != null) {
                                // то мин путь из from в to заменяем на минус бесконечность
                                dist.get(from).put(to, Double.NEGATIVE_INFINITY);
                            }
                        }
                    }
                }
            }
        }

        return dist;
    }


    // Определить, есть ли в графе вершина, каждая из минимальных стоимостей пути от которой до остальных не превосходит N
    public Set<GraphObj> nodesThatEveryMinPathLessThanN(int N) {
        if (N <= 0) {
            throw new NoSuchElementException("N must be a positive value");
        }

        Set<GraphObj> answerNodes = new HashSet<>();

        HashMap<GraphObj, HashMap<GraphObj, Double>> distances = floyd();
        boolean add = true;
        for (var entry : distances.entrySet()) {
            add = true;
            for (var distance : entry.getValue().entrySet()) {
                if (distance.getValue() > N) {
                    add = false;
                    break;
                }
            }

            if (add) {
                answerNodes.add(entry.getKey());
            }
        }

        return answerNodes;
    }
}

