package mars.client;

import mars.domain.SensorType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Random;

public class SensorClient {

    private final Random random = new Random();
    private final SensorType type;

    public SensorClient(SensorType type) {
        this.type = type;
    }

    // Vælger sensor type, eksempelvis O2.
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Angiv sensortype: TEMP, O2 eller CO2.");
            return;
        }

        try {
            SensorType type = SensorType.valueOf(
                    args[0].toUpperCase(Locale.ROOT));

            new SensorClient(type).start();

        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Ukendt sensortype. Brug TEMP, O2 eller CO2.");
        }
    }

    // Forbinder til serveren og sender en måling hvert femte sekund.
    public void start() {
        try (Socket socket = new Socket("localhost", 5001);
             PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true)) {

            System.out.println(type + " forbundet til Mars HQ.");

            while (!Thread.currentThread().isInterrupted()) {
                String measurement = createMeasurement();
                output.println(measurement);

                // PrintWriter skjuler skrivefejl, så vi tjekker dem her.
                if (output.checkError()) {
                    throw new IOException("Forbindelsen til Mars HQ er tabt.");
                }

                System.out.println("Sendt: " + measurement);
                Thread.sleep(5000);
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Sensorfejl: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sensor stoppet.");
        }
    }

    // Genererer værdier både inden for og uden for kommende alarmgrænser der skal implementeres.
    private String createMeasurement() {
        double value = switch (type) {
            case TEMP -> -25 + random.nextDouble() * 70;
            case O2 -> 17 + random.nextDouble() * 8;
            case CO2 -> 400 + random.nextDouble() * 2600;
        };

        return String.format(Locale.ROOT, "%s| %.1f", type, value);
    }
}
