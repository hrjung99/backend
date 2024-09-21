package swyp.swyp6_team7.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import swyp.swyp6_team7.auth.jwt.JwtFilter;
import swyp.swyp6_team7.auth.jwt.JwtProvider;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and() // CORS 허용
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .authorizeHttpRequests(auth -> auth
                        // 로그인, 회원가입, 토큰 재발급, kakao 로그인 요청 경로 경로는 허용
<<<<<<< Updated upstream
                        .requestMatchers("/api/login", "/api/users/new","/api/refresh-token","/login/oauth/kakao/**","/error","/api/users-email").permitAll()
=======
                        .requestMatchers("/api/profile/**","/api/login","/api/logout", "/api/users/new","/api/refresh-token","/login/oauth/kakao/**","/error","/api/users-email").permitAll()
>>>>>>> Stashed changes
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }
    // 전역 CORS 설정
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  // CORS 허용할 API 경로
                        .allowedOrigins("http://localhost:9999")  // 허용할 프론트엔드 도메인
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                        .allowedHeaders("*")  // 모든 헤더 허용
                        .allowCredentials(true);  // 인증 정보 허용
            }
        };
    }


}