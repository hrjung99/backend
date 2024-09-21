package swyp.swyp6_team7.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import swyp.swyp6_team7.tag.domain.TravelTag;
import swyp.swyp6_team7.travel.dto.request.TravelUpdateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "travels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_number", updatable = false)
    private int number;

    //작성자 식별자
    @Column(name = "user_number", nullable = false)
    private int userNumber;

    //작성 일시
    @CreatedDate
    @Column(name = "travel_reg_date", nullable = false)
    private LocalDateTime createdAt;

    //여행지
    @Column(name = "travel_location", length = 20)
    private String location;

    //제목
    @Column(name = "travel_title", length = 20)
    private String title;

    //상세 설명
    @Lob
    @Column(name = "travel_details", length = 2000)
    private String details;

    //조회수
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    //최대 모집 인원
    @Column(name = "travel_max_person")
    private int maxPerson;

    //모집 성별 카테고리
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_gender", nullable = false, length = 20)
    private GenderType genderType;

    //모집 종료 일시
    @Column(name = "travel_due_date")
    private LocalDate dueDate;

    //여행 기간 카테고리
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_period", nullable = false, length = 20)
    private PeriodType periodType;

    //콘텐츠 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_status", nullable = false, length = 20)
    private TravelStatus status;

    @OneToMany(mappedBy = "travel")
    private List<TravelTag> travelTags = new ArrayList<>();

    @Builder
    public Travel(
            int number, int userNumber, LocalDateTime createdAt,
            String location, String title, String details, int viewCount,
            int maxPerson, GenderType genderType, LocalDate dueDate,
            PeriodType periodType, TravelStatus status
    ) {
        this.number = number;
        this.userNumber = userNumber;
        this.createdAt = createdAt;
        this.location = location;
        this.title = title;
        this.details = details;
        this.viewCount = viewCount;
        this.maxPerson = maxPerson;
        this.genderType = genderType;
        this.dueDate = dueDate;
        this.periodType = periodType;
        this.status = status;
    }

    public Travel update(TravelUpdateRequest travelUpdate) {
        this.location = travelUpdate.getLocation();
        this.title = travelUpdate.getTitle();
        this.details = travelUpdate.getDetails();
        this.maxPerson = travelUpdate.getMaxPerson();
        this.genderType = GenderType.of(travelUpdate.getGenderType());
        this.dueDate = travelUpdate.getDueDate();
        this.periodType = PeriodType.of(travelUpdate.getPeriodType());
        this.status = TravelStatus.convertCompletionToStatus(travelUpdate.getCompletionStatus());
        return this;
    }

    public void delete() {
        this.status = TravelStatus.DELETED;
    }

    @Override
    public String toString() {
        return "Travel{" +
                "number=" + number +
                ", userNumber=" + userNumber +
                ", createdAt=" + createdAt +
                ", location='" + location + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", viewCount=" + viewCount +
                ", maxPerson=" + maxPerson +
                ", genderType=" + genderType +
                ", dueDate=" + dueDate +
                ", periodType=" + periodType +
                ", status=" + status +
                '}';
    }
}
