package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPLOAD_VIDEO;

import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoRequest;
import org.springframework.web.bind.annotation.PostMapping;
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
    public CommonResponse saveVideo(@RequestParam @Valid VideoRequest videoRequest,
        Member member) throws IOException {
        videoService.upload(videoRequest, member);
        return CommonResponse.from(UPLOAD_VIDEO.getMessage());
    }
}
