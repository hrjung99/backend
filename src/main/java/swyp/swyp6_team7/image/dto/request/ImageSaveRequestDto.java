package swyp.swyp6_team7.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 커뮤니티 이미지 정식 저장 시 사용
@Getter
@AllArgsConstructor
public class ImageSaveRequestDto {

    private List<String> deletedTempUrls;
    private List<String> tempUrls;

}
