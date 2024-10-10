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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final FileNameHandler fileNameHandler;

    @Transactional
    public ImageDetailResponseDto[] uploadImage(MultipartFile[] imageFiles, String relatedType, int relatedNumber) throws IOException {
        ImageDetailResponseDto[] responseDtos = new ImageDetailResponseDto[imageFiles.length];

        if ("profile".equals(relatedType)) { //profile 이미지 일 경우
            handleProfileImages(imageFiles, relatedType, relatedNumber, responseDtos);

        } else if ("community".equals(relatedType)) { //community 이미지 일 경우
            handleCommunityImages(imageFiles, relatedType, relatedNumber, responseDtos);
        } else {
            throw new IllegalArgumentException("유효하지 않은 relatedType입니다: " + relatedType);
        }

        return responseDtos;
    }

    private ImageDetailResponseDto[] handleProfileImages(MultipartFile[] imageFiles, String relatedType, int relatedNumber, ImageDetailResponseDto[] responseDtos) throws IOException {

        //배열에서 이미 파일 꺼내기
        for (int i = 0; i < imageFiles.length; i++) {
            MultipartFile imageFile = imageFiles[i];

            // 데이터베이스에서 기존 이미지 조회
            Optional<Image> existingImageOpt = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber);

            // 이미 존재하는 이미지 S3에서 삭제 및 데이터베이스에서도 삭제
            existingImageOpt.ifPresent(existingImage -> {
                s3Uploader.deleteFile(existingImage.getPath());
                imageRepository.delete(existingImage);
            });

            // S3에 이미지 파일 업로드 후 폴더 path 받아오기
            String folderPath = s3Uploader.upload(imageFile, relatedType, relatedNumber, 0);

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
            Image uploadedImage = imageRepository.save(image);
            String url = s3Uploader.getImageUrl(folderPath);
            responseDtos[i] = new ImageDetailResponseDto(uploadedImage, url);
        }
        return responseDtos;
    }

    private ImageDetailResponseDto[] handleCommunityImages(MultipartFile[] imageFiles, String relatedType, int relatedNumber, ImageDetailResponseDto[] responseDtos) throws IOException {
        // 데이터베이스에서 기존 이미지 있는지 확인
        List<Image> existingImages = imageRepository.findAllByRelatedTypeAndRelatedNumber(relatedType, relatedNumber);

        // 이미 존재하는 이미지가 있을 경우 S3 및 및 DB에서 데이터 삭제
        existingImages.forEach(existingImage -> {
            s3Uploader.deleteFile(existingImage.getPath());
            imageRepository.delete(existingImage);
        });

        // 각 이미지 파일을 순회하며 처리
        for (int i = 0; i < imageFiles.length; i++) {
            MultipartFile imageFile = imageFiles[i];

            // S3에 이미지 파일 업로드 후 URL 받아오기
            String S3url = s3Uploader.upload(imageFile, relatedType, relatedNumber, i + 1);

            // 메타 데이터 뽑아서 create dto에 담기
            ImageCreateRequestDto imageCreateDto = ImageCreateRequestDto.builder()
                    .originalName(imageFile.getOriginalFilename())
                    .storageName(fileNameHandler.generateUniqueFileName(imageFile.getOriginalFilename()))
                    .size(imageFile.getSize())
                    .format(imageFile.getContentType())
                    .relatedType(relatedType)
                    .relatedNumber(relatedNumber)
                    .path(S3url)
                    .build();

            // DB에 저장
            Image image = imageCreateDto.toImageEntity(imageCreateDto.getOriginalName(),
                    imageCreateDto.getStorageName(),
                    imageCreateDto.getSize(),
                    imageCreateDto.getFormat(),
                    imageCreateDto.getRelatedType(),
                    imageCreateDto.getRelatedNumber(),
                    imageCreateDto.getPath());
            Image uploadedImage = imageRepository.save(image);
            String url = s3Uploader.getImageUrl(uploadedImage.getPath());
            responseDtos[i] = new ImageDetailResponseDto(uploadedImage, url);
        }
        return responseDtos; // 결과를 반환
    }

    // 이미지 정보
    public ImageDetailResponseDto getImageDetailByNumber(String relatedType, int relatedNumber) {
        Image image = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다." + relatedType + ":" + relatedNumber));

        String url = s3Uploader.getImageUrl(image.getPath());
        return new ImageDetailResponseDto(image, url);
    }

    // 이미지 삭제
    @Transactional
    public void deleteImage(String relatedType, int relatedNumber) {
        if ("profile".equals(relatedType)) {
            // 1대1
            Image image = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber)
                    .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다."));

            // S3에서 파일 삭제
            s3Uploader.deleteFile(image.getPath()); // 이미지의 경로를 사용하여 S3에서 삭제

            // 데이터베이스에서 이미지 삭제
            imageRepository.delete(image);

        } else if ("community".equals(relatedType)) {
            // 1 대 다
            List<Image> images = imageRepository.findAllByRelatedTypeAndRelatedNumber(relatedType, relatedNumber);

            for (Image image : images) {
                // S3에서 파일 삭제
                s3Uploader.deleteFile(image.getPath()); // 이미지의 경로를 사용하여 S3에서 삭제

                // 데이터베이스에서 이미지 삭제
                imageRepository.delete(image);
            }
        } else {
            throw new IllegalArgumentException("유효하지 않는 유형입니다: " + relatedType);
        }

    }
}
