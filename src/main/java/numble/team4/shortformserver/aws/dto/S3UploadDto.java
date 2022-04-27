package numble.team4.shortformserver.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class S3UploadDto {

    private String key;
    private String originalFileName;
    private String fileUrl;
}
