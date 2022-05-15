package numble.team4.shortformserver.video.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class VideoSearchRequest {

    @NotBlank(message = "두 글자 이상 입력해주세요.")
    @Length(min = 2, max = 20, message = "최대 20자까지 입력할 수 있어요.")
    private String keyword;

    private Long lastId;
    private String sortBy;
}
