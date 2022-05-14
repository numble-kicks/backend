package numble.team4.shortformserver.video.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoSearchService {

    private final VideoRepository videoRepository;

    public List<VideoListResponse> searchByKeyword(Long lastId, String keyword, String sortBy) {
        return videoRepository.searchVideoByKeyword(lastId, keyword, sortBy)
            .stream()
            .map(VideoListResponse::from)
            .collect(Collectors.toList());
    }

    public List<VideoListResponse> getTopVideo(String sortBy, Integer limitNum) {
        return videoRepository.getTopVideo(sortBy, limitNum)
            .stream()
            .map(VideoListResponse::from)
            .collect(Collectors.toList());
    }

}
