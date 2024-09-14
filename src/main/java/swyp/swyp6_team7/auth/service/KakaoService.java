package swyp.swyp6_team7.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swyp.swyp6_team7.global.config.KakaoConfig;
import java.util.Map;

@Service
public class KakaoService {
    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate;

    public KakaoService(KakaoConfig kakaoConfig) {
        this.kakaoConfig = kakaoConfig;
        this.restTemplate = new RestTemplate();
    }
    public KakaoConfig getKakaoConfig() {
        return kakaoConfig;
    }

    // 액세스 토큰 요청
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getClientId());
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(kakaoConfig.getTokenUrl(), request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            return body != null ? (String) body.get("access_token") : null;
        }
        return null;
    }

    // 사용자 정보 요청
    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(kakaoConfig.getUserInfoUrl(), HttpMethod.GET, request, Map.class);

        return response.getBody();
    }
}
