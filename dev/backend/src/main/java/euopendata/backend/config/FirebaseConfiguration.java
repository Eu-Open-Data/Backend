package euopendata.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {

    @Bean
    public FirebaseApp initializeApp() throws IOException {
        ClassPathResource serviceAccount = new ClassPathResource("serviceAccount.json");
        InputStream inputStream = serviceAccount.getInputStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl("https://holidayadvisoralbums-default-rtdb.europe-west1.firebasedatabase.app/")
                .setStorageBucket("holidayadvisoralbums.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
