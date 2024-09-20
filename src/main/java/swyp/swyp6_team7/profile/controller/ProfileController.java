package swyp.swyp6_team7.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.member.entity.UserProfile;
import swyp.swyp6_team7.member.repository.UserProfileRepository;
import swyp.swyp6_team7.profile.dto.ProfileCreateRequest;
import swyp.swyp6_team7.profile.dto.ProfileUpdateRequest;
import swyp.swyp6_team7.profile.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserProfileRepository userProfileRepository;

    public ProfileController(ProfileService profileService,UserProfileRepository userProfileRepository) {
        this.profileService = profileService;
        this.userProfileRepository = userProfileRepository;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request) {
        System.out.println("Request Body: " + request);

        profileService.updateProfile(request);
        return ResponseEntity.ok("Profile updated successfully");
    }
    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody ProfileCreateRequest request) {
        profileService.createProfile(request);
        return ResponseEntity.ok("프로필이 생성되었습니다.");
    }
}
