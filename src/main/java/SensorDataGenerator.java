import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SensorDataGenerator {
    public static void main(String[] args) {
        int numSamples = 20;
        double minValue = 0.0;
        double maxValue = 20.0;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("temperature_data.txt"));
            for (int i = 0; i < numSamples; i++) {
                double sensorValue = minValue + Math.random() * (maxValue - minValue);
                writer.write(sensorValue + "\n");
            }
            writer.close();
            System.out.println("Sensor data generated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing data to file.");
        }
    }
}
