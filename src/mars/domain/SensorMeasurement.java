package mars.domain;

// Sensor type og dens målte værdi.
public class SensorMeasurement {

    private final SensorType type;
    private final double value;

    public SensorMeasurement(SensorType type, double value) {
        this.type = type;
        this.value = value;
    }

    public SensorType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}