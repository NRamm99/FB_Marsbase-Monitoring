package mars.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarsServer {

    public static void main(String[] args) {
        new MarsServer().start();
    }

    // Accepterer sensorer og giver hver forbindelse til trådpoolen.
    public void start() {
        try (ExecutorService threadPool = Executors.newFixedThreadPool(3);
             ServerSocket server = new ServerSocket(5001)) {

            System.out.println("Mars HQ venter på sensorer...");

            while (!Thread.currentThread().isInterrupted()) {
                Socket sensor = server.accept();
                System.out.println("Sensor forbundet.");

                threadPool.execute(new SensorHandler(sensor));
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Serverfejl: " + e.getMessage());
        }
    }
}