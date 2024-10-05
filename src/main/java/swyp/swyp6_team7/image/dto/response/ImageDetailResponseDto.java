package swyp.swyp6_team7.image.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.image.domain.Image;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImageDetailResponseDto {

    private Long imageNumber;
    private String originalName;
    private Long size;

    private String relatedType;
    private int relatedNumber;

    private String path;
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시 MM분")
    private LocalDateTime uploadDate;

    @Builder
    public ImageDetailResponseDto(
            Long imageNumber, String originalName, Long size, String relatedType,
            Integer relatedNumber, String path, LocalDateTime uploadDate)
    {
        this.imageNumber = imageNumber;
        this.originalName = originalName;
        this.size = size;
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
        this.path = path;
        this.uploadDate = uploadDate;
    }

    public ImageDetailResponseDto(Image image) {
        this.imageNumber = image.getImageNumber();
        this.originalName = image.getOriginalName();
        this.size = image.getSize();
        this.relatedType = image.getRelatedType();
        this.relatedNumber = image.getRelatedNumber();
        this.path = image.getPath();
        this.uploadDate = image.getUploadDate();
    }

    @Override
    public String toString() {
        return "ImageDetailResponseDto{" +
                "imageNumber=" + imageNumber +
                ", originalName='" + originalName + '\'' +
                ", size=" + size +
                ", relatedType='" + relatedType + '\'' +
                ", relatedNumber=" + relatedNumber +
                ", path='" + path + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }

}
