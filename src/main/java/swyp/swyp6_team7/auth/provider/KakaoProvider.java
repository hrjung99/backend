package swyp.swyp6_team7.auth.provider;

import org.springframework.stereotype.Service;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoProvider implements SocialLoginProvider {
    @Override
    public boolean supports(String provider) {
        return "kakao".equalsIgnoreCase(provider);  // 카카오 제공자를 식별
    }
    @Override
    public Map<String, String> getUserInfo(String code, String state) {
        Map<String, String> userInfo = new HashMap<>();

        return userInfo;
    }
}
