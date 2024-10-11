package swyp.swyp6_team7.auth.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class NaverProvider implements SocialLoginProvider{

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NaverProvider(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(String provider) {
        return "naver".equalsIgnoreCase(provider);  // 네이버 제공자를 식별
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public Map<String, String> getUserInfo(String code, String state) {
        // 1. Access Token 가져오기
        String accessToken = getAccessToken(code, state);

        // 2. Access Token으로 사용자 정보 요청
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                Map<String, Object> responseBody = (Map<String, Object>) result.get("response");

                // 사용자 정보를 Map으로 변환하여 반환
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("email", (String) responseBody.get("email"));
                userInfo.put("name", (String) responseBody.get("name"));
                userInfo.put("gender", (String) responseBody.get("gender"));
                userInfo.put("ageGroup", getAgeGroup((String) responseBody.get("age")));
                userInfo.put("socialNumber", (String) responseBody.get("id")); // 네이버의 소셜 고유 ID
                userInfo.put("provider", "naver");

                return userInfo;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse user info from Naver", e);
            }
        } else {
            throw new RuntimeException("Failed to get user info from Naver");
        }
    }

    private String getAccessToken(String code, String state) {
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
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                return (String) result.get("access_token");
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse access token", e);
            }
        } else {
            throw new RuntimeException("Failed to get access token");
        }
    }

    private String getAgeGroup(String ageRange) {
        if (ageRange != null) {
            if (ageRange.startsWith("10")) return "10대";
            else if (ageRange.startsWith("20")) return "20대";
            else if (ageRange.startsWith("30")) return "30대";
            else if (ageRange.startsWith("40")) return "40대";
        }
        return "50대 이상";
    }
}
