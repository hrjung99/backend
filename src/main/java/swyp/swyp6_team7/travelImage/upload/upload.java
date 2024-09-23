package swyp.swyp6_team7.travelImage.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class upload {

    private Map<String, String> upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        Map<String, String> param = new HashMap<>();
        String originFileName = uploadFile.getName().substring(uploadFile.getName().indexOf("_") +1);
        param.put("fileName", originFileName);
        param.put("uploadImageUrl", uploadImageUrl);
        return param;      // 업로드된 파일의 S3 URL 주소 반환
    }
}
