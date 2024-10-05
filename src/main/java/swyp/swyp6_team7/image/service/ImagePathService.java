package swyp.swyp6_team7.image.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImagePathService {

    private static final String BASE_FOLDER = "images/"; // application.yml에 설정된 값 가져올 수 있음


    //이미지 동적 경로 생성 메소드
    public String generateS3Path(String relatedType, int relatedNumber, String originalFileName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");

        String year = now.format(yearFormatter);
        String month = now.format(monthFormatter);
        String day = now.format(dayFormatter);

        // 경로 생성: {type}/{id}/{yyyy}/{mm}/{dd}/{file_name}
        return BASE_FOLDER + relatedType + "/" + relatedNumber + "/" + year + "/" + month + "/" + day + "/" + originalFileName;
    }
}
