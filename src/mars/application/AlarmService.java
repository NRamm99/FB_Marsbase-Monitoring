package mars.application;

import mars.domain.SensorMeasurement;

public class AlarmService {

    // Kontrollerer, om målingen overskrider sensorens grænseværdier.
    public boolean isOutOfRange(SensorMeasurement measurement) {
        double value = measurement.getValue();

        return switch (measurement.getType()) {
            case TEMP -> value < -15 || value > 35;
            case O2 -> value < 19 || value > 23;
            case CO2 -> value > 2000;
        };
    }
}