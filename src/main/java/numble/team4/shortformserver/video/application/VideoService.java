package numble.team4.shortformserver.video.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final AmazonS3Uploader amazonS3Uploader;

    @Transactional
    public VideoResponse uploadVideo(VideoRequest videoRequest, Member loggedInMember) {
        S3UploadDto videoDto = amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video");
        S3UploadDto thumbnailDto = amazonS3Uploader.saveToS3(videoRequest.getThumbnail(), "video/thumbnail");

        Video video = videoRequest.toVideo(videoDto.getFileUrl(), thumbnailDto.getFileUrl(), loggedInMember);

        Video saveVideo = videoRepository.save(video);
        return VideoResponse.from(saveVideo);
    }

    @Transactional
    public VideoResponse updateVideo(VideoUpdateRequest videoUpdateRequest, Member loggedInMember, Long videoId) {
        Video video = findVideo(videoId);

        video.update(videoUpdateRequest.getTitle(), videoUpdateRequest.getDescription(), loggedInMember);
        return VideoResponse.from(video);
    }

    @Transactional
    public void deleteVideo(Long videoId, Member loggedMember) {
        Video video = findVideo(videoId);

        validateAuthor(loggedMember, video);

        amazonS3Uploader.deleteToS3(video.getVideoUrl());
        amazonS3Uploader.deleteToS3(video.getThumbnailUrl());

        videoRepository.delete(video);
    }

    private void validateAuthor(Member member, Video video) {
        if (!video.isAuthorOf(member)) {
            throw new NotAuthorException();
        }
    }

    private Video findVideo(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(NotExistVideoException::new);
    }
}
