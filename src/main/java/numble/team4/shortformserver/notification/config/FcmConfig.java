package numble.team4.shortformserver.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FcmConfig {

    @Value("${firebase.config}")
    private String configPath;

    @Bean
    public FirebaseApp init() throws IOException {

        ClassPathResource resource = new ClassPathResource(configPath);

        try (InputStream inputStream = resource.getInputStream()) {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            log.warn("FirebaseApp initialization complete");
            return FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.warn(e.getClass().toString());
            log.warn("FirebaseApp initialization failed");
            throw new NullPointerException();
        }
    }

    @Bean
    public FirebaseMessaging initFirebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
