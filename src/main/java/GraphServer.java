import java.io.*;
import java.net.*;
import java.util.Scanner;

class GraphServer {
    private CustomGraph graph; // ваш класс графа
    private String graphType;
    private ServerSocket serverSocket;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(() -> {
            while(true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    String command = in.readLine();
                    if (command == null) {
                        return;
                    }
                    String response = handleCommand(command);
                    out.println(response);
                } catch (IOException e) { e.printStackTrace(); }
            }
        }).start();
    }

    public void stopServer() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public GraphServer() {

    }

    public void initGraph(String graphType) {
        this.graph = new CustomGraph();
        this.graphType = graphType;
    }

    public void initGraphFromFile(String fileName) {
        try {
            graph = new CustomGraph(fileName + ".json");

            Scanner scanner = new Scanner(new File(fileName + ".txt"));
            this.graphType = scanner.nextLine();
            scanner.close();

        } catch (IOException e) {
            System.out.printf("No such file: %s\n", fileName);
        }
    }


    private String handleCommand(String command) throws IOException {
        // Разделяем команду и данные
        String[] parts = command.split(":", 2);

        if (parts.length < 2) {
            return "ERROR: Invalid command format";
        }

        String cmdType = parts[0];
        String data = parts[1];  // дополнительные данные типа имён вершин, весов, файлов и тд

        switch (cmdType) {
            case "INIT_GRAPH":
                initGraph(data);
                return "OK: Graph with type '" + data + "' initialized";

            case "INIT_GRAPH_FROM_FILE":
                initGraph(data);
                return "OK: Graph from file '" + data + "' initialized";

            case "ADD_NODE":
                // имя из TouchDesigner
                graph.addNode(data);
                return "OK: Node " + data + " added";

            case "SAVE":
                graph.saveGraph(data + ".json");

                FileWriter writer = new FileWriter(data + ".txt");
                writer.write(graphType);
                writer.close();

                return "OK: Graph saved to file " + data;

            case "ADD_EDGE":

            default:
                return "ERROR: Unknown command";
        }
    }
}

