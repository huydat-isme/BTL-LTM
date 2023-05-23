

import com.github.javafaker.Faker;

public class HumidityGenerator {

    public double HumidityGenerator () {
        Faker faker = new Faker();
        double offset = 6.0; // giá trị offset

        double humididty = faker.random().nextInt(200) + offset; // Generate temperature from 0 to 100
        System.out.println("humidity generated successfully");

        return humididty;

    }
}
