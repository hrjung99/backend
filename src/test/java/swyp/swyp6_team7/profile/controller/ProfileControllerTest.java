package swyp.swyp6_team7.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import swyp.swyp6_team7.member.entity.Gender;
import swyp.swyp6_team7.member.entity.AgeGroup;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.profile.dto.ProfileCreateRequest;
import swyp.swyp6_team7.profile.dto.ProfileUpdateRequest;
import swyp.swyp6_team7.profile.dto.ProfileViewResponse;
import swyp.swyp6_team7.profile.entity.UserProfile;
import swyp.swyp6_team7.profile.service.ProfileService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private JwtProvider jwtProvider;

    private String validToken;
    private Integer userNumber;
    private Users user;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        validToken = "validToken";
        userNumber = 1;

        user = new Users();
        user.setUserEmail("test@example.com");
        user.setUserNumber(userNumber);

        userProfile = new UserProfile();
        userProfile.setProIntroduce("Hello, I am a user.");

        // Mock JWT Provider
        when(jwtProvider.getUserNumber(validToken)).thenReturn(userNumber);
    }

    @Test
    @WithMockUser
    void testUpdateProfile_Success() throws Exception {
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setProIntroduce("Updated introduction");

        mockMvc.perform(put("/api/profile/update")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"introduce\": \"Updated introduction\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile updated successfully"));

        verify(profileService).updateProfile(eq(userNumber), any(ProfileUpdateRequest.class));
    }

    @Test
    @WithMockUser
    void testCreateProfile_Success() throws Exception {
        // 프로필 생성 요청에 userNumber 포함
        ProfileCreateRequest request = new ProfileCreateRequest();
        request.setUserNumber(1);  // 회원번호 설정

        // 서비스 메서드가 정상적으로 호출되는지 확인
        doNothing().when(profileService).createProfile(any(ProfileCreateRequest.class));

        mockMvc.perform(post("/api/profile/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("프로필이 생성되었습니다."));

        // profileService.createProfile이 호출되었는지 검증
        verify(profileService).createProfile(any(ProfileCreateRequest.class));
    }

    @Test
    @WithMockUser
    void testViewProfile_Success() throws Exception {
        Users mockUser = new Users();
        mockUser.setUserEmail("test@example.com");
        mockUser.setUserNumber(1);
        mockUser.setUserGender(Gender.M);  // gender 설정
        mockUser.setUserAgeGroup(AgeGroup.TWENTY);  // ageGroup 설정

        UserProfile mockUserProfile = new UserProfile();
        mockUserProfile.setProIntroduce("Test introduction");

        // mock 프로필 서비스의 동작 설정
        when(profileService.getUserByUserNumber(anyInt())).thenReturn(Optional.of(mockUser));
        when(profileService.getProfileByUserNumber(anyInt())).thenReturn(Optional.of(mockUserProfile));

        // 요청 수행 및 기대 결과 검증
        mockMvc.perform(get("/api/profile/me")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testViewProfile_UserNotFound() throws Exception {
        when(profileService.getUserByUserNumber(userNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profile/me")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    @WithMockUser
    void testViewProfile_ProfileNotFound() throws Exception {
        when(profileService.getUserByUserNumber(userNumber)).thenReturn(Optional.of(user));
        when(profileService.getProfileByUserNumber(userNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profile/me")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User profile not found"));
    }
}
