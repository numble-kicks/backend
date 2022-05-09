package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.*;

import numble.team4.shortformserver.video.dto.*;
import org.springframework.web.bind.annotation.*;
import numble.team4.shortformserver.common.dto.*;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.application.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

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
        Member loggedInMember) {
        VideoResponse videoResponse = videoService.uploadVideo(videoRequest, loggedInMember);
        return CommonResponse.of(videoResponse, UPLOAD_VIDEO.getMessage());
    }

    @PutMapping("/{videoId}")
    public CommonResponse<VideoResponse> updateVideo(
        @RequestBody @Valid VideoUpdateRequest videoUpdateRequest,
        @RequestParam Long memberId,
        @PathVariable Long videoId) {
        Member loggedInMember = memberRepository.findById(memberId).orElseThrow(
            NotExistMemberException::new);
        VideoResponse videoResponse = videoService.updateVideo(videoUpdateRequest, loggedInMember,
            videoId);
        return CommonResponse.of(videoResponse, UPDATE_VIDEO.getMessage());
    }

    @DeleteMapping("/{videoId}")
    public CommonResponse<VideoResponse> deleteVideo(@PathVariable Long videoId,
        @RequestParam Long loggedInMemberId) {
        Member loggedInMember = memberRepository.findById(loggedInMemberId)
            .orElseThrow(NotExistMemberException::new);
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
        @RequestParam Long videoId,
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

    @GetMapping("/{memberId}")
    public CommonResponse<List<VideoResponse>> findAllVideosOfMember(
        @PathVariable Long memberId,
        @RequestParam Long videoId,
        @PageableDefault Pageable pageable
        ) {
        Page<VideoResponse> videos = videoService.findAllVideosOfMember(memberId, videoId,
            pageable);

        return CommonResponse.of(videos.getContent(), PageInfo.from(videos),
            GET_ALL_VIDEOS_OF_MEMBER.getMessage());
    }
}
