package swyp.swyp6_team7.image.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.image.util.FileFolderHandler;
import swyp.swyp6_team7.image.util.FileNameHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final S3Component s3Component;
    private final FileFolderHandler fileFolderHandler; // FileFolderHandler 추가
    private final FileNameHandler fileNameHandler; // FileNameHandler 추가

    @Autowired
    public S3Uploader(AmazonS3 amazonS3, S3Component s3Component, FileFolderHandler fileFolderHandler, FileNameHandler fileNameHandler) {
        this.amazonS3 = amazonS3;
        this.s3Component = s3Component;
        this.fileFolderHandler = fileFolderHandler; // FileFolderHandler 주입
        this.fileNameHandler = fileNameHandler; // FileNameHandler 주입
    }

    //S3에 업로드 하는 메소드
    public String upload(MultipartFile file, String relatedType, int relatedNumber, int order) throws IOException {
        // 파일 메타데이터
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        //폴더 경로와 파일 이름 생성
        String storageName = fileNameHandler.generateUniqueFileName(file.getOriginalFilename()); // 고유한 파일 이름 생성
        String S3Path = fileFolderHandler.generateS3Path(relatedType, relatedNumber, storageName, order); // 경로 생성

        // 프로필 타입인 경우, 기존 파일 삭제
        if ("profile".equals(relatedType)) {
            // 기존 파일 경로 생성
            String existingFilePath = fileFolderHandler.generateS3Path(relatedType, relatedNumber, storageName,0);
            deleteFile(existingFilePath); // 기존 파일 삭제 메소드 호출

        } else if ("community".equals(relatedType)) { //커뮤니티 타입인 경우,

            S3Path = fileFolderHandler.generateS3Path(relatedType, relatedNumber, storageName, order); //순서에 따른 경로 생성
        } else {
            throw new IllegalArgumentException("지원하지 않는 relatedType입니다.: " + relatedType);
        }

        try (InputStream inputStream = file.getInputStream()) {
            //S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(s3Component.getBucket(), S3Path, inputStream, metadata));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e); //업로드 실패 시 예외 처리
        }

        // S3Path 리턴
        return S3Path;
    }

    // S3에서 파일 삭제 메소드
    public void deleteFile(String folderPath) {
        if (amazonS3.doesObjectExist(s3Component.getBucket(), folderPath)) {
            try {
                amazonS3.deleteObject(s3Component.getBucket(), folderPath);
            } catch (Exception e) {
                // 로그 추가
                System.err.println("S3 파일 삭제 실패: " + e.getMessage());
            }
        } else {
            // 로그 추가
            System.out.println("삭제할 파일이 존재하지 않습니다: " + folderPath);
        }
    }

    // 이미지 URL 추출
    public String getImageUrl(String folderPath) {
        // S3에서 해당 경로에 이미지가 존재하는지 확인
        if (amazonS3.doesObjectExist(s3Component.getBucket(), folderPath)) {
            // 이미지가 존재하면 해당 URL 반환
            return amazonS3.getUrl(s3Component.getBucket(), folderPath).toString();
        } else {
            // 이미지가 없을 경우 빈 문자열 반환
            return "";
        }
    }

    // S3에서 해당 경로에 있는 파일 중 첫 번째 파일의 URL을 가져오는 메소드
    public String getSingleFileUrlFromS3(String folderPath) {
        ObjectListing objectListing = amazonS3.listObjects(s3Component.getBucket(), folderPath);
        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        if (!s3ObjectSummaries.isEmpty()) {
            String filePath = s3ObjectSummaries.get(0).getKey(); // 첫 번째 파일의 Key
            return getImageUrl(filePath); // 파일의 URL 반환
        }

        return ""; // 파일이 없으면 null 반환
    }


}