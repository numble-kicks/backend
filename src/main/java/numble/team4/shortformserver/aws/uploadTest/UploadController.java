package numble.team4.shortformserver.aws.uploadTest;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UploadController {

    private final S3Uploader s3Uploader;

    @PostMapping("/api/v2/upload")
    public String upload(@RequestParam("files")MultipartFile file) throws IOException {
        System.out.println("UploadController.upload");
        return s3Uploader.upload(file, "test");
    }
}
