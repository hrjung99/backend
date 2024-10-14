package swyp.swyp6_team7.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("!test")
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
