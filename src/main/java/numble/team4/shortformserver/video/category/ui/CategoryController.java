package numble.team4.shortformserver.video.category.ui;


import static numble.team4.shortformserver.video.category.ui.CategoryResponseMessage.GET_CATEGORIES;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.video.category.application.CategoryService;
import numble.team4.shortformserver.video.category.dto.CategoryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public CommonResponse<List<CategoryDto>> showCategories() {
        List<CategoryDto> categories = categoryService.findCategories();
        return CommonResponse.of(categories, GET_CATEGORIES.getMessage());
    }
}
