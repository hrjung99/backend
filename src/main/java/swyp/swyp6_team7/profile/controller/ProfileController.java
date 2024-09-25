package swyp.swyp6_team7.profile.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.profile.entity.UserProfile;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.profile.dto.ProfileCreateRequest;
import swyp.swyp6_team7.profile.dto.ProfileUpdateRequest;
import swyp.swyp6_team7.profile.service.ProfileService;
import swyp.swyp6_team7.tag.domain.Tag;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;


    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;

    }

    // 프로필 수정 (이름, 자기소개, 선호 태그)
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request) {
        profileService.updateProfile(request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody ProfileCreateRequest request) {
        profileService.createProfile(request);
        return ResponseEntity.ok("프로필이 생성되었습니다.");
    }
    //프로필 조회 (이름, 이메일, 연령대, 성별, 선호 태그, 자기소개)
    @GetMapping("/me")
    public ResponseEntity<?> viewProfile(@RequestParam("userNumber")  Integer userNumber) {
        Optional<Users> userOpt = profileService.getUserByUserNumber(userNumber);
        Optional<UserProfile> userProfileOpt = profileService.getProfileByUserNumber(userNumber);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (userProfileOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User profile not found");
        }

        // 필요한 사용자 정보와 프로필 정보 반환
        return ResponseEntity.ok(new ProfileViewResponse(userOpt.get(), userProfileOpt.get()));
    }

    // 프로필 조회 응답 객체
    public static class ProfileViewResponse {
        private String email;
        private String name;
        private String gender;
        private String ageGroup;
        private String proIntroduce;
        private String[] preferredTags;

        public ProfileViewResponse(Users user, UserProfile userProfile) {
            this.email = user.getUserEmail();
            this.name = user.getUserName();
            this.gender =  user.getUserGender().name();
            this.ageGroup = user.getUserAgeGroup().getValue();
            this.proIntroduce = userProfile.getProIntroduce();

            Set<Tag> tagSet = userProfile.getPreferredTags();
            this.preferredTags = tagSet.stream()
                    .map(Tag::getName)
                    .toArray(String[]::new);  // 태그 이름 배열로 변환
        }

        public String getEmail() {
            return email;
        }

        public String[] getPreferredTags() {
            return preferredTags;
        }

        public String getGender() {
            return gender;
        }

        public String getAgeGroup() {
            return ageGroup;
        }

        public String getName() {
            return name;
        }

        public String getProIntroduce() {
            return proIntroduce;
        }
    }
}
