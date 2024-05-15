package euopendata.backend.config;

import euopendata.backend.models.Filter;
import euopendata.backend.repositories.FilterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FilterConfig {

    @Bean
    CommandLineRunner commandLineRunner3(FilterRepository repository) {
           return args -> {
               Filter filterCountries = new Filter("Country name:","select",List.of("USA", "Canada", "Mexico"));
               Filter filterCities = new Filter("City Name","select",List.of("New York", "Toronto", "Mexico City"));

               Filter filter1= new Filter("Filter 1", "input", null);
               Filter filter2= new Filter("Filter 2", "input", null);
               Filter filter3= new Filter("Filter 3", "input", null);
               Filter filter4= new Filter("Filter 4", "toggle", null);
               Filter filter5= new Filter("Filter 5", "toggle", null);
               Filter filter6= new Filter("Filter 6", "toggle", null);
               Filter filter7= new Filter("Filter 7", "toggle", null);
               Filter filter8= new Filter("Filter 8", "toggle", null);
               Filter filter9= new Filter("Filter 9", "toggle", null);
               Filter filter10= new Filter("Filter 10", "toggle", null);
               Filter filter11= new Filter("Filter 11", "toggle", null);
           repository.saveAll(List.of(filterCountries,filterCities,filter1,filter2,filter3,filter4,filter5,filter6,filter7,filter8,filter9,filter10,filter11));
           };
    }
}