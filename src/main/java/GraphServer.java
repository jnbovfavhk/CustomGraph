import java.io.*;
import java.net.*;
import java.util.Scanner;

class GraphServer {
    private CustomGraph graph; // ваш класс графа
    private String graphType;
    private ServerSocket serverSocket;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("=== СЕРВЕР ЗАПУЩЕН НА ПОРТУ " + port + " ===");
        System.out.println("Ожидание подключений...");

        new Thread(() -> {
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("!!! КЛИЕНТ ПОДКЛЮЧИЛСЯ !!!");
                    System.out.println("Адрес клиента: " + socket.getRemoteSocketAddress());

                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                        // Читаем ВСЕ строки, которые приходят
                        String command;
                        while ((command = in.readLine()) != null) {
                            System.out.println(">>> [ДИАГНОСТИКА] Получено: '" + command + "'");

                            // Если это пустая строка - пропускаем
                            if (command.trim().isEmpty()) {
                                System.out.println(">>> [ДИАГНОСТИКА] Пустая строка, пропускаем");
                                continue;
                            }

                            // Обрабатываем команду
                            String response = handleCommand(command);
                            System.out.println("<<< [ДИАГНОСТИКА] Ответ: '" + response + "'");
                            out.println(response);

                            // Если команда была ADD_NODE или другая основная - продолжаем слушать
                            // Не закрываем соединение после одной команды
                        }
                    } catch (IOException e) {
                        System.out.println("Ошибка при обработке клиента: " + e.getMessage());
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка при принятии подключения: " + e.getMessage());
                    e.printStackTrace();
                }
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
        System.out.println("=== ПОЛУЧЕНО: [" + command + "] ===");
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

            case "SAVE":
                graph.saveGraph(data + ".json");

                FileWriter writer = new FileWriter(data + ".txt");
                writer.write(graphType);
                writer.close();

                return "OK: Graph saved to file " + data;

            case "ADD_NODE":
                // имя из TouchDesigner
                graph.addNode(data);
                System.out.println("Node " + data + " added");
                return "OK: Node " + data + " added";

            case "ADD_EDGE":
                String nodeFrom = data.split(",")[0];
                String nodeTo = data.split(",")[1];
                switch (this.graphType) {
                    case "default":
                        this.graph.addEdge(nodeFrom, nodeTo);

                        return "OK: Added edge from '" + nodeFrom + "' to '" + nodeTo + "'";
                    case "oriented":
                        this.graph.addOrientedEdge(nodeFrom, nodeTo);

                        return "OK: Added edge from '" + nodeFrom + "' to '" + nodeTo + "'";
                    case "weighted":
                        this.graph.addEdge(nodeFrom, nodeTo, Double.parseDouble(data.split(",")[2]));

                        return "OK: Added edge from '" + nodeFrom + "' to '" + nodeTo + "' with weight " + data.split(",")[2];

                    case "orientedWeighted":
                        this.graph.addOrientedEdge(nodeFrom, nodeTo, Double.parseDouble(data.split(",")[2]));

                        return "OK: Added edge from '" + nodeFrom + "' to '" + nodeTo + "' with weight " + data.split(",")[2];
                    default:
                        return "ERROR: Unknown command: " + command;
                }

            case "DELETE_NODE":
                this.graph.removeNode(data);
                return "OK: Node '" + data + "' deleted";

            case "DELETE_EDGE":
                switch (this.graphType) {
                    case "default", "weighted":
                        this.graph.removeEdge(data.split(",")[0], data.split(",")[1]);

                        return "OK: Edge between '" + data.split(",")[0] + "' and '" + data.split(",")[0] + "' removed";
                    case "oriented", "orientedWeighted":
                        this.graph.removeOrientedEdge(data.split(",")[0], data.split(",")[1]);

                        return "OK: Edge between '" + data.split(",")[0] + "' and '" + data.split(",")[0] + "' removed";

                        default:
                        return "ERROR: Unknown command: " + command;
                }



            default:
                return "ERROR: Unknown command: " + command;
        }
    }
}

