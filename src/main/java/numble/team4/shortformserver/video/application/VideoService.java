package numble.team4.shortformserver.video.application;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
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
    private final MemberRepository memberRepository;
    private final AmazonS3Uploader amazonS3Uploader;

    @Transactional
    public VideoResponse uploadVideo(VideoRequest videoRequest, Long memberId)
        throws IOException {
        Member member = findMember(memberId);
        
        S3UploadDto videoDto = amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video");
        S3UploadDto thumbnailDto = amazonS3Uploader.saveToS3(videoRequest.getThumbnail(), "video/thumbnail");

        Video video = videoRequest.toVideo(videoDto.getFileUrl(), thumbnailDto.getFileUrl(), member);

        return VideoResponse.of(videoRepository.save(video));
    }

    @Transactional
    public VideoResponse updateVideo(VideoUpdateRequest videoUpdateRequest, Long memberId, Long videoId) {
        Video video = findVideo(videoId);
        Member member = findMember(memberId);

        validateAuthor(member, video);

        video.update(videoUpdateRequest.toVideo());
        return VideoResponse.of(video);
    }

    private void validateAuthor(Member member, Video video) {
        if (!video.isAuthorOf(member)) {
            throw new NotAuthorException();
        }
    }

    private Video findVideo(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(NotExistVideoException::new);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(NotExistMemberException::new);
    }

}
