package swyp.swyp6_team7.travel.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swyp.swyp6_team7.config.DataConfig;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@Import(DataConfig.class)
@DataJpaTest
class TravelCustomRepositoryImplTest {

    @Autowired
    private TravelRepository travelRepository;

    @BeforeEach
    void setUp() {
        travelRepository.save(Travel.builder()
                .title("기본 테스트 데이터")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.IN_PROGRESS)
                .build());
    }

    @DisplayName("search: 제목에 keyword가 포함된 데이터를 찾을 수 있다")
    @Test
    public void searchWithKeyword() {
        // given
        Travel travel = Travel.builder()
                .title("추가 테스트 데이터")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.IN_PROGRESS)
                .build();
        travelRepository.save(travel);

        TravelSearchCondition condition = TravelSearchCondition.builder()
                .keyword("추가")
                .pageRequest(PageRequest.of(0, 5))
                .build();

        // when
        Page<Travel> results = travelRepository.search(condition);

        // then
        assertThat(results.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("search: 키워드가 주어지지 않을 경우 가능한 모든 콘텐츠를 전달")
    @Test
    public void searchWithoutKeyword() {
        // given
        Travel travel = Travel.builder()
                .title("추가 테스트 데이터")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.IN_PROGRESS)
                .build();
        travelRepository.save(travel);

        TravelSearchCondition condition = TravelSearchCondition.builder()
                .keyword("")
                .pageRequest(PageRequest.of(0, 5))
                .build();

        // when
        Page<Travel> results = travelRepository.search(condition);

        // then
        assertThat(results.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("search: 콘텐츠의 상태가 DELETED, DRAFT일 경우 제외하고 가져온다")
    @Test
    public void searchOnlyActivated() {
        // given
        Travel deletedTravel = Travel.builder()
                .title("추가 테스트 데이터1")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.DELETED)
                .build();
        travelRepository.save(deletedTravel);
        Travel draftTravel = Travel.builder()
                .title("추가 테스트 데이터2")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.DELETED)
                .build();
        travelRepository.save(draftTravel);

        TravelSearchCondition condition = TravelSearchCondition.builder()
                .keyword("데이터")
                .pageRequest(PageRequest.of(0, 5))
                .build();

        // when
        Page<Travel> results = travelRepository.search(condition);

        // then
        assertThat(results.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("search: 페이징을 이용해 콘텐츠를 가져올 수 있다")
    @Test
    public void searchWithPaging() {
        // given
        for (int i = 0; i < 6; i++) {
            travelRepository.save(Travel.builder()
                    .title("추가 테스트 데이터")
                    .userNumber(1)
                    .viewCount(0)
                    .createdAt(LocalDateTime.now())
                    .status(TravelStatus.IN_PROGRESS)
                    .build());
        }

        TravelSearchCondition condition = TravelSearchCondition.builder()
                .keyword("추가")
                .pageRequest(PageRequest.of(0, 5))
                .build();

        // when
        Page<Travel> result = travelRepository.search(condition);

        // then
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(6);
    }

}