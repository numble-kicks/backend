package numble.team4.shortformserver.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.exception.BaseErrorResponse;
import numble.team4.shortformserver.common.exception.BaseException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII;

@RequiredArgsConstructor
public class JwtExceptionHandleFilter extends OncePerRequestFilter {

    private static final String APPLICATION_JSON = "application/json; charset:UTF-8";
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            assert exception instanceof BaseException;
            setErrorResponse(response, (BaseException) exception);
        }
    }

    public void setErrorResponse(HttpServletResponse response, BaseException exception) throws JsonProcessingException {
        response.setStatus(exception.getStatus().value());
        response.setContentType(APPLICATION_JSON);
        objectMapper.getFactory().configure(ESCAPE_NON_ASCII.mappedFeature(), true);
        String responseData = objectMapper.writeValueAsString(BaseErrorResponse.from(exception));

        try{
            response.getWriter().write(responseData);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
