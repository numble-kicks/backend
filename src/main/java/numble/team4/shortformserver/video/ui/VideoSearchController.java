package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.GET_VIDEO_LIST_BY_KEYWORD;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.GET_VIDEO_TOP_10;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.video.application.VideoSearchService;
import numble.team4.shortformserver.video.dto.VideosResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/search")
public class VideoSearchController {

    private final VideoSearchService videoSearchService;

    @GetMapping("/videos")
    public CommonResponse<List<VideosResponse>> searchVideoByKeyword(
        @RequestParam String keyword,
        @RequestParam(required = false) Long lastId,
        @RequestParam(required = false) String sortBy
    ) {
        return CommonResponse.of(videoSearchService.searchByKeyword(lastId, keyword, sortBy),
            GET_VIDEO_LIST_BY_KEYWORD.getMessage());
    }

    @GetMapping
    public CommonResponse<List<VideosResponse>> getVideoTop10(@RequestParam String sortBy) {
        return CommonResponse.of(videoSearchService.getVideoTop10(sortBy),
            GET_VIDEO_TOP_10.getMessage());
    }
}
