package mars.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SensorHandler implements Runnable {

    private final Socket sensor;

    public SensorHandler(Socket sensor) {
        this.sensor = sensor;
    }

    // Modtager målinger fra én sensor og lukker forbindelsen bagefter.
    @Override
    public void run() {
        try (Socket connection = sensor;
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(connection.getInputStream()))) {

            String measurement;

            while ((measurement = input.readLine()) != null) {
                System.out.println("Modtaget: " + measurement);
            }

            System.out.println("Sensor har lukket forbindelsen.");

        } catch (IOException e) {
            System.err.println("[ERROR] Sensor mistede forbindelsen: "
                    + e.getMessage());
        }
    }
}