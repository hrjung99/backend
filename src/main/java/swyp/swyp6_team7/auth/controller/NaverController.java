package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import swyp.swyp6_team7.auth.service.NaverService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

@RestController
public class NaverController {

    private final NaverService naverService;

    public NaverController(NaverService naverService) {
        this.naverService = naverService;
    }

    // 네이버 로그인 리다이렉트 URL
    @GetMapping("/login/oauth/naver")
    public void naverLoginRedirect(HttpServletResponse response) throws IOException {
        String clientId = "kSCqWxTinQzYy5tipaNp"; // 네이버 클라이언트 ID
        String redirectUri = "http://localhost:8080/login/oauth/naver/callback";
        String state = "RANDOM_STATE"; // CSRF 방지용 state 값

        String naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;
        response.sendRedirect(naverAuthUrl);
    }

    // 네이버 콜백 처리
    @GetMapping("/login/oauth/naver/callback")
    public ResponseEntity<?> naverCallback(@RequestParam String code, @RequestParam String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "YOUR_CLIENT_ID");
        params.add("client_secret", "YOUR_CLIENT_SECRET");
        params.add("code", code);
        params.add("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 네이버로부터 액세스 토큰을 받아 처리
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get access token");
        }
    }
}
