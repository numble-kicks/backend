package numble.team4.shortformserver.video.category.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@BaseDataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 전체 조회")
    void getCategories() throws Exception {
        // when, then
        assertThat(categoryRepository.findAll())
            .extracting("name")
            .containsExactly("구두/로퍼", "워커/부츠", "샌들/슬리퍼", "스니커즈", "기타");
    }

    @Test
    @DisplayName("카테고리 명 조회")
    void getCategoryName() throws Exception {
        // given
        String name = "스니커즈";

        // when
        Category category = categoryRepository.findByName(name)
            .orElseThrow(NotFoundCategoryException::new);

        // then
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("카테고리 명 조회 실패, 없는 카테고리 조회")
    void getCategoryName_failed() throws Exception {
        // given
        String name = "스니커즈ㅏ";

        // when, then
        assertThrows(NotFoundCategoryException.class, () -> categoryRepository.findByName(name)
            .orElseThrow(NotFoundCategoryException::new));
    }
}