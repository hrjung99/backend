package swyp.swyp6_team7.https;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        String responseBody = "Healthy";
        return new ResponseEntity<>(responseBody, HttpStatus.OK); // 200 OK 응답
    }
}