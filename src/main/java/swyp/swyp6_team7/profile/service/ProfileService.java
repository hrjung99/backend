package swyp.swyp6_team7.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.profile.repository.UserProfileRepository;
import swyp.swyp6_team7.profile.entity.UserProfile;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.profile.dto.ProfileCreateRequest;
import swyp.swyp6_team7.profile.dto.ProfileUpdateRequest;
import swyp.swyp6_team7.tag.domain.Tag;
import swyp.swyp6_team7.tag.domain.UserTagPreference;
import swyp.swyp6_team7.tag.repository.TagRepository;
import swyp.swyp6_team7.tag.repository.UserTagPreferenceRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UserTagPreferenceRepository userTagPreferenceRepository;

    public ProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository, TagRepository tagRepository, UserTagPreferenceRepository userTagPreferenceRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.userTagPreferenceRepository = userTagPreferenceRepository;  // 추가
    }

    public void createProfile(ProfileCreateRequest profileCreateRequest) {
        // 프로필 엔티티 생성
        UserProfile userProfile = new UserProfile();
        userProfile.setUserNumber(profileCreateRequest.getUserNumber());

        // 프로필 저장
        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void updateProfile(Integer userNumber,ProfileUpdateRequest request) {

        // Users 엔티티 업데이트
        Users user = userRepository.findUserWithTags(userNumber)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserName(request.getName());  // 이름 수정 가능
        userRepository.save(user);

        // UserProfile 엔티티 업데이트
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserNumber(userNumber);
        if (userProfileOpt.isEmpty()) {
            throw new IllegalArgumentException("프로필이 존재하지 않습니다. 새 프로필을 생성해주세요.");
        }
        UserProfile userProfile = userProfileOpt.get();
        userProfile.setProIntroduce(request.getProIntroduce());  // 자기소개 수정
        userProfileRepository.save(userProfile);  // UserProfile 저장

        // 선호 태그 업데이트
        if (request.getPreferredTags() != null && request.getPreferredTags().length > 0) {
            Set<UserTagPreference> tagPreferences = user.getTagPreferences();
            tagPreferences.clear();  // 기존 태그 삭제

            Set<Tag> preferredTags = new HashSet<>();

            for (String tagName : request.getPreferredTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = Tag.of(tagName);  // 태그가 없으면 새로 생성
                            tagRepository.save(newTag);
                            return newTag;
                        });

                UserTagPreference userTagPreference = new UserTagPreference();
                userTagPreference.setUser(user);
                userTagPreference.setTag(tag);
                tagPreferences.add(userTagPreference);  // 새로운 태그 추가
                preferredTags.add(tag);
            }
            userTagPreferenceRepository.saveAll(tagPreferences);
        }
        // 영속성 컨텍스트를 강제로 flush하여 DB에 반영
        userRepository.flush();
        userTagPreferenceRepository.flush();

    }

    public Optional<UserProfile> getProfileByUserNumber(Integer userNumber) {
        return userProfileRepository.findByUserNumber(userNumber);
    }

    public Optional<Users> getUserByUserNumber(Integer userNumber) {
        return userRepository.findUserWithTags(userNumber);
    }


}
