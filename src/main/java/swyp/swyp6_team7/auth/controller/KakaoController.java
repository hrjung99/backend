package swyp.swyp6_team7.auth.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.auth.service.KakaoService;

import java.io.IOException;
import java.util.Map;

@RestController
public class KakaoController {
    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    // 카카오 로그인 리디렉션
    @GetMapping("/login/oauth/kakao")
    public void kakaoLoginRedirect(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoService.getKakaoConfig().getClientId()
                + "&redirect_uri=" + kakaoService.getKakaoConfig().getRedirectUri()
                + "&response_type=code";
        response.sendRedirect(kakaoAuthUrl);
    }

    // 카카오 인증 후, 리다이렉트된 URI에서 코드를 처리
    @GetMapping("/login/oauth/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {
        // 액세스 토큰 가져오기
        String accessToken = kakaoService.getAccessToken(code);

        // 액세스 토큰을 이용해 사용자 정보 가져오기
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        // 로그인 또는 회원가입 처리 (추가 로직 필요)
        return ResponseEntity.ok(userInfo);
    }
}
