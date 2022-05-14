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
    void videoUpdateRequestValidateTest() {
        // given
        VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
            .title(null)
            .description(null)
            .category(null)
            .price(null)
            .usedStatus(null)
            .build();

        // when
        Set<ConstraintViolation<VideoUpdateRequest>> violations = validator.validate(
            videoUpdateRequest);

        // then
        assertThat(violations).hasSize(4);
    }

    @Test
    @DisplayName("VideoRequest DTO 테스트")
    void videoRequestValidateTest() {
        // given
        VideoRequest videoRequest = new VideoRequest(null, null, null, null, null, null, null);

        // when
        Set<ConstraintViolation<VideoRequest>> violations = validator.validate(videoRequest);

        // then
        assertThat(violations).hasSize(6);
    }
}