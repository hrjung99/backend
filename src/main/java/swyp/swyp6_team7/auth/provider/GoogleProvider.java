package swyp.swyp6_team7.auth.provider;

import org.springframework.stereotype.Service;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;

@Service
public class GoogleProvider implements SocialLoginProvider {
    @Override
    public boolean supports(String provider) {
        return "google".equalsIgnoreCase(provider);  // 구글 제공자를 식별
    }
    @Override
    public SocialUserDTO getUserInfo(String code, String state) {
        // 구글 API를 사용해 액세스 토큰 및 사용자 정보 가져오기 로직 구현
        SocialUserDTO userDTO = new SocialUserDTO();
        // 사용자 정보를 userDTO에 설정
        return userDTO;
    }
}