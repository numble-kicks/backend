package numble.team4.shortformserver.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.notification.domain.FcmConfigEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FcmConfig {

    private final FcmConfigEnvironment config;
    private static final String firebaseAppName = "kicks";

    @Bean
    public FirebaseApp firebaseApp() {
        String configStringValue = config.getConfigStringValue();
        try (InputStream inputStream = new ByteArrayInputStream(configStringValue.getBytes(StandardCharsets.UTF_8))) {
            List<FirebaseApp> apps = FirebaseApp.getApps();

            if (Objects.isNull(apps) || apps.isEmpty()) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(inputStream))
                        .build();
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, firebaseAppName);
                log.warn("FirebaseApp initialization complete");
                return firebaseApp;
            }

            return apps.get(0);
        } catch (Exception e) {
            log.warn("FirebaseApp initialization failed");
            throw new NullPointerException();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
