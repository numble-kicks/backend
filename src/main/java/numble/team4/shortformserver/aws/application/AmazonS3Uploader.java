package numble.team4.shortformserver.aws.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.aws.exception.AmazonClientException;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class AmazonS3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloudfront.url}")
    private String cloudfrontUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3UploadDto saveToS3(MultipartFile file, String dirName) throws IOException {
        validateFileExist(file);

        String key = dirName + "/" + UUID.randomUUID() + "_" + file.getName();
        return putS3(file, key);
    }

    private S3UploadDto putS3(MultipartFile file, String key) throws RuntimeException, IOException {
        InputStream inputStream = file.getInputStream();

        byte[] bytes = IOUtils.toByteArray(inputStream);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        objectMetadata.setContentType(file.getContentType());

        ByteArrayInputStream uploadFile = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, uploadFile, objectMetadata);
            amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (RuntimeException e) {
            throw new AmazonClientException();
        }

        return new S3UploadDto(key, cloudfrontUrl + key);
    }

    private void validateFileExist(MultipartFile file) throws RuntimeException {
        if (file.isEmpty()) {
            throw new NotExistFileException();
        }
    }
}
