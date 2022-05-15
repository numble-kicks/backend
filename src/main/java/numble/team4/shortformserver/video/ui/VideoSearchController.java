package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.GET_VIDEO_LIST_BY_KEYWORD;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.GET_VIDEO_TOP_10;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.video.application.VideoSearchService;
import numble.team4.shortformserver.video.dto.VideoListRequest;
import numble.team4.shortformserver.video.dto.VideosResponse;
import numble.team4.shortformserver.video.dto.VideoSearchRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoSearchController {

    private final VideoSearchService videoSearchService;

    @GetMapping("/keyword-condition")
    public CommonResponse<List<VideosResponse>> searchByKeyword(
        @ModelAttribute VideoSearchRequest request) {

        return CommonResponse.of(videoSearchService.searchByKeyword(
                request.getLastId(),
                request.getKeyword(),
                request.getSortBy()
            ),
            GET_VIDEO_LIST_BY_KEYWORD.getMessage());
    }

    @GetMapping("/status-condition")
    public CommonResponse<List<VideosResponse>> getTopVideo(
        @ModelAttribute VideoListRequest request) {
        return CommonResponse.of(videoSearchService.getTopVideo(request.getSortBy(), 10),
            GET_VIDEO_TOP_10.getMessage());
    }
}
