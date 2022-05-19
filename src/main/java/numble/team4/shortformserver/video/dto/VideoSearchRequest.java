package numble.team4.shortformserver.video.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoSearchRequest {

    @NotBlank(message = "두 글자 이상 입력해주세요.")
    @Length(min = 2, message = "두 글자 이상 입력해주세요.")
    private String keyword;

    private String sortBy;
}
