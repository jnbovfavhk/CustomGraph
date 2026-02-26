import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        while (ans != 6) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Exit");
            ans = takeValidNumber(1, 6);
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
        System.out.println("Enter 2 nodes to remove an edge between them(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();
        System.out.printf("%s, %s", node1, node2);

        int result = graph.addEdge(node1, node2);
        if (result == -1) {
            System.out.println("No such node");
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
        while (ans != 6) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Exit");
            ans = takeValidNumber(1, 6);
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
        System.out.println("Enter 2 nodes to remove an edge between them(via new line)");
        String node1 = sc.nextLine();
        String node2 = sc.nextLine();

        int result = graph.addOrientedEdge(node1, node2);
        if (result == -1) {
            System.out.println("No such node");
        }
    }


    private static void weightedGraph() throws IOException {

        type = "weighted";

        int ans = 0;
        while (ans != 6) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Exit");
            ans = takeValidNumber(1, 6);
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

    private static void orientedWeightedGraph() throws IOException {

        type = "orientedWeighted";

        int ans = 0;
        while (ans != 6) {
            System.out.println("Choose the option:");
            System.out.println("1 Add node");
            System.out.println("2 Add edge");
            System.out.println("3 Remove node");
            System.out.println("4 Remove edge");
            System.out.println("5 Save to file");
            System.out.println("6 Exit");
            ans = takeValidNumber(1, 6);
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
            }
        }
    }

    private static void orientedWeightedAddEdge() {
        System.out.println("Enter 2 nodes to remove an edge between them and the weight(via new line)");
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


}
