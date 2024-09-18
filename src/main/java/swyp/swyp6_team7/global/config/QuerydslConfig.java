package swyp.swyp6_team7.global.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class QuerydslConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory querydsl() {
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }
}
