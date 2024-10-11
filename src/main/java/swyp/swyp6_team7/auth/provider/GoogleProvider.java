package swyp.swyp6_team7.auth.provider;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleProvider implements SocialLoginProvider {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean supports(String provider) {
        return "google".equalsIgnoreCase(provider);  // 구글 제공자를 식별
    }


    public Map<String, String> getUserInfoFromGoogle(String code) {
        // 구글 API에서 사용자 정보 가져오기
        String accessToken = getAccessToken(code);

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        String socialLoginId = (String) response.getBody().get("sub");  // 구글의 고유 사용자 ID
        String email = (String) response.getBody().get("email");

        return Map.of(
                "socialLoginId", socialLoginId,
                "email", email
        );
    }

    private String getAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "YOUR_GOOGLE_CLIENT_ID");
        params.add("client_secret", "YOUR_GOOGLE_CLIENT_SECRET");
        params.add("redirect_uri", "YOUR_GOOGLE_REDIRECT_URI");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        return (String) response.getBody().get("access_token");
    }
}