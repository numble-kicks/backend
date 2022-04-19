package numble.team4.shortformserver.common.healthcheck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("권한이 필요 없는 url로 요청 테스트")
    @Test
    void healthCheckNormalCase() throws Exception {
        mockMvc.perform(get("/healthcheck/normal").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("예외처리가 정상 작동하는지 확인 테스트")
    @Test
    void healthCheckExceptionTest() throws Exception {
        mockMvc.perform(get("/healthcheck/exception").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("권한이 필요한 url로 요청 테스트")
    @Test
    void permitAllTest() throws Exception {
        mockMvc.perform(get("/healthcheck/require-auth").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}