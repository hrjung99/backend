package swyp.swyp6_team7.auth.controller;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import swyp.swyp6_team7.member.entity.UserRole;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    public void testRefreshTokenSuccess() throws Exception {
        // given
        String refreshToken = "validRefreshToken";
        String userEmail = "test@example.com";
        Integer userNumber = 1;
        String newAccessToken = "newAccessToken";
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        Users user = new Users();
        user.setUserEmail(userEmail);
        user.setUserNumber(userNumber);
        List<String> roles = List.of("ROLE_USER");

        when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getUserEmail(refreshToken)).thenReturn(userEmail);
        when(userRepository.findByUserEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtProvider.createAccessToken(eq(userEmail), eq(userNumber), any(List.class))).thenReturn(newAccessToken);

        // when & then
        mockMvc.perform(post("/api/token/refresh")
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Bearer " + newAccessToken));
    }

    @Test
    void testRefreshTokenInvalid() throws Exception {
        String invalidRefreshToken = "invalid-refresh-token";

        when(jwtProvider.refreshAccessToken(invalidRefreshToken)).thenThrow(new JwtException("Invalid token"));

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
