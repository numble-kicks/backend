package numble.team4.shortformserver.video.category.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.dto.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findCategories() {
        return categoryRepository.findAll()
            .stream().sorted((a, b)-> (int) (a.getId() - b.getId()))
            .map(CategoryDto::from)
            .collect(Collectors.toList());
    }
}
