package numble.team4.shortformserver.member.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.exception.BaseErrorResponse;
import numble.team4.shortformserver.member.member.exception.NoAccessPermissionException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String APPLICATION_JSON = "application/json; charset:UTF-8";
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setErrorResponse(response);
    }

    public void setErrorResponse(HttpServletResponse response) throws JsonProcessingException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON);
        objectMapper.getFactory().configure(ESCAPE_NON_ASCII.mappedFeature(), true);
        String responseData = objectMapper.writeValueAsString(BaseErrorResponse.from(new NoAccessPermissionException()));

        try{
            response.getWriter().write(responseData);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
