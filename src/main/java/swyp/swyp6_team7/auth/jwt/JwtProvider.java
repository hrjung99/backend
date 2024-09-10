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
    private final String secretKey = "your_secret_key";
    private final long validityInMilliseconds = 3600000;  // 1시간

    public String createToken(String userEmail, List<String> roles) {
        // 사용자 이메일과 역할을 기반으로 JWT 생성
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("roles", roles);

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

    public String getUserEmail(String token) {
        // JWT에서 사용자 이메일을 추출
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
