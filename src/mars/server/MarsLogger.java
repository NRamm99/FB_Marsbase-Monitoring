package mars.server;

import mars.domain.SensorMeasurement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarsLogger {

    private final DateTimeFormatter format =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Skriver målingen til filen uden at overskrive gamle loglinjer.
    public synchronized void log(SensorMeasurement measurement, boolean alarm) {
        String logLine = "[" + LocalDateTime.now().format(format) + "] "
                + measurement.getType() + ": " + measurement.getValue();

        if (alarm) {
            logLine += " -> ALARM!";
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("mars.log", true))) {
            writer.write(logLine);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[ERROR] Kunne ikke skrive til mars.log: "
                    + e.getMessage());
        }
    }
}
