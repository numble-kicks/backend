package numble.team4.shortformserver.aws.application;

import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import java.io.FileInputStream;
import numble.team4.shortformserver.aws.config.AmazonS3Config;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest(classes = {AmazonS3Uploader.class, AmazonS3Config.class})
class AmazonS3UploaderTest {

    @Autowired
    private AmazonS3Uploader amazonS3Uploader;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Test
    @DisplayName("s3_업로드_테스트")
    public void saveToS3() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "test",
            "test.mov",
            "video/quicktime",
            new FileInputStream("src/test/test.mov")
        );

        S3UploadDto s3UploadDto = amazonS3Uploader.saveToS3(mockMultipartFile, "test");

        // when
        S3Object s3Object = amazonS3Client.getObject(bucket, s3UploadDto.getKey());

        // then
        assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(mockMultipartFile.getContentType());
        assertThat(s3Object.getBucketName()).isEqualTo(bucket);

        // 검증 후 오브젝트 삭제
        amazonS3Client.deleteObject(bucket, s3UploadDto.getKey());
    }

    @Test
    @DisplayName("파일이 비어있을 경우 s3 업로드되지 않음")
    public void s3UploadFail () {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", new byte[]{});

        // when
        NotExistFileException exception = assertThrows(NotExistFileException.class,
            () -> amazonS3Uploader.saveToS3(mockMultipartFile, "test"));

        // then
        assertThat(exception.getMessage()).isEqualTo(NOT_EXIST_FILE.getMessage());
    }
}