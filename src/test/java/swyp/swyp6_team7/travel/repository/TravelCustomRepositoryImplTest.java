package swyp.swyp6_team7.travel.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import swyp.swyp6_team7.config.DataConfig;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;

import java.time.LocalDateTime;
import java.util.List;

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
                .title("테스트 데이터")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.IN_PROGRESS)
                .build();
        travelRepository.save(travel);

        // when
        List<Travel> results = travelRepository.search("테스트");

        // then
        assertThat(results.size()).isEqualTo(2);
    }

    @DisplayName("search: 콘텐츠의 상태가 DELETED, DRAFT일 경우 제외하고 가져온다")
    @Test
    public void searchOnlyActivated() {
        // given
        Travel deletedTravel = Travel.builder()
                .title("테스트 데이터1 - deleted")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.DELETED)
                .build();
        travelRepository.save(deletedTravel);
        Travel draftTravel = Travel.builder()
                .title("테스트 데이터2 - draft")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .status(TravelStatus.DELETED)
                .build();
        travelRepository.save(draftTravel);

        // when
        List<Travel> results = travelRepository.search("테스트");

        // then
        assertThat(results.size()).isEqualTo(1);
    }

}