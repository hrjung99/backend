package swyp.swyp6_team7.enrollment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.enrollment.dto.EnrollmentCreateRequest;
import swyp.swyp6_team7.enrollment.service.EnrollmentService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @PostMapping("/api/enrollment")
    public ResponseEntity create(
            @RequestBody @Validated EnrollmentCreateRequest request, Principal principal
    ) {
        enrollmentService.create(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("여행 참가 신청이 완료되었습니다.");
    }

}
