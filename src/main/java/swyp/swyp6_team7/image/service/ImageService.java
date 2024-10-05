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

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final FileNameHandler fileNameHandler;

    @Transactional
    public Image uploadImage(MultipartFile imageFile, String relatedType, int relatedNumber) throws IOException {

        // S3에 이미지 파일 업로드 후 URL 경로 받아오기
        String imagePath = s3Uploader.upload(imageFile, relatedType, relatedNumber);

        //메타 데이터 뽑아서 create dto에 담기
        ImageCreateRequestDto imageCreateDto = ImageCreateRequestDto.builder()
                .originalName(imageFile.getOriginalFilename())
                .storageName(fileNameHandler.generateUniqueFileName(imageFile.getOriginalFilename()))
                .size(imageFile.getSize())
                .format(imageFile.getContentType())
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .path(imagePath)
                .build();

        //DB에 저장
        Image image = imageCreateDto.toImageEntity();
        return imageRepository.save(image);
    }


    public ImageDetailResponseDto getImageDetailByNumber(Long imageNumber) {
        Image image = imageRepository.findByImageNumber(imageNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다." + imageNumber));

        return new ImageDetailResponseDto(image);
    }










}
