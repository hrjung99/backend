package swyp.swyp6_team7.auth.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.dto.LoginRequestDto;
import swyp.swyp6_team7.auth.service.LoginService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.UserLoginHistoryService;
import swyp.swyp6_team7.member.service.MemberService;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private UserLoginHistoryService userLoginHistoryService;

    @MockBean
    private MemberService memberService;

    @Test
    public void testLoginSuccess() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("password");

        Users user = new Users();
        user.setUserEmail("test@example.com");

        Map<String, String> mockedTokenMap = new HashMap<>();
        mockedTokenMap.put("accessToken", "mocked-access-token");

        Mockito.when(loginService.login(any(LoginRequestDto.class), any()))
                .thenReturn(mockedTokenMap);

        Mockito.when(loginService.getUserByEmail(anyString()))
                .thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bearer mocked-access-token"));
    }

    @Test
    public void testLoginFailedDueToWrongPassword() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("wrongpassword");

        Mockito.when(loginService.login(any(LoginRequestDto.class), any()))
                .thenThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@example.com\", \"password\": \"wrongpassword\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void testLoginFailedDueToNonExistentEmail() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("nonexistent@example.com");
        loginRequestDto.setPassword("password");

        Mockito.when(loginService.login(any(LoginRequestDto.class), any()))
                .thenThrow(new UsernameNotFoundException("사용자 이메일을 찾을 수 없습니다."));

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"nonexistent@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("사용자 이메일을 찾을 수 없습니다."));
    }
}
