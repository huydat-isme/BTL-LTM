

import com.github.javafaker.Faker;

public class PressureGenerator {

    public double PressureGenerator () {
        Faker faker = new Faker();
        double offset = 10.0; // giá trị offset

        double pressure = faker.random().nextInt(1000) + offset; // Generate temperature from 0 to 100
        System.out.println("pressure generated successfully");

        return pressure;

    }
}
