package numble.team4.shortformserver.notification.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
@Setter
public class FcmConfigEnvironment {

    @Value("${firebase.type}")
    private String type;

    @Value("${firebase.project_id}")
    private String projectId;

    @Value("${firebase.private_key_id}")
    private String privateKeyId;

    @Value("${firebase.private_key}")
    private String privateKey;

    @Value("${firebase.client_email}")
    private String clientEmail;

    @Value("${firebase.client_id}")
    private String clientId;

    @Value("${firebase.auth_uri}")
    private String authId;

    @Value("${firebase.token_uri}")
    private String tokenUri;

    @Value("${firebase.auth_provider_x509_cert_url}")
    private String authProviderX509CertUrl;

    @Value("${firebase.client_x509_cert_url}")
    private String clientX509CertUrl;

    public String getConfigStringValue() {
        try {
            HashMap<String, String> config = new HashMap<>() {{
                put("type", type);
                put("project_id", projectId);
                put("private_key_id", privateKeyId);
                put("private_key", privateKey.replace("\\n", System.lineSeparator()));
                put("client_email", clientEmail);
                put("client_id", clientId);
                put("auth_id", authId);
                put("token_uri", tokenUri);
                put("auth_provider_x509_cert_url", authProviderX509CertUrl);
                put("client_x509_cert_url", clientX509CertUrl);
            }};
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(config);
        } catch (Exception e) {
            return null;
        }
    }

}
