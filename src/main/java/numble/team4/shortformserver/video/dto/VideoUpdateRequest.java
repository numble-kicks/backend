package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PROTECTED;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VideoUpdateRequest {

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    private String title;

    @NotNull(message = "가격은 비어있을 수 없습니다.")
    private Integer price;

    @NotNull
    private Boolean usedStatus;

    @NotNull
    private String category;

    private String description;

    public Video toVideo(Category category) {
        return Video.builder()
            .title(title)
            .description(description)
            .price(price)
            .usedStatus(usedStatus)
            .category(category)
            .build();
    }
}
