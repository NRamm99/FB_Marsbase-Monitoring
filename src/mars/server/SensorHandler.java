package mars.server;

import mars.domain.SensorMeasurement;
import mars.domain.SensorType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SensorHandler implements Runnable {

    private final Socket sensor;
    private final SensorParser parser = new SensorParser();
    private static final MarsLogger logger = new MarsLogger();

    public SensorHandler(Socket sensor) {
        this.sensor = sensor;
    }

    // Modtager målinger fra én sensor og lukker forbindelsen bagefter.
    @Override
    public void run() {
        SensorType connectedSensor = null;

        try (Socket connection = sensor;
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(connection.getInputStream()))) {

            String measurement;

            while ((measurement = input.readLine()) != null) {
                try {
                    SensorMeasurement parsedMeasurement = parser.parse(measurement);

                    if (connectedSensor == null) {
                        connectedSensor = parsedMeasurement.getType();
                        System.out.println("Sensor forbundet: " + connectedSensor);
                    }

                    System.out.println(timestamp() + " Modtaget: " + measurement);
                    boolean alarm = validateMeasurement(parsedMeasurement);
                    logger.log(parsedMeasurement, alarm);
                } catch (IllegalArgumentException e) {
                    System.err.println("[ERROR] Ugyldig måling: " + measurement);
                }
            }

            System.out.println("Sensor har lukket forbindelsen: " + connectedSensor);

        } catch (IOException e) {
            System.err.println("[ERROR] Sensor mistede forbindelsen ("
                    + connectedSensor + "): " + e.getMessage());
        }
    }

    // Kontrollerer sensortypen og dens værdi på serveren.
    private boolean validateMeasurement(SensorMeasurement measurement) {
        SensorType type = measurement.getType();
        double value = measurement.getValue();

        boolean alarm = switch (type) {
            case TEMP -> value < -15 || value > 35;
            case O2 -> value < 19 || value > 23;
            case CO2 -> value > 2000;
        };

        if (alarm) {
            System.out.println(timestamp() + " [ALARM] " + type
                    + " er uden for grænsen: " + value);
        }

        return alarm;
    }

    // Returnerer klokkeslættet i serverens console-format.
    private String timestamp() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + LocalDateTime.now().format(format) + "]";
    }
}
