package swyp.swyp6_team7.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.image.domain.Image;
import swyp.swyp6_team7.image.dto.request.ImageCreateRequestDto;
import swyp.swyp6_team7.image.dto.response.ImageDetailResponseDto;
import swyp.swyp6_team7.image.repository.ImageRepository;
import swyp.swyp6_team7.image.s3.S3Uploader;
import swyp.swyp6_team7.image.util.FileNameHandler;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final FileNameHandler fileNameHandler;

    @Transactional
    public Image uploadImage(MultipartFile imageFile, String relatedType, int relatedNumber) throws IOException {
        // 데이터베이스에서 기존 이미지 조회
        Optional<Image> existingImageOpt = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber);

        // 관련 타입이 "profile"인 경우
        if ("profile".equals(relatedType) && existingImageOpt.isPresent()) {
            // 기존 이미지가 있으면 S3에서 삭제
            Image existingImage = existingImageOpt.get();
            s3Uploader.deleteFile(existingImage.getPath()); // 기존 이미지의 S3 경로 사용하여 삭제

            // 데이터베이스에서 기존 이미지 삭제
            imageRepository.delete(existingImage);
        }

        // S3에 이미지 파일 업로드 후 URL 경로 받아오기
        String folderPath = s3Uploader.upload(imageFile, relatedType, relatedNumber);

        // 메타 데이터 뽑아서 create dto에 담기
        ImageCreateRequestDto imageCreateDto = ImageCreateRequestDto.builder()
                .originalName(imageFile.getOriginalFilename())
                .storageName(fileNameHandler.generateUniqueFileName(imageFile.getOriginalFilename()))
                .size(imageFile.getSize())
                .format(imageFile.getContentType())
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .path(folderPath)
                .build();

        // DB에 저장
        Image image = imageCreateDto.toImageEntity(imageCreateDto.getOriginalName(),
                imageCreateDto.getStorageName(),
                imageCreateDto.getSize(),
                imageCreateDto.getFormat(),
                imageCreateDto.getRelatedType(),
                imageCreateDto.getRelatedNumber(),
                imageCreateDto.getPath());
        return imageRepository.save(image);
    }

    //이미지 정보
    public ImageDetailResponseDto getImageDetailByNumber(String relatedType, int relatedNumber) {
        Image image = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다." + relatedType + ":" + relatedNumber));

        String url = getImageUrl(image.getRelatedType(), image.getRelatedNumber());
        return new ImageDetailResponseDto(image, url);
    }

    //이미지 삭제
    @Transactional
    public void deleteImage(String relatedType, int relatedNumber) {
        // 데이터베이스에서 이미지 정보 조회
        Image image = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다. "));

        // S3에서 파일 삭제
        s3Uploader.deleteFile(image.getPath()); // 이미지의 경로를 사용하여 S3에서 삭제

        // 데이터베이스에서 이미지 삭제
        imageRepository.delete(image);
    }


    // 이미지 URL 조회
    public String getImageUrl(String relatedType, int relatedNumber) {
        s3Uploader.getImageUrl(relatedType, relatedNumber);
        return s3Uploader.getImageUrl(relatedType, relatedNumber);
    }


}
