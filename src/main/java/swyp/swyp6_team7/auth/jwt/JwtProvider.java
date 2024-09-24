package swyp.swyp6_team7.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Date;

@Component
public class JwtProvider {
    private final String secretKey = "oRzn5UpJmgRXxtOLMBG+jNhz2aAjPXHESKdjz4hFea5GNLnB9bVeVXKxKcZtxU+DlGZ4nHGO7xrXYhTkEhe2Zg==";
    private final long accessTokenValidity = 15 * 60 * 1000; // 15분
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000; // 1주일

    // Access Token 생성
    public String createAccessToken(String userEmail, Integer userNumber, List<String> roles) {
        return createToken(userEmail, userNumber, roles, accessTokenValidity);
    }

    // Refresh Token 생성
    public String createRefreshToken(String userEmail, Integer userNumber) {
        return createToken(userEmail,userNumber, null, refreshTokenValidity);
    }

    // 공통적으로 토큰 생성하는 로직
    public String createToken(String userEmail, Integer userNumber, List<String> roles, long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("userNumber", userNumber);
        if (roles != null &&!roles.isEmpty()) {
            claims.put("roles", roles);
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        // JWT 가 유효한지 검증
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    // JWT에서 사용자 이메일을 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    // JWT에서 사용자 ID 추출
    public Integer getUserNumber(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.get("userNumber", Integer.class);  // 저장한 userId 추출
    }

    // Refresh Token이 유효하다면 새로운 Access Token 발급
    public String refreshAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            // Refresh Token이 유효하다면 사용자 이메일을 추출하여 새로운 Access Token 발급
            String userEmail = getUserEmail(refreshToken);
            Integer userNumber = getUserNumber(refreshToken);
            // 역할이 필요하다면, 이를 추가하는 로직도 필요 (예시에서는 제외)
            return createAccessToken(userEmail, userNumber, null);
        } else {
            throw new JwtException("유효하지 않은 Refresh Token입니다.");
        }
    }
}
