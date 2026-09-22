package mars.server;

import mars.domain.SensorMeasurement;
import mars.domain.SensorType;

import java.util.Locale;

public class SensorParser {

    // Omdanner en besked som "TEMP|22.5" til en måling.
    public SensorMeasurement parse(String message) {
        String[] parts = message.split("\\|", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("Ugyldigt målingsformat");
        }

        SensorType type = SensorType.valueOf(parts[0].trim().toUpperCase(Locale.ROOT));
        double value = Double.parseDouble(parts[1].trim());

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Sensorværdien er ugyldig");
        }

        return new SensorMeasurement(type, value);
    }
}
