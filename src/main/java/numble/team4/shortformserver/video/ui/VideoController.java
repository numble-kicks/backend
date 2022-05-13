package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.*;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoController {

    private static final String HITS = "hits";
    private static final String LIKES = "likes";
    private static final int PAGE_SIZE = 18;
    private static final Pageable PAGE = PageRequest.of(0, PAGE_SIZE);

    private final VideoService videoService;

    @PostMapping
    public CommonResponse<Long> saveVideo(@Valid VideoRequest videoRequest,
        @LoginUser Member loggedInMember) {
        Long savedVideoId = videoService.uploadVideo(videoRequest, loggedInMember).getId();
        return CommonResponse.of(savedVideoId, UPLOAD_VIDEO.getMessage());
    }

    @PutMapping("/{video_id}")
    public CommonResponse<Long> updateVideo(
        @RequestBody @Valid VideoUpdateRequest videoUpdateRequest,
        @LoginUser Member loggedInMember,
        @PathVariable("video_id") Long videoId) {
        Long updatedVideoId = videoService.updateVideo(videoUpdateRequest, loggedInMember,
            videoId).getId();
        return CommonResponse.of(updatedVideoId, UPDATE_VIDEO.getMessage());
    }

    @DeleteMapping("/{video_id}")
    public CommonResponse<VideoResponse> deleteVideo(@PathVariable("video_id") Long videoId,
        @LoginUser Member loggedInMember) {
        videoService.deleteVideo(videoId, loggedInMember);

        return CommonResponse.from(DELETE_VIDEO.getMessage());
    }

    @GetMapping("/{videoId}")
    public CommonResponse<VideoResponse> findById(@PathVariable Long videoId) {
        VideoResponse video = videoService.findVideoById(videoId);

        return CommonResponse.of(video, GET_VIDEO_BY_ID.getMessage());
    }
}
