package swyp.swyp6_team7.https;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "kakao.client-id=fake-client-id",
        "kakao.client-secret=fake-client-secret",
        "kakao.redirect-uri=http://localhost:8080/login/oauth2/code/kakao",
        "kakao.token-url=https://kauth.kakao.com/oauth/token",
        "kakao.user-info-url=https://kapi.kakao.com/v2/user/me"
})
public class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andDo(print())
                .andExpect(status().isOk()) // 200 OK 상태 코드 확인
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("UP")); // JSON 응답에서 "status": "UP" 확인
    }
}