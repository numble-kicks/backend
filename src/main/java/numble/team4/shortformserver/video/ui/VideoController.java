package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.*;
import numble.team4.shortformserver.common.dto.*;
import numble.team4.shortformserver.video.dto.*;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.application.VideoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/v1")
public class VideoController {

    private static final String BASE_URI = "/videos";

    private final VideoService videoService;

    @PostMapping(BASE_URI)
    public CommonResponse<Long> saveVideo(
        @Valid VideoRequest request,
        @LoginUser Member loggedInMember) {

        Long savedVideoId = videoService.uploadVideo(request, loggedInMember).getId();
        return CommonResponse.of(savedVideoId, UPLOAD_VIDEO.getMessage());
    }

    @PutMapping(BASE_URI + "/{video_id}")
    public CommonResponse<Long> updateVideo(
        @RequestBody @Valid VideoUpdateRequest request,
        @LoginUser Member loggedInMember,
        @PathVariable("video_id") Long videoId) {

        Long updatedVideoId = videoService.updateVideo(request, loggedInMember, videoId).getId();
        return CommonResponse.of(updatedVideoId, UPDATE_VIDEO.getMessage());
    }

    @DeleteMapping(BASE_URI + "/{video_id}")
    public CommonResponse<VideoResponse> deleteVideo(
        @PathVariable("video_id") Long videoId,
        @LoginUser Member loggedInMember) {

        videoService.deleteVideo(videoId, loggedInMember);
        return CommonResponse.from(DELETE_VIDEO.getMessage());
    }

    @GetMapping(BASE_URI + "/{video_id}")
    public CommonResponse<VideoResponse> findVideoById(@PathVariable("video_id") Long videoId) {

        return CommonResponse.of(videoService.findVideoById(videoId), GET_VIDEO_BY_ID.getMessage());
    }

    @GetMapping(BASE_URI)
    public CommonResponse<List<VideosResponse>> getAllVideo() {
        return CommonResponse.of(videoService.getAllVideo(), GET_ALL_VIDEO.getMessage());
    }

    @GetMapping("/admin" + BASE_URI)
    public CommonResponse<List<AdminPageVideosResponse>> getAllVideos(@LoginUser Member admin, Pageable pageable) {

        Page<AdminPageVideosResponse> videos = videoService.getAdminPageVideoList(pageable, admin);
        return CommonResponse.of(videos.getContent(), PageInfo.from(videos), GET_ADMIN_PAGE_VIDEO_LIST.getMessage());
    }

    @GetMapping(BASE_URI + "/search-condition")
    public CommonResponse<List<VideosResponse>> searchVideoByKeyword(
        @ModelAttribute VideoSearchRequest request
    ) {
        return CommonResponse.of(videoService.searchByKeyword(request.getId(), request.getKeyword(), request.getSortBy()),
            GET_VIDEO_LIST_BY_KEYWORD.getMessage());
    }

    @GetMapping(BASE_URI + "/status-condition")
    public CommonResponse<List<VideosResponse>> getTopVideos((@ModelAttribute VideoSearchRequest request) {
        return CommonResponse.of(videoService.getTopVideos((request.getSortBy(), 10),
            GET_VIDEO_TOP_10.getMessage());
    }
}
