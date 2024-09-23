package swyp.swyp6_team7.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Moing API")
                .description("여행 커뮤니티 서비스 '모잉' 백엔드 API")
                .version("1.0.0");

        return new OpenAPI()
                .info(info);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)  // 사용하려는 AWS 리전 설정
                .build();
    }

}
