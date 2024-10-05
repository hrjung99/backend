package swyp.swyp6_team7.global.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Bean
    public AmazonS3 amazonS3() {
        // AWS 자격 증명 설정
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("ACCESS_KEY", "SECRET_KEY");

        return AmazonS3ClientBuilder.standard()
                .withRegion("ap-northeast-2") // 사용 중인 리전을 입력
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}