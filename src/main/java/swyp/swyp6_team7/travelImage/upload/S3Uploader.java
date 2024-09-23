package swyp.swyp6_team7.travelImage.upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public Map<String, String> upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    private Map<String, String> upload(File uploadFile, String dirName) throws IOException {
        String fileName = dirName + "/" + uploadFile.getName();  // S3에 저장될 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName);     // S3로 파일 업로드

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        Map<String, String> param = new HashMap<>();
        String originFileName = uploadFile.getName().substring(uploadFile.getName().indexOf("_") + 1);
        param.put("fileName", originFileName);
        param.put("uploadImageUrl", uploadImageUrl);
        return param;  // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .acl("public-read")  // PublicRead 권한 설정
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(uploadFile));

        return s3Client.utilities().getUrl(b -> b.bucket(bucket).key(fileName)).toExternalForm();
    }

    private void removeNewFile(File targetFile) {
        try {
            Files.delete(targetFile.toPath());
            log.info("파일이 삭제되었습니다.");
        } catch (IOException e) {
            log.error("파일 삭제에 실패했습니다.", e);
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        File convertFile = new File(uuid.toString() + "_" + file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
