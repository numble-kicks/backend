package numble.team4.shortformserver.video.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideosResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoSearchService {

    private final VideoRepository videoRepository;

    public List<VideosResponse> searchByKeyword(Long lastId, String keyword, String sortBy) {
        return videoRepository.searchVideoByKeyword(lastId, keyword, sortBy)
            .stream()
            .map(VideosResponse::from)
            .collect(Collectors.toList());
    }

    public List<VideosResponse> getVideoTop10(String sortBy) {
        return videoRepository.getVideoTop10(sortBy)
            .stream()
            .map(VideosResponse::from)
            .collect(Collectors.toList());
    }

}
