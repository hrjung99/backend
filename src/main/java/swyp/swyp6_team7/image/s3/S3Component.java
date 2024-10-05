package swyp.swyp6_team7.image.s3;

import lombok.Getter;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class  S3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-folder}")
    private String baseFolder;
}
