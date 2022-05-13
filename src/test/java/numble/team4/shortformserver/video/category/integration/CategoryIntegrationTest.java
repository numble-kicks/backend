package numble.team4.shortformserver.video.category.integration;

import static numble.team4.shortformserver.video.category.ui.CategoryResponseMessage.GET_CATEGORIES;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@BaseIntegrationTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("카테고리 목록 조회")
    void getCategories() throws Exception {
        // when
        ResultActions res = mockMvc.perform(get("/v1/categories"));

        // then
        res.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(GET_CATEGORIES.getMessage()))
            .andExpect(jsonPath("$.data.size()").value(5))
            .andDo(print());
    }
}
