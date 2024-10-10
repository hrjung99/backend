package swyp.swyp6_team7.image.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.image.domain.Image;
import swyp.swyp6_team7.image.dto.response.ImageDetailResponseDto;
import swyp.swyp6_team7.image.service.ImageService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{relatedType}/{relatedNumber}")
    public ResponseEntity<ImageDetailResponseDto> uploadImage(
            @PathVariable String relatedType,
            @PathVariable int relatedNumber,
            @RequestParam("file") MultipartFile image,
            Principal principal) throws IOException {
        if (!"profile".equals(relatedType)) {
            return ResponseEntity.badRequest().body(null); // 관련 타입이 profile이 아닌 경우 에러 처리
        }

        MultipartFile[] images = new MultipartFile[1];
        images[0] = image; // 배열에 이미지 추가
        try {
            // 이미지 업로드
            ImageDetailResponseDto[] createdImages = imageService.uploadImage(images, relatedType, relatedNumber);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdImages[0]);
        } catch (IOException e) {
            log.error("Image upload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{relatedType}/{relatedNumber}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable String relatedType,
            @PathVariable int relatedNumber) {
        if (!"profile".equals(relatedType)) {
            return ResponseEntity.badRequest().build(); // 관련 타입이 profile이 아닌 경우 에러 처리
        }

        try {
            // 이미지 삭제
            imageService.deleteImage(relatedType, relatedNumber);
            return ResponseEntity.noContent().build(); // 성공적으로 삭제된 경우
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 이미지가 존재하지 않는 경우
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 기타 에러 처리
        }
    }

    @GetMapping("/{relatedType}/{relatedNumber}")
    public ResponseEntity<ImageDetailResponseDto> getImageDetail(
            @PathVariable String relatedType,
            @PathVariable int relatedNumber) {
        try {
            // 이미지 상세 조회
            ImageDetailResponseDto imageDetail = imageService.getImageDetailByNumber(relatedType, relatedNumber);
            return ResponseEntity.ok(imageDetail); // 조회 성공
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ImageDetailResponseDto(null, "해당 프로필 사진이 존재하지 않습니다.")); // 이미지가 존재하지 않는 경우
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 기타 에러 처리
        }
    }
}
