package euopendata.backend.config;


import euopendata.backend.models.Users;
import euopendata.backend.repositories.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UsersConfig {

    @Bean
    CommandLineRunner commandLineRunner2(UsersRepository repository) {
        return args -> {
            Users marcu = new Users("Marcu","Petraru","marcupetraru03@gmail.com","marcu123b","marcu");
            Users ioja = new Users("Stefan","Ioja","stefanioja03@gmail.com","stefan123b","stefan");

            repository.saveAll(
                    List.of(marcu,ioja)
            );
        };
    }
}
