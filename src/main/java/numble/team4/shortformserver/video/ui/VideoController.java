package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPDATE_VIDEO;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPLOAD_VIDEO;

import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    private final VideoService videoService;

    @PostMapping
    public CommonResponse<VideoResponse> saveVideo(@ModelAttribute VideoRequest videoRequest,
        Member loggedInMember) throws IOException {
        VideoResponse videoResponse = videoService.uploadVideo(videoRequest, loggedInMember.getId());
        return CommonResponse.of(videoResponse, UPLOAD_VIDEO.getMessage());
    }

    @PutMapping("/{videoId}")
    public CommonResponse<VideoResponse> updateVideo(
        @RequestBody @Valid VideoUpdateRequest videoUpdateRequest,
        @RequestParam Long memberId,
        Member loggedInMember,
        @PathVariable Long videoId) {
        VideoResponse videoResponse = videoService.updateVideo(videoUpdateRequest, memberId,
            videoId);
        return CommonResponse.of(videoResponse, UPDATE_VIDEO.getMessage());
    }
}
