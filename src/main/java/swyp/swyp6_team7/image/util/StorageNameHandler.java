package swyp.swyp6_team7.image.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StorageNameHandler {

    public String generateUniqueFileName(String originalFileName) {
        //확장자 추출
        String extension = extractExtension(originalFileName);

        //이름 생성
        String uniqueFileName = generateUUID();

        //확장자 붙여서 리턴
        return uniqueFileName + extension;
    }


    // 파일 확장자 추출 메소드
    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return "";  // 확장자가 없는 경우 빈 문자열 반환
    }

    // UUID를 사용해 고유한 이름 생성 메소드
    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    //key로 storageName 추출
    public String extractStorageName(String s3Key) {
        // 마지막 '/' 이후의 문자열을 추출
        return s3Key.substring(s3Key.lastIndexOf('/') + 1);
    }
}
