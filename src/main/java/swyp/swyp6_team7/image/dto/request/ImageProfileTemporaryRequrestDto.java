package swyp.swyp6_team7.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// 프로필 이미지 정식 저장 시 사용
@Getter
@AllArgsConstructor
public class ImageProfileTemporaryRequrestDto {
    private List<String> deletedTempUrls;
    private String tempUrl;
}
