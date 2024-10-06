package swyp.swyp6_team7.image.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.image.domain.Image;
import swyp.swyp6_team7.image.repository.ImageRepository;
import swyp.swyp6_team7.image.util.FileFolderHandler;
import swyp.swyp6_team7.image.util.FileNameHandler;

import java.io.IOException;
import java.io.InputStream;

@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final S3Component s3Component;
    private final FileFolderHandler fileFolderHandler; // FileFolderHandler 추가
    private final FileNameHandler fileNameHandler; // FileNameHandler 추가
    private final ImageRepository imageRepository;

    @Autowired
    public S3Uploader(AmazonS3 amazonS3, S3Component s3Component, FileFolderHandler fileFolderHandler, FileNameHandler fileNameHandler, ImageRepository imageRepository) {
        this.amazonS3 = amazonS3;
        this.s3Component = s3Component;
        this.fileFolderHandler = fileFolderHandler; // FileFolderHandler 주입
        this.fileNameHandler = fileNameHandler; // FileNameHandler 주입
        this.imageRepository = imageRepository;
    }

    //S3에 업로드 하는 메소드
    public String upload(MultipartFile file, String relatedType, int relatedNumber) throws IOException {
        // 파일 메타데이터
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        //폴더 경로와 파일 이름 생성
        String storageName = fileNameHandler.generateUniqueFileName(file.getOriginalFilename()); // 고유한 파일 이름 생성
        String folderPath = fileFolderHandler.generateS3Path(relatedType, relatedNumber, storageName); // 경로 생성

        // 프로필 타입인 경우, 기존 파일 삭제
        if ("profile".equals(relatedType)) {
            // 기존 파일 경로 생성
            String existingFilePath = fileFolderHandler.generateS3Path(relatedType, relatedNumber, storageName);
            deleteFile(existingFilePath); // 기존 파일 삭제 메소드 호출
        }

        try (InputStream inputStream = file.getInputStream()) {
            //S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(s3Component.getBucket(), folderPath, inputStream, metadata));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e); //업로드 실패 시 예외 처리
        }

        // 파일 URL 리턴
        return folderPath;
    }

    // S3에서 파일 삭제 메소드
    public void deleteFile(String filePath) {
        if (amazonS3.doesObjectExist(s3Component.getBucket(), filePath)) {
            try {
                amazonS3.deleteObject(s3Component.getBucket(), filePath);
            } catch (Exception e) {
                // 로그 추가
                System.err.println("S3 파일 삭제 실패: " + e.getMessage());
            }
        } else {
            // 로그 추가
            System.out.println("삭제할 파일이 존재하지 않습니다: " + filePath);
        }
    }

    // 이미지 url 추출
    public String getImageUrl(String relatedType, int relatedNumber) {
        // Image 엔티티를 찾기
        Image image = imageRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다."));

        // 파일 경로를 통해 URL 생성
        return amazonS3.getUrl(s3Component.getBucket(), image.getPath()).toString();
    }
}