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
import swyp.swyp6_team7.tag.repository.TagRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static swyp.swyp6_team7.member.entity.QUsers.users;

@Service
public class ProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public ProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    public void createProfile(ProfileCreateRequest profileCreateRequest) {
        // 프로필 엔티티 생성
        UserProfile userProfile = new UserProfile();
        userProfile.setUserNumber(profileCreateRequest.getUserNumber());

        // 프로필 저장
        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void updateProfile(ProfileUpdateRequest request) {

        // Users 엔티티 업데이트
        Optional<Users> userOpt = userRepository.findById(request.getUserNumber());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        Users user = userOpt.get();
        user.setUserName(request.getName());  // 이름 수정 가능
        userRepository.save(user);

        // UserProfile 엔티티 업데이트
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserNumber(request.getUserNumber());
        if (userProfileOpt.isEmpty()) {
            throw new IllegalArgumentException("프로필이 존재하지 않습니다. 새 프로필을 생성해주세요.");
        }
        UserProfile userProfile = userProfileOpt.get();
        userProfile.setProIntroduce(request.getProIntroduce());  // 자기소개 수정

        // 선호 태그 업데이트
        Set<Tag> tagSet = new HashSet<>();
        for (String tagName : request.getPreferredTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        // 태그가 없으면 새로 생성
                        Tag newTag = Tag.of(tagName);
                        tagRepository.save(newTag);
                        return newTag;
                    });
            tagSet.add(tag);
        }
        userProfile.setPreferredTags(tagSet);  // 선호 태그 업데이트
        userProfileRepository.save(userProfile);  // UserProfile 저장
    }

    public Optional<UserProfile> getProfileByUserNumber(Integer userNumber) {
        return userProfileRepository.findByUserNumber(userNumber);
    }

    public Optional<Users> getUserByUserNumber(Integer userNumber) {
        return userRepository.findById(userNumber);
    }

}
