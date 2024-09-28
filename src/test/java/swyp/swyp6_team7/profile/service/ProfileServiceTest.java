package swyp.swyp6_team7.profile.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.profile.dto.ProfileCreateRequest;
import swyp.swyp6_team7.profile.dto.ProfileUpdateRequest;
import swyp.swyp6_team7.profile.entity.UserProfile;
import swyp.swyp6_team7.profile.repository.UserProfileRepository;
import swyp.swyp6_team7.tag.domain.Tag;
import swyp.swyp6_team7.tag.domain.UserTagPreference;
import swyp.swyp6_team7.tag.repository.TagRepository;
import swyp.swyp6_team7.tag.repository.UserTagPreferenceRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserTagPreferenceRepository userTagPreferenceRepository;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("프로필 생성 테스트")
    void testCreateProfile() {
        // given
        ProfileCreateRequest request = new ProfileCreateRequest();
        request.setUserNumber(1);

        // when
        profileService.createProfile(request);

        // then
        ArgumentCaptor<UserProfile> profileCaptor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(profileCaptor.capture());
        UserProfile savedProfile = profileCaptor.getValue();

        assertThat(savedProfile.getUserNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("프로필 업데이트 테스트 - 사용자 및 프로필 존재")
    void testUpdateProfile_Success() {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setName("New Name");
        request.setProIntroduce("New Introduction");
        request.setPreferredTags(new String[]{"Tag1", "Tag2"});

        Users user = new Users();
        user.setUserNumber(1);
        user.setUserName("Old Name");
        Set<UserTagPreference> tagPreferences = new HashSet<>();
        user.setTagPreferences(tagPreferences);

        UserProfile userProfile = new UserProfile();
        userProfile.setProIntroduce("Old Introduction");

        when(userRepository.findUserWithTags(1)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserNumber(1)).thenReturn(Optional.of(userProfile));
        when(tagRepository.findByName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            Tag tag = Tag.of("testTag");
            tag.setName(tagName);
            return Optional.of(tag);
        });

        // when
        profileService.updateProfile(1, request);

        // then
        assertThat(user.getUserName()).isEqualTo("New Name");
        assertThat(userProfile.getProIntroduce()).isEqualTo("New Introduction");
        assertThat(tagPreferences).hasSize(2);

        verify(userProfileRepository).save(userProfile);
        verify(userRepository).save(user);
        verify(userTagPreferenceRepository).saveAll(anySet());
        verify(userRepository).flush();
        verify(userTagPreferenceRepository).flush();
    }

    @Test
    @DisplayName("프로필 업데이트 테스트 - 사용자 미존재")
    void testUpdateProfile_UserNotFound() {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setName("New Name");

        when(userRepository.findUserWithTags(1)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateProfile(1, request);
        });

        assertThat(exception.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("프로필 업데이트 테스트 - 프로필 미존재")
    void testUpdateProfile_ProfileNotFound() {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setName("New Name");

        Users user = new Users();
        user.setUserNumber(1);
        user.setUserName("Old Name");

        when(userRepository.findUserWithTags(1)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserNumber(1)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateProfile(1, request);
        });

        assertThat(exception.getMessage()).isEqualTo("프로필이 존재하지 않습니다. 새 프로필을 생성해주세요.");
    }

    @Test
    @DisplayName("userNumber로 프로필 조회 테스트")
    void testGetProfileByUserNumber() {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setUserNumber(1);
        when(userProfileRepository.findByUserNumber(1)).thenReturn(Optional.of(userProfile));

        // when
        Optional<UserProfile> result = profileService.getProfileByUserNumber(1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("userNumber로 사용자 조회 테스트")
    void testGetUserByUserNumber() {
        // given
        Users user = new Users();
        user.setUserNumber(1);
        when(userRepository.findUserWithTags(1)).thenReturn(Optional.of(user));

        // when
        Optional<Users> result = profileService.getUserByUserNumber(1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserNumber()).isEqualTo(1);
    }
}
