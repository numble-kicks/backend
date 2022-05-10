package numble.team4.shortformserver.video.category.dto;

import static lombok.AccessLevel.PRIVATE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.video.category.domain.Category;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class CategoryDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    public static CategoryDto create(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
