import org.apache.commons.lang3.tuple.MutablePair;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            CustomGraph graph = new CustomGraph(); // граф
            GraphServer server = new GraphServer();
            server.startServer(8888); // порт любой, например 8888
            System.out.println("Server started on port 8888");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ConsoleInterface.execute();
    }
}
