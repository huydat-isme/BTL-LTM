

import com.github.javafaker.Faker;

public class AltitudeGenerator {

    public double AltitudeGenerator () {
        Faker faker = new Faker();
        double offset = 7.0; // giá trị offset

        double altitude = faker.random().nextInt(2000) + offset; // Generate temperature from 0 to 100
        System.out.println("altitude generated successfully");

        return altitude;

    }
}
