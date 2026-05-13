import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.MutablePair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ConsoleInterface {
    private static CustomGraph graph;
    private static String type;
    private static Scanner sc;

    private static int takeValidNumber(int start, int end) {
        int ans = 0;
        boolean validInput = false;

        while (!validInput) {
            String ansString = sc.nextLine();

            try {
                ans = Integer.parseInt(ansString);

                if (ans >= start && ans <= end) {
                    validInput = true;
                } else {
                    System.out.printf("Please enter a number between %d and %d:", start, end);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Please enter a number (%d-%d):", start, end);
            }
        }

        return ans;
    }

    public static void execute() throws IOException {

        sc = new Scanner(System.in);


        System.out.println("What type of graph do you want?(Answer only by the numbers)");
        System.out.println("1 Default graph(no orientation, no weights)");
        System.out.println("2 Oriented graph without weights");
        System.out.println("3 Weighted graph");
        System.out.println("4 Oriented weighted graph");
        System.out.println("5 Take a graph from a file");

        int ans = takeValidNumber(1, 5);

        graph = new CustomGraph();
        switch (ans) {
            case 1:
                defaultGraph();
                break;
            case 2:
                orientedGraph();
                break;
            case 3:
                weightedGraph();
                break;
            case 4:
                orientedWeightedGraph();
                break;
            case 5:
                takeGraphFromFile();
        }
    }

    private static void takeGraphFromFile() {
        System.out.println("Enter the name of a file:");

        String fileName = sc.nextLine();
        try {
            graph = new CustomGraph(fileName + ".json");

            Scanner scanner = new Scanner(new File(fileName + ".txt"));
            type = scanner.nextLine();
            scanner.close();

            switch (type) {
                case "default":
                    defaultGraph();
                    break;
                case "oriented":
                    orientedGraph();
                    break;
                case "weighted":
                    weightedGraph();
                    break;
                case "orientedWeighted":
                    orientedWeightedGraph();
                    break;
            }
        } catch (IOException e) {
            System.out.printf("No such file: %s\n", fileName);
        }
    }

    private static void defaultGraph() throws IOException {

        type = "default";

        int ans = 0;
        while (ans != 10) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Print adjacency list");
            System.out.println("7 Print graph type");
            System.out.println("8 Take new graph from this removing edges between nodes with same degree");
            System.out.println("9 Find shortest path length");
            System.out.println("10 Exit");
            ans = takeValidNumber(1, 10);
            switch (ans) {
                case 1:
                    addNode();
                    break;
                case 2:
                    defaultAddEdge();
                    break;
                case 3:
                    removeNode();
                    break;
                case 4:
                    defaultRemoveEdge();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    printAdjList();
                    break;
                case 7:
                    printType();
                    break;
                case 8:
                    removeEdgesBetweenSameDegree();
                    break;
                case 9:
                    shortestPath();
                    break;
            }
        }

    }

    private static void save() throws IOException {
        System.out.println("Enter the name of a file");
        String ans = sc.nextLine();
        while (ans.isEmpty()) {
            ans = sc.nextLine();
        }
        graph.saveGraph(ans + ".json");

        FileWriter writer = new FileWriter(ans + ".txt");
        writer.write(type);
        writer.close();
    }

    private static void removeNode() {
        System.out.println("Enter the name of a node to remove");
        String ans = sc.nextLine();
        int result = graph.removeNode(ans);
        if (result == -1) {
            System.out.printf("No such node with name '%s'", ans);
        }
    }

    private static void defaultRemoveEdge() {
        System.out.println("Enter 2 nodes to remove an edge between them(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();

        int result = graph.removeEdge(node1, node2);
        if (result == -1) {
            System.out.println("No such node");
        }
    }

    private static void defaultAddEdge() {
        System.out.println("Enter 2 nodes to add an edge between them(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();
        System.out.printf("%s, %s", node1, node2);

        int result = graph.addEdge(node1, node2);
        if (result == -1) {
            System.out.println("No such node");
        }
    }

    private static void removeEdgesBetweenSameDegree() throws IOException {
        System.out.println("What do you want to do with resulting graph?");
        System.out.println("1 Replace my graph with it");
        System.out.println("2 Save it to file");
        int ans = 0;
        ans = takeValidNumber(1, 2);
        switch (ans) {
            case 1:
                graph = graph.removeEdgesBetweenSameDegree();
                System.out.println("Succeed");
                break;
            case 2:
                System.out.println("Enter the name of a file");
                String fileName = sc.nextLine();
                CustomGraph newGraph = graph.removeEdgesBetweenSameDegree();
                newGraph.saveGraph(fileName + ".json");

                FileWriter writer = new FileWriter(fileName + ".txt");
                writer.write(type);
                writer.close();

                System.out.println("Succeed");
                break;
        }
    }

    private static void addNode() {
        System.out.println("Enter the name of a node to add");
        String ans = sc.nextLine();
        graph.addNode(ans);
        System.out.println("Node added");
    }


    private static void orientedGraph() throws IOException {

        type = "oriented";
        int ans = 0;
        while (ans != 12) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Print adjacency list");
            System.out.println("7 Print graph type");
            System.out.println("8 Print outgoing nodes");
            System.out.println("9 Print outgoing and incoming nodes");
            System.out.println("10 Topological sort");
            System.out.println("11 Find shortest path length");
            System.out.println("12 Exit");
            ans = takeValidNumber(1, 12);
            switch (ans) {
                case 1:
                    addNode();
                    break;
                case 2:
                    orientedAddEdge();
                    break;
                case 3:
                    removeNode();
                    break;
                case 4:
                    orientedRemoveEdge();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    printAdjList();
                    break;
                case 7:
                    printType();
                    break;
                case 8:
                    printOneSideNeighboursList();
                    break;
                case 9:
                    printAllNeighboursList();
                    break;
                case 10:
                    topologicalSort();
                    break;
                case 11:
                    shortestPath();
                    break;
            }
        }
    }

    private static void orientedRemoveEdge() {
        System.out.println("Enter 2 nodes to remove an edge between them(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();

        int result = graph.removeOrientedEdge(node1, node2);
        if (result == -1) {
            System.out.println("No such node");
        }
    }

    private static void orientedAddEdge() {
        System.out.println("Enter 2 nodes to add an edge between them(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();

        int result = graph.addOrientedEdge(node1, node2);
        if (result == -1) {
            System.out.println("No such node");
        }
    }

    private static void printOneSideNeighboursList() {

        System.out.println("Enter the name of a node");
        String node = sc.nextLine();

        try {
            Set<GraphObj> ans = graph.returnOneSideNeighboursList(node);
            if (ans.isEmpty()) {
                System.out.println("No outgoing nodes");
            } else {
                System.out.println(ans);
            }
        } catch (NoSuchElementException e) {
            System.out.println("No such node");
        }

    }

    private static void printAllNeighboursList() {
        System.out.println("Enter the name of a node");
        String node = sc.nextLine();

        try {
            Set<GraphObj> ans = graph.returnFullNeighboursList(node);
            System.out.println("Outgoing and incoming: ");
            System.out.println(ans);
        } catch (NoSuchElementException e) {
            System.out.println("No such node");
        }
    }

    private static void topologicalSort() {
        try {
            List<GraphObj> sorted = graph.topologicalSort();
            System.out.println("Topological order:");
            for (int i = 0; i < sorted.size(); i++) {
                System.out.println((i+1) + ". " + sorted.get(i).getName());
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void shortestPath() {
        System.out.println("Enter start node:");
        String u = sc.nextLine();
        System.out.println("Enter end node:");
        String v = sc.nextLine();

        int length = graph.shortestPathLength(u, v);
        if (length == -1) {
            System.out.println("Path not found or vertices don't exist");
        } else {
            System.out.println("Shortest path length (number of edges): " + length);
        }
    }


    private static void weightedGraph() throws IOException {

        type = "weighted";

        int ans = 0;
        while (ans != 10) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Print adjacency list");
            System.out.println("7 Print graph type");
            System.out.println("8 Find minimum spanning tree (Prim)");
            System.out.println("9 Find node that the sum of minimum paths to other nodes is less than...");
            System.out.println("10 Exit");
            ans = takeValidNumber(1, 10);
            switch (ans) {
                case 1:
                    addNode();
                    break;
                case 2:
                    weightedAddEdge();
                    break;
                case 3:
                    removeNode();
                    break;
                case 4:
                    defaultRemoveEdge();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    printAdjList();
                    break;
                case 7:
                    printType();
                    break;
                case 8:
                    findMST();
                    break;
                case 9:
                    nodeThatSumOfMinimumPathsLessThan();
                    break;
            }
        }
    }

    private static void weightedAddEdge() {
        System.out.println("Enter 2 nodes to add an edge between them and the weight(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();
        int weight = 0;

        if (sc.hasNextInt()) {
            weight = Integer.parseInt(sc.nextLine());
        }
        else {
            System.out.println("Not a number");
            return;
        }

        int result = graph.addEdge(node1, node2, weight);
        if (result == -1) {
            System.out.println("No such node");
        }
    }

    private static void findMST() throws IOException {
        System.out.println("What do you want to do with the minimum spanning tree?");
        System.out.println("1 Print it");
        System.out.println("2 Save it to file");
        int ans = takeValidNumber(1, 2);

        CustomGraph mst = graph.primMST();

        switch (ans) {
            case 1:
                System.out.println("Minimum Spanning Tree:");
                printAdjList(mst);
                break;
            case 2:
                System.out.println("Enter file name:");
                String fileName = sc.nextLine();
                mst.saveGraph(fileName + ".json");


                FileWriter writer = new FileWriter(fileName + ".txt");
                writer.write("weighted"); // MST - неориентированный взвешенный граф
                writer.close();

                System.out.println("MST saved to " + fileName + ".json");
                break;
        }
    }

    private static void nodeThatSumOfMinimumPathsLessThan() {
        System.out.println("Enter value:");
        Double value = Double.parseDouble(sc.nextLine());

        GraphObj node = graph.nodeThatSumOfMinimumPathsLessThan(value);
        if (node == null) {
            System.out.println("No such node");
        } else {
            System.out.println(node);
        }
    }

    private static void orientedWeightedGraph() throws IOException {

        type = "orientedWeighted";

        int ans = 0;
        while (ans != 11) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Print adjacency list");
            System.out.println("7 Print graph type");
            System.out.println("8 Print All Nodes With Distance To Given Node Less Than N");
            System.out.println("9 Print Nodes That Every Min Path Less Than N");
            System.out.println("10 Print max flow between 2 nodes");
            System.out.println("11 Exit");
            ans = takeValidNumber(1, 11);
            switch (ans) {
                case 1:
                    addNode();
                    break;
                case 2:
                    orientedWeightedAddEdge();
                    break;
                case 3:
                    removeNode();
                    break;
                case 4:
                    orientedRemoveEdge();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    printAdjList();
                    break;
                case 7:
                    printType();
                    break;
                case 8:
                    allNodesWithDistanceToNodeLessThanN();
                    break;
                case 9:
                    nodesThatEveryMinPathLessThanN();
                    break;
                case 10:
                    maxFlow();
                    break;
            }
        }
    }

    private static void orientedWeightedAddEdge() {
        System.out.println("Enter 2 nodes to add an edge between them and the weight(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();
        int weight = 0;

        if (sc.hasNextInt()) {
            weight = Integer.parseInt(sc.nextLine());
        }
        else {
            System.out.println("Not a number");
            return;
        }

        int result = graph.addOrientedEdge(node1, node2, weight);
        if (result == -1) {
            System.out.println("No such node");
        }
    }

    public static void allNodesWithDistanceToNodeLessThanN() {
        System.out.println("Enter a node");
        String node = sc.nextLine();
        System.out.println("Enter N");
        int n;
        if (sc.hasNextInt()) {
            n = Integer.parseInt(sc.nextLine());
        }
        else {
            System.out.println("Not a number");
            return;
        }

        try {
            var resultSet = graph.allNodesWithDistanceToNodeLessThanN(node, n);
            for (GraphObj x : resultSet) {
                System.out.printf("%s, ", x);
            }
            System.out.println();
        } catch (NoSuchElementException e) {
            System.out.println("No such node");
            return;
        }
    }

    public static void nodesThatEveryMinPathLessThanN() {
        System.out.println("Print N");
        int n;
        if (sc.hasNextInt()) {
            n = Integer.parseInt(sc.nextLine());
        }
        else {
            System.out.println("Not a number");
            return;
        }

        var result = graph.nodesThatEveryMinPathLessThanN(n);

        for (GraphObj x : result) {
            System.out.printf("%s, ", x);
        }
        System.out.println();

    }

    public static void printAdjList() {
        printAdjList(graph);
    }

    public static void printAdjList(CustomGraph graph) {
        if (graph == null || graph.getNodeCount() == 0) {
            System.out.println("Graph is empty");
            return;
        }

        for (var entry : graph.getAdjacencyList().entrySet()) {
            System.out.print(entry.getKey().getName() + " -> ");

            if (entry.getValue().isEmpty()) {
                System.out.println("no neighbors");
            } else {
                var neighbors = new ArrayList<String>();
                for (var neighbor : entry.getValue().entrySet()) {
                    String s = neighbor.getKey().getName();
                    if (type.equals("weighted") || type.equals("orientedWeighted"))
                        s += "(" + neighbor.getValue() + ")";
                    neighbors.add(s);
                }
                System.out.println(String.join(", ", neighbors));
            }
        }
    }

    public static void maxFlow() {
        System.out.println("Enter a 'from' node");
        String s = sc.nextLine();

        System.out.println("Enter an 'exit' node");
        String t = sc.nextLine();

        System.out.println(graph.maxFlow(s, t));
    }

    public static void printType() {
        System.out.println("Type: " + (type != null ? type : "not set"));
    }
}
