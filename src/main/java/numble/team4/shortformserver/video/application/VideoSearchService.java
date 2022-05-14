package numble.team4.shortformserver.video.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoSearchService {

    private final VideoRepository videoRepository;

    public List<VideoListResponse> searchByKeyword(Long lastId, String keyword, String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            sortBy = "";
        }

        return VideoListResponse.from(
            videoRepository.searchVideoByKeyword(lastId, keyword, sortBy));
    }

    public List<VideoListResponse> getTopVideo(String sortBy, Integer limitNum) {

        return VideoListResponse.from(videoRepository.getTopVideo(sortBy, limitNum));
    }

}
