package swyp.swyp6_team7.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.image.domain.Image;
import swyp.swyp6_team7.image.dto.request.ImageCreateRequestDto;
import swyp.swyp6_team7.image.dto.request.ImageUpdateRequestDto;
import swyp.swyp6_team7.image.dto.response.ImageDetailResponseDto;
import swyp.swyp6_team7.image.repository.ImageRepository;
import swyp.swyp6_team7.image.s3.S3Uploader;
import swyp.swyp6_team7.image.util.StorageNameHandler;


import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageProfileService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final ImageService imageService;
    private final StorageNameHandler storageNameHandler;


    //프로필 처음 생성 시 이미지 처리
    @Transactional
    public ImageDetailResponseDto initializeDefaultProfileImage(int relatedNumber) {
        ImageDetailResponseDto response = uploadDefaultImage(relatedNumber, 1);

        return response;
    }

    //프로필 이미지 파일로 업로드
    @Transactional
    public ImageDetailResponseDto uploadProfileImage(int relatedNumber, MultipartFile file) throws IOException {


        int order = 0;
        String relatedType = "profile";

        //기존 이미지가 있는지 확인
        Optional<Image> image = imageRepository.findByRelatedTypeAndRelatedNumberAndOrder(relatedType, relatedNumber, order);

        if(image.isPresent()) {
            //기존 이미지 S3에서 삭제
            s3Uploader.deleteFile(image.get().getKey());
        }
        
        //이미지 업로드
        String key = s3Uploader.upload(file, relatedType, relatedNumber, order);
        String savedImageUrl = s3Uploader.getImageUrl(key);

        //DB 수정
        // 최소 필드를 사용하여 DTO 생성
        // 메타 데이터 뽑아서 update dto에 담기
        ImageUpdateRequestDto updateRequest = ImageUpdateRequestDto.builder()
                .originalName(file.getOriginalFilename())
                .storageName(storageNameHandler.generateUniqueFileName(file.getOriginalFilename()))
                .size(file.getSize())
                .format(file.getContentType())
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .order(order)
                .key(key)
                .url(savedImageUrl)
                .build();

        return imageService.updateDB(relatedType, relatedNumber, 0, updateRequest);
    }

    //프로필 이미지 기본 이미지로 등록/수정
    @Transactional
    public ImageDetailResponseDto uploadDefaultImage(int relatedNumber, int defaultProfileImageNumber) {
        String relatedType = "profile";
        int order = 0;
        String url = "";

        switch (defaultProfileImageNumber) {

            case 1:
                url = "https://moing-hosted-contents.s3.ap-northeast-2.amazonaws.com/images/profile/default/defaultProfile.png";
                break;
            case 2:
                url = "https://moing-hosted-contents.s3.ap-northeast-2.amazonaws.com/images/profile/default/defaultProfile2.png";
                break;
            case 3:
                url = "https://moing-hosted-contents.s3.ap-northeast-2.amazonaws.com/images/profile/default/defaultProfile3.png";
                break;
            case 4:
                url = "https://moing-hosted-contents.s3.ap-northeast-2.amazonaws.com/images/profile/default/defaultProfile4.png";

                break;
            case 5:
                url = "https://moing-hosted-contents.s3.ap-northeast-2.amazonaws.com/images/profile/default/defaultProfile5.png";

                break;
            case 6:
                url = "https://moing-hosted-contents.s3.ap-northeast-2.amazonaws.com/images/profile/default/defaultProfile6.png";
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 default image number 입니다: " + defaultProfileImageNumber);
        }


        ImageDetailResponseDto response = updateProfileByDefaultUrl(relatedType, relatedNumber, url);
        return response;
    }


    //기존 이미지를 지우고 url로 이미지 수정 (프로필)
    @Transactional
    public ImageDetailResponseDto updateProfileByDefaultUrl(String relatedType, int relatedNumber, String url) {
        //DB에서 이미지 찾기
        Optional<Image> searchImage = imageRepository.findByRelatedTypeAndRelatedNumberAndOrder(relatedType, relatedNumber, 0);

        //관련 이미지 행이 조회 됐을 때
        if (searchImage.isPresent()) {

            // original name이 null이 아니라면(= 기존 이미지가 파일로 업로드 한 사진)

            if (searchImage.get().getOriginalName() != null) {
                // S3에서 기존 파일 삭제
                s3Uploader.deleteFile(searchImage.get().getKey());

                //DB 수정
                // 최소 필드를 사용하여 DTO 생성
                ImageUpdateRequestDto updateRequest = ImageUpdateRequestDto.builder()
                        .relatedType(relatedType)
                        .relatedNumber(relatedNumber)
                        .order(0)
                        .key(null)
                        .url(url) // 새 이미지 URL
                        .build();

                return imageService.updateDB(relatedType, relatedNumber, 0, updateRequest);

                // original name이 null이 라면 (=기존 이미지를 url로 저장)
            } else {

                ImageUpdateRequestDto updateRequest = ImageUpdateRequestDto.builder()
                        .relatedType(relatedType)
                        .relatedNumber(relatedNumber)
                        .order(0)
                        .key(null)
                        .url(url) // 새 이미지 URL
                        .build();

                return imageService.updateDB(relatedType, relatedNumber, 0, updateRequest);

            }

            //관련 이미지 행이 조회되지 않았을 경우 (= 프로필 이미지 행이 insert 되기 전)
        } else {
            //DB insert 동작
            ImageCreateRequestDto imageCreateDto = ImageCreateRequestDto.builder()
                    .relatedType(relatedType)
                    .relatedNumber(relatedNumber)
                    .order(0)
                    .url(url)
                    .build();

            // DB에 저장
            Image image = imageCreateDto.toImageEntity();

            Image uploadedImage = imageRepository.save(image);
            return new ImageDetailResponseDto(uploadedImage);

        }
    }

    //임시저장
//    public String temporaryImage (MultipartFile file, int userNumber) throws IOException {
//        String relatedType = "profile";
//
//        String temKey = S3Uploader.uploadInTemporary(file, relatedType);
//
//    }

}
