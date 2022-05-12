package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.*;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.common.dto.PageInfo;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    private final VideoService videoService;
    private final MemberRepository memberRepository;

    @PostMapping
    public CommonResponse<VideoResponse> saveVideo(@Valid VideoRequest videoRequest,
        @LoginUser Member loggedInMember) {
        VideoResponse videoResponse = videoService.uploadVideo(videoRequest, loggedInMember);
        return CommonResponse.of(videoResponse, UPLOAD_VIDEO.getMessage());
    }

    @PutMapping("/{videoId}")
    public CommonResponse<VideoResponse> updateVideo(
        @RequestBody @Valid VideoUpdateRequest videoUpdateRequest,
        @LoginUser Member loggedInMember,
        @PathVariable Long videoId) {
        VideoResponse videoResponse = videoService.updateVideo(videoUpdateRequest, loggedInMember,
            videoId);
        return CommonResponse.of(videoResponse, UPDATE_VIDEO.getMessage());
    }

    @DeleteMapping("/{videoId}")
    public CommonResponse<VideoResponse> deleteVideo(@PathVariable Long videoId,
        @LoginUser Member loggedInMember) {
        videoService.deleteVideo(videoId, loggedInMember);

        return CommonResponse.from(DELETE_VIDEO.getMessage());
    }

    @GetMapping("/{videoId}")
    public CommonResponse<VideoResponse> findByIdVideo(@PathVariable Long videoId) {
        VideoResponse video = videoService.findVideoById(videoId);

        return CommonResponse.of(video, GET_VIDEO_BY_ID.getMessage());
    }

    @GetMapping
    public CommonResponse<List<VideoResponse>> findAllVideos(
        @RequestParam String sortBy,
        @Nullable @RequestParam Long videoId,
        @PageableDefault Pageable pageable
    ) {
        Page<VideoResponse> videos = new PageImpl<>(new ArrayList<>(), pageable,
            pageable.getPageSize());

        if (sortBy.equals(LIKES)) {
            videos = videoService.findAllVideosSortByLikes(videoId, pageable);
        }

        if (sortBy.equals(HITS)) {
            videos = videoService.findAllVideosSortByHits(videoId, pageable);
        }

        return CommonResponse.of(videos.getContent(), PageInfo.from(videos), GET_ALL_VIDEOS.getMessage());
    }
}
