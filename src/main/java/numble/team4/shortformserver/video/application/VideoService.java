package numble.team4.shortformserver.video.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.exception.NoAccessPermissionException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideosResponse;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private static final int PAGE_SIZE = 18;

    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final AmazonS3Uploader amazonS3Uploader;


    @Transactional
    public VideoResponse uploadVideo(VideoRequest videoRequest, Member loggedInMember) {
        Category category = categoryRepository.findByName(videoRequest.getCategory())
            .orElseThrow(NotFoundCategoryException::new);

        S3UploadDto videoDto = amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video");
        S3UploadDto thumbnailDto = amazonS3Uploader.saveToS3(videoRequest.getThumbnail(),
            "video/thumbnail");

        Video video = videoRequest.toVideo(
            videoDto.getFileUrl(),
            thumbnailDto.getFileUrl(),
            loggedInMember,
            category
        );

        return VideoResponse.from(videoRepository.save(video));
    }

    @Transactional
    public VideoResponse updateVideo(VideoUpdateRequest videoUpdateRequest, Member loggedInMember, Long videoId) {

        Video findVideo = videoRepository.findById(videoId)
            .orElseThrow(NotExistVideoException::new);

        Category category = categoryRepository.findByName(videoUpdateRequest.getCategory())
            .orElseThrow(NotFoundCategoryException::new);

        findVideo.validateAuthor(loggedInMember);
        findVideo.update(
            videoUpdateRequest.getTitle(),
            videoUpdateRequest.getDescription(),
            videoUpdateRequest.getPrice(),
            videoUpdateRequest.getUsed_status(),
            category
        );
        return VideoResponse.from(findVideo);
    }

    @Transactional
    public void deleteVideo(Long videoId, Member loggedInMember) {
        Video findVideo = videoRepository.findById(videoId)
            .orElseThrow(NotExistVideoException::new);

        findVideo.validateAuthor(loggedInMember);

        amazonS3Uploader.deleteToS3(findVideo.getVideoUrl());
        amazonS3Uploader.deleteToS3(findVideo.getThumbnailUrl());

        videoRepository.delete(findVideo);
    }

    public List<VideosResponse> findAllVideosByMember(Long memberId, Long videoId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMemberException::new);

        List<Video> videos = videoRepository.findAllByMemberAndMaxVideoId(member, videoId,
            PAGE_SIZE);
        return VideosResponse.from(videos);
    }

    public List<VideosResponse> findAllLikeVideosByMember(Long memberId, Long videoId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMemberException::new);

        List<Video> videos = videoRepository.findAllLikeVideoByMemberAndMaxVideoId(member, videoId,
            PAGE_SIZE);
        return VideosResponse.from(videos);
    }

    @Transactional
    public VideoResponse findVideoById(Long videoId) {
        Video findVideo = videoRepository.findById(videoId)
            .orElseThrow(NotExistVideoException::new);
        findVideo.increaseViewCount();
        return VideoResponse.from(findVideo);
    }

    public List<VideosResponse> getAllVideos() {
        return VideosResponse.from(videoRepository.findAll());
    }

    public List<VideosResponse> searchByKeyword(Long lastId, String keyword, String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            sortBy = "";
        }
        return VideosResponse.from(videoRepository.searchVideoByKeyword(lastId, keyword, sortBy));
    }

    public List<VideosResponse> getTopVideos(String sortBy, Integer limitNum) {
        return VideosResponse.from(videoRepository.getTopVideos(sortBy, limitNum));
    }

    public Page<VideoResponse> getAdminPageVideos(Pageable page, Member admin) {
        if (!admin.getRole().equals(Role.ADMIN)) {
            throw new NoAccessPermissionException();
        }

        long count = videoRepository.count();
        return videoRepository.getAllVideos(page, count).map(VideoResponse::from);
    }
}
