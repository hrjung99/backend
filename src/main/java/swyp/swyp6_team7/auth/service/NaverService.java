package swyp.swyp6_team7.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class NaverService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NaverService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // 네이버에서 Access Token 요청
    public String getAccessToken(String code, String state, HttpSession session) {

        String tokenUrl = "https://nid.naver.com/oauth2.0/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // 액세스 토큰 추출
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                return (String) result.get("access_token");
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse access token", e);
            }
        } else {
            throw new RuntimeException("Failed to get access token");
        }
    }


    // 액세스 토큰을 사용해 네이버에서 사용자 정보를 가져오는 메서드
    public Map<String, Object> fetchNaverUserInfo(String accessToken) {
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                return (Map<String, Object>) result.get("response");
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse user info from Naver", e);
            }
        } else {
            throw new RuntimeException("Failed to get user info from Naver");
        }
    }
}
