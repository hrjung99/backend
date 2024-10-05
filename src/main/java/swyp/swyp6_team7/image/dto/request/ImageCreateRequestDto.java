package swyp.swyp6_team7.image.dto.request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.image.domain.Image;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageCreateRequestDto {
    private String originalName;
    private String storageName;
    private Long size;
    private String format;
    private String relatedType;
    private int relatedNumber;
    private String path;

    @Builder
    public ImageCreateRequestDto(String originalName, String storageName, Long size, String format, String relatedType, int relatedNumber, String path) {
        this.originalName = originalName;
        this.storageName = storageName;
        this.size = size;
        this.format = format;
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
        this.path = path;
    }

    // Image 엔티티로 변환
    public Image toImageEntity() {
        return Image.builder()
                .originalName(originalName)
                .storageName(storageName)
                .size(size)
                .format(format)
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .path(path)
                .uploadDate(LocalDateTime.now())  // 업로드 시간은 현재 시간으로 설정
                .build();
    }
}
