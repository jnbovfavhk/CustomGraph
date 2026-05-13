import java.io.*;
import java.net.*;

class GraphServer {
    private CustomGraph graph; // ваш класс графа
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
        this.graph = new CustomGraph();
    }

    public GraphServer(CustomGraph graph) {
        this.graph = graph;
    }


    private String handleCommand(String command) {
        // Разделяем команду и данные
        String[] parts = command.split(":", 2);

        if (parts.length < 2) {
            return "ERROR: Invalid command format";
        }

        String cmdType = parts[0];
        String data = parts[1];  // дополнительные данные типа имён вершин, весов и тд

        switch (cmdType) {
            case "ADD_VERTEX":
                String nodeName = data;  // имя из TouchDesigner
                graph.addNode(nodeName);
                return "OK: Vertex " + nodeName + " added";

            default:
                return "ERROR: Unknown command";
        }
    }
}

