package swyp.swyp6_team7.image.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.image.util.FileFolderHandler;
import swyp.swyp6_team7.image.util.FileNameHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class S3Uploader {

    private final AmazonS3 s3Client;
    private final S3Component s3Component;
    private final FileFolderHandler fileFolderHandler; // FileFolderHandler 추가
    private final FileNameHandler fileNameHandler; // FileNameHandler 추가

    @Autowired
    public S3Uploader(AmazonS3 s3Client, S3Component s3Component, FileFolderHandler fileFolderHandler, FileNameHandler fileNameHandler) {
        this.s3Client = s3Client;
        this.s3Component = s3Component;
        this.fileFolderHandler = fileFolderHandler; // FileFolderHandler 주입
        this.fileNameHandler = fileNameHandler; // FileNameHandler 주입
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

        try (InputStream inputStream = file.getInputStream()) {
            //S3에 파일 업로드
            s3Client.putObject(new PutObjectRequest(s3Component.getBucket(), folderPath, inputStream, metadata));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e); //업로드 실패 시 예외 처리
        }

        // 파일 경로 리턴
        return s3Client.getUrl(s3Component.getBucket(), folderPath).toString();
    }
}