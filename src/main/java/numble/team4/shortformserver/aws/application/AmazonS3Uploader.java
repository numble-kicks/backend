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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class AmazonS3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveToS3(MultipartFile file, String dirName) throws IOException {
        validateFileExist(file);

        try {
            String key = dirName + "/" + UUID.randomUUID() + "_" + file.getName();
            return putS3(file, key);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private String putS3(MultipartFile file, String key) throws IOException {
        InputStream inputStream = file.getInputStream();

        byte[] bytes = IOUtils.toByteArray(inputStream);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        objectMetadata.setContentType(file.getContentType());

        ByteArrayInputStream uploadFile = new ByteArrayInputStream(bytes);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, uploadFile, objectMetadata);
        amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    private void validateFileExist(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있음");
        }
    }
}
