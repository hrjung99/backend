package swyp.swyp6_team7.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import swyp.swyp6_team7.profile.entity.UserProfile;

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

    //실제 파일 이름
    @Column(name = "image_original_name", nullable = false)
    private String originalName;

    //서버에 저장된 파일 이름
    @Column(name = "image_storage_name", nullable = false)
    private String storageName;

    //파일의 크기
    @Column(name = "image_size", nullable = false)
    private Long size;

    //파일의 포맷
    @Column(name = "image_format", nullable = false)
    private Long format;


    //어느 기능과 관련된 이미지 인지
    //"profile" 은 프로필 이미지, community 는 커뮤니티 게시물 이미지
    @Column(name = "image_related_type", nullable = false)
    private String relatedType;

    //누구 혹은 어느 게시물의 이미지인지
    //프로필 이미지면 userNumber 입력, 커뮤니티 이미지면 게시물 번호 입력
    @Column(name = "image_related_number", nullable = false)
    private int relatedNumber;


    //S3에 저장 된 이미지 경로
    @Column(name = "image_path", nullable = false)
    private String path;

    //이미지 업로드 일시
    @Column(name = "image_upload_date", nullable = false)
    private LocalDateTime uploadDate;
}