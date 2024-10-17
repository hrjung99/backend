package swyp.swyp6_team7.auth.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.dto.LoginRequestDto;
import swyp.swyp6_team7.auth.service.LoginService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.member.service.UserLoginHistoryService;
import swyp.swyp6_team7.member.service.MemberService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "kakao.client-id=fake-client-id",
        "kakao.client-secret=fake-client-secret",
        "kakao.redirect-uri=http://localhost:8080/login/oauth2/code/kakao",
        "kakao.token-url=https://kauth.kakao.com/oauth/token",
        "kakao.user-info-url=https://kapi.kakao.com/v2/user/me"
})
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private UserLoginHistoryService userLoginHistoryService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoginSuccess() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "password";
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("password");

        Users mockUser = Users.builder()
                .userNumber(123)
                .userEmail(email)
                .userPw("encodedPassword")
                .userSocialTF(false)
                .build();

        Map<String, String> mockedTokenMap = new HashMap<>();
        mockedTokenMap.put("accessToken", "mocked-access-token");
        mockedTokenMap.put("refreshToken", "mocked-refresh-token");

        Mockito.when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(mockUser));  // 사용자 조회 설정 추가
        Mockito.when(loginService.login(any(LoginRequestDto.class))).thenReturn(mockedTokenMap);
        Mockito.when(loginService.getUserByEmail(anyString())).thenReturn(mockUser);


        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked-access-token"))
                .andExpect(cookie().value("refreshToken", "mocked-refresh-token"))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().secure("refreshToken", true));

    }

    @Test
    public void testLoginFailedDueToWrongPassword() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("wrongpassword");

        Mockito.when(loginService.login(any(LoginRequestDto.class)))
                .thenThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@example.com\", \"password\": \"wrongpassword\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void testLoginFailedDueToNonExistentEmail() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("nonexistent@example.com");
        loginRequestDto.setPassword("password");

        Mockito.when(loginService.login(any(LoginRequestDto.class)))
                .thenThrow(new UsernameNotFoundException("사용자 이메일을 찾을 수 없습니다."));

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"nonexistent@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("사용자 이메일을 찾을 수 없습니다."));
    }
}
