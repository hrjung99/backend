package swyp.swyp6_team7.profile.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.profile.dto.ProfileViewResponse;
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
    private final JwtProvider jwtProvider;

    public ProfileController(ProfileService profileService, JwtProvider jwtProvider) {
        this.profileService = profileService;
        this.jwtProvider = jwtProvider;
    }

    // 프로필 수정 (이름, 자기소개, 선호 태그)
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request, HttpServletRequest httpServletRequest) {
        // JWT 토큰에서 userNumber 추출
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(token);

        // 프로필 수정 로직 호출
        profileService.updateProfile(userNumber, request);  // userNumber는 토큰에서 추출된 값

        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody ProfileCreateRequest request) {
        profileService.createProfile(request);
        return ResponseEntity.ok("프로필이 생성되었습니다.");
    }
    //프로필 조회 (이름, 이메일, 연령대, 성별, 선호 태그, 자기소개)
    @GetMapping("/me")
    public ResponseEntity<?> viewProfile(HttpServletRequest request) {
        // Authorization 헤더에서 JWT 토큰을 가져옴
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        // 토큰에서 userNumber 추출
        Integer userNumber = jwtProvider.getUserNumber(token);

        Optional<Users> userOpt = profileService.getUserByUserNumberWithTags(userNumber);
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

}
