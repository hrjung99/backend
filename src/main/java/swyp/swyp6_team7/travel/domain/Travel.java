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

    @Column(name = "user_number", nullable = false)
    private int userNumber;

    @Column(name = "travel_title", length = 20)
    private String title;

    @Column(name = "travel_summary", length = 30)
    private String summary;

    @Lob
    @Column(name = "travel_details", length = 2000)
    private String details;

    //조회수
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    //작성 일시
    @CreatedDate
    @Column(name = "travel_reg_date", nullable = false)
    private LocalDateTime createdAt;

    //여행 시작 날짜
    @Column(name = "travel_start_at")
    private LocalDate startAt;

    //여행 종료 날짜
    @Column(name = "travel_end_at")
    private LocalDate endAt;

    //모집 종료 일시(날짜+시간)
    @Column(name = "travel_due_datetime")
    private LocalDateTime dueDateTime;

    //여행지
    @Column(name = "travel_location", length = 20)
    private String location;

    //모집 최소 인원
    @Column(name = "travel_min_person")
    private int minPerson;

    //모집 최대 인원
    @Column(name = "travel_max_person")
    private int maxPerson;

    //예산
    @Column(name = "travel_budget")
    private int budget;

    //TODO: StatusConverter 사용 고민
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_status", nullable = false)
    private TravelStatus status;

    @OneToMany(mappedBy = "travel")
    private List<TravelTag> travelTags = new ArrayList<>();

    @Builder
    public Travel(
            int number, int userNumber, String title, String summary, String details, int viewCount,
            LocalDateTime createdAt, LocalDate startAt, LocalDate endAt, LocalDateTime dueDateTime,
            String location, int minPerson, int maxPerson, int budget, TravelStatus status
    ) {
        this.number = number;
        this.userNumber = userNumber;
        this.title = title;
        this.summary = summary;
        this.details = details;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.dueDateTime = dueDateTime;
        this.location = location;
        this.minPerson = minPerson;
        this.maxPerson = maxPerson;
        this.budget = budget;
        this.status = status;
    }

    public Travel update(TravelUpdateRequest travelUpdate) {
        this.title = travelUpdate.getTitle();
        this.summary = travelUpdate.getSummary();
        this.details = travelUpdate.getDetails();
        this.startAt = travelUpdate.getTravelStartAt();
        this.endAt = travelUpdate.getTravelEndAt();
        this.dueDateTime = travelUpdate.getDueDateTime();
        this.location = travelUpdate.getLocation();
        this.minPerson = travelUpdate.getMinPerson();
        this.maxPerson = travelUpdate.getMaxPerson();
        this.budget = travelUpdate.getBudget();
        this.status = TravelStatus.convertCompletionToStatus(travelUpdate.getCompletionStatus());
        return this;
    }

    public void delete() {
        this.status = TravelStatus.DELETED;
    }
}
