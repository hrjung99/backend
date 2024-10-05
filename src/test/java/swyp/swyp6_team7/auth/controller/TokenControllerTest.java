package swyp.swyp6_team7.auth.controller;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRefreshAccessTokenSuccess() throws Exception {
        // Given
        String validRefreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        String userEmail = "test@example.com";

        Cookie refreshTokenCookie = new Cookie("refreshToken", validRefreshToken);

        Users user = new Users();
        user.setUserEmail(userEmail);
        user.setUserNumber(123);

        when(jwtProvider.validateToken(validRefreshToken)).thenReturn(true);
        when(jwtProvider.getUserEmail(validRefreshToken)).thenReturn(userEmail);
        when(userRepository.findByUserEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtProvider.createAccessToken(user.getUserEmail(), user.getUserNumber(), List.of(user.getRole().name())))
                .thenReturn(newAccessToken);

        // When & Then
        mockMvc.perform(post("/api/token/refresh")
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken));
    }

    @Test
    public void testRefreshAccessTokenFailureDueToMissingRefreshToken() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Refresh Token이 존재하지 않습니다."));
    }

    @Test
    public void testRefreshAccessTokenFailureDueToInvalidToken() throws Exception {
        // Given
        String invalidRefreshToken = "invalid-refresh-token";

        Cookie refreshTokenCookie = new Cookie("refreshToken", invalidRefreshToken);

        when(jwtProvider.validateToken(invalidRefreshToken)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/token/refresh")
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Refresh Token이 만료되었습니다. 다시 로그인 해주세요."));
    }

    @Test
    public void testRefreshAccessTokenFailureDueToJwtException() throws Exception {
        // Given
        String refreshToken = "refresh-token";
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        when(jwtProvider.validateToken(refreshToken)).thenThrow(new JwtException("Invalid JWT token"));

        // When & Then
        mockMvc.perform(post("/api/token/refresh")
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Refresh Token이 유효하지 않습니다."));
    }
}
