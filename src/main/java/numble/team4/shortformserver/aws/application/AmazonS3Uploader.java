package numble.team4.shortformserver.aws.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
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

    public void deleteToS3(String url) {
        if (!url.contains(cloudfrontUrl)) {
            return;
        }
        String key = url.substring(cloudfrontUrl.length());

        amazonS3Client.deleteObject(bucket, key);
    }

    public S3UploadDto saveToS3(MultipartFile file, String dirName) {
        validateFileExist(file);

        String key = dirName + "/" + UUID.randomUUID() + "_" + file.getName();
        return convertFile(file, key);
    }

    private S3UploadDto convertFile(MultipartFile file, String key) {
        try {
            InputStream inputStream = file.getInputStream();

            byte[] bytes = IOUtils.toByteArray(inputStream);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(file.getContentType());

            ByteArrayInputStream uploadFile = new ByteArrayInputStream(bytes);

            putS3(key, objectMetadata, uploadFile);

            return new S3UploadDto(key, cloudfrontUrl + key);
        } catch (IOException e){
            throw new NotExistFileException();
        }
    }

    private void putS3(String key, ObjectMetadata objectMetadata,
        ByteArrayInputStream uploadFile) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, uploadFile,
                objectMetadata);
            amazonS3Client.putObject(
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (RuntimeException e) {
            throw new AmazonClientException();
        }
    }

    private void validateFileExist(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new NotExistFileException();
        }
    }
}
