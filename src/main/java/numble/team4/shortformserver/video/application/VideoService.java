package numble.team4.shortformserver.video.application;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Uploader amazonS3Uploader;

    public Video upload(VideoRequest request, Member member, MultipartFile videos, MultipartFile thumbnail)
        throws IOException {
        Member findMember = memberRepository.findById(member.getId())
            .orElseThrow(NotExistMemberException::new);

        S3UploadDto videoDto = amazonS3Uploader.saveToS3(videos, "video");
        S3UploadDto thumbnailDto = amazonS3Uploader.saveToS3(thumbnail, "video/thumbnail");

        Video video = request.toVideo(videoDto.getFileUrl(), thumbnailDto.getFileUrl(), findMember);

        return videoRepository.save(video);
    }
}
