
import com.github.javafaker.Faker;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TemperatureGenerator {
    public double TemperatureGenerator () {
        Faker faker = new Faker();
        double offset = 5.0; // giá trị offset

                double temperature = faker.random().nextInt(100) + offset; // Generate temperature from 0 to 100
                System.out.println("temperature generated successfully");

        return temperature;

    }
}
