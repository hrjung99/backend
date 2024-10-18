package swyp.swyp6_team7.auth.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import swyp.swyp6_team7.auth.dto.SignupRequestDto;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.auth.provider.GoogleProvider;
import swyp.swyp6_team7.member.entity.*;
import swyp.swyp6_team7.member.repository.SocialUserRepository;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GoogleService {
    private final GoogleProvider googleProvider;
    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    private final JwtProvider jwtProvider;
    @Autowired
    public GoogleService(GoogleProvider googleProvider, UserRepository userRepository,
                        SocialUserRepository socialUserRepository, JwtProvider jwtProvider) {
        this.googleProvider = googleProvider;
        this.userRepository = userRepository;
        this.socialUserRepository = socialUserRepository;
        this.jwtProvider = jwtProvider;
    }

    public Map<String, String> getUserInfoFromGoogle(String code) {
        return googleProvider.getUserInfoFromGoogle(code);
    }

    @Transactional
    public Map<String, String> processGoogleLogin(String code) {
        // google API에서 사용자 정보 가져오기
        Map<String, String> userInfo = googleProvider.getUserInfoFromGoogle(code);

        String socialLoginId = userInfo.get("socialLoginId");
        String email = userInfo.get("email");
        String name = userInfo.get("name");

        // 사용자 정보 DB에 저장 (기존 사용자 없으면 새로 생성)
        Users user = saveSocialUser(userInfo);

        // 반환할 사용자 정보를 Map에 담기
        Map<String, String> response = new HashMap<>();
        response.put("userNumber",user.getUserNumber().toString());
        response.put("userName", user.getUserName());
        response.put("userEmail", user.getUserEmail());
        response.put("userStatus", user.getUserStatus().toString());
        response.put("socialLoginId", socialLoginId);

        return response;  // 사용자 정보를 담은 Map 반환
    }

    @Transactional
    public Map<String, String> completeSignup(@RequestBody SignupRequestDto signupData) {
        if (signupData.getUserNumber() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        Optional<Users> optionalUser = userRepository.findById(signupData.getUserNumber());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = optionalUser.get();
        user.setUserGender(Gender.valueOf(signupData.getGender().toUpperCase()));
        user.setUserAgeGroup(AgeGroup.fromValue(signupData.getAgeGroup()));
        user.setUserStatus(UserStatus.ABLE);  // 회원가입 완료

        // 선호 태그 처리
        if (signupData.getPreferredTags() != null) {
            Set<String> preferredTags = signupData.getPreferredTags();
            // 선호 태그 처리 로직 추가 (필요에 따라 태그를 User 엔티티에 저장하는 로직)
        }

        userRepository.save(user);

        Optional<SocialUsers> existingSocialUser = socialUserRepository.findByUser(user);
        String socialLoginId = null;
        String email = user.getUserEmail();

        if (existingSocialUser.isPresent()) {
            SocialUsers socialUser = existingSocialUser.get();
            socialLoginId = socialUser.getSocialLoginId();
            socialUserRepository.save(socialUser);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Signup complete");
        response.put("socialLoginId", socialLoginId != null ? socialLoginId : "N/A");
        response.put("email",email);

        return response;
    }

    @Transactional
    private Users saveSocialUser(Map<String, String> userInfo) {
        String email = userInfo.get("email");
        String socialLoginId = userInfo.get("socialLoginId");
        String name = userInfo.get("name");

        Optional<Users> existingUser = userRepository.findByUserEmail(email);
        Users user;

        if (existingUser.isPresent()) {
            user = existingUser.get();  // 기존 사용자 정보
        } else {
            // 사용자 정보가 없으면 새로 생성
            user = new Users();
            user.setUserEmail(email);
            user.setUserName(name != null ? name : "Unknown");
            user.setUserPw("social-login");
            user.setUserGender(Gender.NULL);  // 임시 성별
            user.setUserAgeGroup(AgeGroup.UNKNOWN);  // 임시 연령대
            user.setUserSocialTF(true);
            user.setUserStatus(UserStatus.PENDING);  // 회원가입 미완료 상태

            user = userRepository.save(user);  // 저장 후 반환
        }

        // SocialUsers 테이블에 소셜 정보 저장
        if (!socialUserRepository.existsBySocialLoginId(socialLoginId)) {
            SocialUsers socialUser = new SocialUsers();
            socialUser.setUser(user);
            socialUser.setSocialLoginId(socialLoginId);
            socialUser.setSocialEmail(email);  // 임시 이메일
            socialUser.setSocialProvider(SocialProvider.GOOGLE);

            socialUserRepository.save(socialUser);  // SocialUsers 엔티티 저장
        }

        return user;
    }
}
