package swyp.swyp6_team7.auth.controller;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @WithMockUser
    void testRefreshTokenSuccess() throws Exception {
        String validRefreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";

        Mockito.when(jwtProvider.refreshAccessToken(validRefreshToken)).thenReturn(newAccessToken);

        mockMvc.perform(post("/api/token/refresh")
                        .cookie(new Cookie("refreshToken", validRefreshToken)))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshTokenInvalid() throws Exception {
        String invalidRefreshToken = "invalid-refresh-token";

        Mockito.when(jwtProvider.refreshAccessToken(invalidRefreshToken)).thenThrow(new JwtException("Invalid token"));

        mockMvc.perform(post("/api/token/refresh")
                        .cookie(new Cookie("refreshToken", invalidRefreshToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testRefreshTokenMissing() throws Exception {
        // 쿠키 없이 요청을 보냄
        mockMvc.perform(post("/api/token/refresh"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Refresh Token이 존재하지 않습니다."));
    }
}
