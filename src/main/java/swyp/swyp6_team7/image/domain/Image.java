package swyp.swyp6_team7.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Table(name = "Images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_number", updatable = false, nullable = false)
    private Long imageNumber;

    //파일에서 뽑아오는 데이터
    @Column(name = "image_original_name", nullable = false)
    private String originalName;    //실제 파일 이름

    @Column(name = "image_storage_name", nullable = false)
    private String storageName; //서버에 저장된 파일 이름

    @Column(name = "image_size", nullable = false)
    private Long size;  //파일의 크기

    @Column(name = "image_format", nullable = false)
    private String format;  //파일의 포맷


    //관리(구분)을 위한 테이터
    @Column(name = "image_related_type", nullable = false)
    private String relatedType; //어느 기능과 관련된 이미지 인지
    //"profile" 은 프로필 이미지, community 는 커뮤니티 게시물 이미지

    @Column(name = "image_related_number", nullable = false)
    private int relatedNumber;  //누구 혹은 어느 게시물의 이미지인지
    //프로필 이미지면 userNumber 입력, 커뮤니티 이미지면 게시물 번호 입력




    //**upload하고 가져오는 데이터
    //S3에 저장 된 이미지 경로
    @Column(name = "image_path", nullable = false)
    private String path;

    //이미지 업로드 일시
    @Column(name = "image_upload_date", nullable = false)
    private LocalDateTime uploadDate;

    //Create
    @Builder
    public Image(String originalName, String storageName, Long size, String format, String relatedType, int relatedNumber, String path, LocalDateTime uploadDate) {
        this.originalName = originalName;
        this.storageName = storageName;
        this.size = size;
        this.format = format;
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
        this.path = path;
        this.uploadDate = uploadDate;
    }

//    //Update (파일 이름 및 경로 수정)
//    public Image update(String originalName, String storageName, String path) {
//        this.originalName = originalName;
//        this.storageName = storageName;
//        this.path = path;
//        return this;
//    }

    //Delete (이미지 삭제)
    public void delete(Long imageNumber) {
        this.imageNumber = imageNumber;
    }

}