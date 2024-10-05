package swyp.swyp6_team7.image.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.image.domain.Image;
import swyp.swyp6_team7.image.dto.response.ImageDetailResponseDto;
import swyp.swyp6_team7.image.service.ImageService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;
    private final JwtProvider jwtProvider;

    @PostMapping("/api/{relatedType}/{relatedNumber}/image/")
    public ResponseEntity<ImageDetailResponseDto> uploadImage(
            @PathVariable String relatedType,
            @PathVariable int relatedNumber,
            @RequestParam("file") MultipartFile imageFile,
            @RequestHeader("Authorization") String token) {

        try {
            // 이미지 업로드
            Image createdImage = imageService.uploadImage(imageFile, relatedType, relatedNumber);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(imageService.getImageDetailByNumber(createdImage.getImageNumber()));
        } catch (IOException e) {
            // IOException 발생 시 로그를 남기고, 에러 응답 리턴
            log.error("Image upload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
