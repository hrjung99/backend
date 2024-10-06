package swyp.swyp6_team7.image.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swyp.swyp6_team7.image.domain.Image;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ImageCreateRequestDto {

    private String originalName; // 원본 파일 이름
    private String storageName; // 저장된 파일 이름 (고유 이름)
    private long size; // 파일 크기
    private String format; // 파일 포맷 (MIME 타입)
    private String relatedType; // 관련 타입 (프로필, 게시물 등)
    private int relatedNumber; // 관련 번호 (userNumber, postNumber 등)
    private String path; // S3에 저장된 파일 경로
    private LocalDateTime uploadDate;

    @Builder
    public ImageCreateRequestDto(String originalName, String storageName, long size, String format,
                                 String relatedType, int relatedNumber, String path) {
        this.originalName = originalName;
        this.storageName = storageName;
        this.size = size;
        this.format = format;
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
        this.path = path;
    }

    public Image toImageEntity(String originalName, String storageName, long size, String format,
                               String relatedType, int relatedNumber, String path) {
        return Image.builder()
                .originalName(originalName)
                .storageName(storageName)
                .size(size)
                .format(format)
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .path(path)
                .uploadDate(LocalDateTime.now()) // uploadDate는 현재 시간으로 설정
                .build();
    }
}
