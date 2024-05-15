package euopendata.backend.config;

import com.github.javafaker.Faker;
import euopendata.backend.models.Location;
import euopendata.backend.repositories.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MockLocationConfig {

    @Bean
    CommandLineRunner commandLineRunner4(LocationRepository repository) {
        return args -> {
            Faker faker = new Faker();
            List<Location> locations = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                String name = faker.address().cityName();
                String type = "City";
                String air_quality = String.valueOf(faker.number().numberBetween(15, 100));
                String soil_quality = String.valueOf(faker.number().numberBetween(15, 100));
                String water_quality = String.valueOf(faker.number().numberBetween(15, 100));
                locations.add(new Location(name, type, air_quality, soil_quality, water_quality));
            }
            repository.saveAll(locations);
        };
    }
}
