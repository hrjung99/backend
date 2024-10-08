package swyp.swyp6_team7.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.profile.dto.PasswordChangeRequest;
import swyp.swyp6_team7.profile.service.ProfileService;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final ProfileService profileService;
    private final JwtProvider jwtProvider;

    public PasswordController(ProfileService profileService, JwtProvider jwtProvider) {
        this.profileService = profileService;
        this.jwtProvider = jwtProvider;
    }

    // 현재 비밀번호 확인
    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCurrentPassword(@RequestHeader("Authorization") String token, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        Integer userNumber = jwtProvider.getUserNumber(token.substring(7)); // "Bearer " 제거
        profileService.verifyCurrentPassword(userNumber, passwordChangeRequest.getCurrentPassword());
        return ResponseEntity.ok().build();
    }

    // 새 비밀번호 설정
    @PutMapping("/change")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        Integer userNumber = jwtProvider.getUserNumber(token.substring(7)); // "Bearer " 제거
        profileService.changePassword(userNumber, passwordChangeRequest.getNewPassword(), passwordChangeRequest.getNewPasswordConfirm());
        return ResponseEntity.ok().build();
    }
}

