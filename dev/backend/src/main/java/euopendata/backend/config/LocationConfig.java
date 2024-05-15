package euopendata.backend.config;

import euopendata.backend.models.Location;
import euopendata.backend.repositories.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LocationConfig {

    @Bean
    CommandLineRunner commandLineRunner1(LocationRepository repository) {
        return args -> {
            Location cluj = new Location("Cluj-Napoca","City","30","30","30");
            Location brasov = new Location("Brasov","City","21","25","100");

            repository.saveAll(
                    List.of(cluj,brasov)
            );
        };
    }
}
