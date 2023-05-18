import com.github.javafaker.Faker;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TemperatureGenerator {
    public static void main(String[] args) {
        Faker faker = new Faker();
        double offset = 5.0; // giá trị offset
        int num = 100;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("temperature.txt"));
            for (int i = 0; i < num; i++) {
                double temperature = faker.random().nextInt(100) + offset;
                writer.write(temperature + "\n");
            }
            writer.close();
            System.out.println("Sensor data generated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing data to file.");
        }
    }
}
