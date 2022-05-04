package numble.team4.shortformserver.video.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VideoDtoTest {

    private static Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("VideoUpdate DTO 테스트")
    void videoUpdateRequestValidateTest() throws Exception {
        // given
        VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
            .title(null)
            .description(null)
            .category(null)
            .build();

        // when
        Set<ConstraintViolation<VideoUpdateRequest>> violations = validator.validate(
            videoUpdateRequest);

        // then
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("VideoRequest DTO 테스트")
    void videoRequestValidateTest() throws Exception {
        // given
        VideoRequest videoRequest = VideoRequest.builder()
            .video(null)
            .thumbnail(null)
            .title(null)
            .build();

        // when
        Set<ConstraintViolation<VideoRequest>> violations = validator.validate(videoRequest);

        // then
        assertThat(violations.size()).isEqualTo(4);
    }
}