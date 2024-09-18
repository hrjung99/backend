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
import swyp.swyp6_team7.tag.domain.Tag;
import swyp.swyp6_team7.tag.domain.TravelTag;
import swyp.swyp6_team7.tag.repository.TagRepository;
import swyp.swyp6_team7.tag.repository.TravelTagRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.TravelRecentResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import(DataConfig.class)
@DataJpaTest
class TravelCustomRepositoryImplTest {

    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private TravelTagRepository travelTagRepository;
    @Autowired
    private TagRepository tagRepository;

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

    @DisplayName("findAll: 여행 콘텐츠를 DTO로 만들어 최신순으로 정렬해 반환한다")
    @Test
    public void findAllSortedByCreatedAt() {
        // given
        Travel travel = Travel.builder()
                .title("추가 테스트 데이터")
                .userNumber(1)
                .viewCount(0)
                .createdAt(LocalDateTime.now().plusDays(1))
                .status(TravelStatus.IN_PROGRESS)
                .build();
        travelRepository.save(travel);
        for (int i = 0; i < 5; i++) {
            Tag tag = tagRepository.save(Tag.of("태그" + i));
            travelTagRepository.save(TravelTag.of(travel, tag));
        }

        // when
        List<TravelRecentResponse> results = travelRepository.findAllSortedByCreatedAt();

        // then
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0).getTitle()).isEqualTo("추가 테스트 데이터");
        assertThat(results.get(0).getTags().size()).isEqualTo(3);
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

//    @DisplayName("search: 주어지는 태그가 달린 콘텐츠를 가져온다")
//    @Test
//    public void searchWithTags() {
//        // given
//        Tag tag = tagRepository.save(Tag.of("테스트"));
//        for (int i = 0; i < 6; i++) {
//            Travel travel = travelRepository.save(Travel.builder()
//                    .title("추가 테스트 데이터" + i)
//                    .userNumber(1)
//                    .viewCount(0)
//                    .createdAt(LocalDateTime.now())
//                    .status(TravelStatus.IN_PROGRESS)
//                    .build());
//            travelTagRepository.save(TravelTag.of(travel, tag));
//        }
//
//        List<String> tags = new ArrayList<>();
//        tags.add("테스트");
//        TravelSearchCondition condition = TravelSearchCondition.builder()
//                .pageRequest(PageRequest.of(0, 5))
//                .tags(tags)
//                .build();
//
//        // when
//        Page<Travel> result = travelRepository.search(condition);
//
//        // then
//        Page<TravelSearchDto> temp = travelRepository.search(condition)
//                .map(TravelSearchDto::from);
//        for (TravelSearchDto travelSearchDto : temp) {
//            System.out.println(travelSearchDto.toString());
//        }
//        assertThat(result.getTotalElements()).isEqualTo(6);
//    }
//
//    @DisplayName("search: 여러 개의 태그가 주어졌을 때, 모든 태그를 가진 콘텐츠만 가져온다")
//    @Test
//    public void searchWithMultipleTags() {
//        // given
//        Tag tag1 = tagRepository.save(Tag.of("한국"));
//        Tag tag2 = tagRepository.save(Tag.of("투어"));
//
//        Travel travel1 = travelRepository.save(Travel.builder()
//                .title("추가 테스트 데이터1")
//                .userNumber(1)
//                .viewCount(0)
//                .createdAt(LocalDateTime.now())
//                .status(TravelStatus.IN_PROGRESS)
//                .build());
//        travelTagRepository.save(TravelTag.of(travel1, tag1));
//        travelTagRepository.save(TravelTag.of(travel1, tag2));
//
//        Travel travel2 = travelRepository.save(Travel.builder()
//                .title("추가 테스트 데이터2")
//                .userNumber(1)
//                .viewCount(0)
//                .createdAt(LocalDateTime.now())
//                .status(TravelStatus.IN_PROGRESS)
//                .build());
//        travelTagRepository.save(TravelTag.of(travel2, tag1));
//
//        List<String> tags = new ArrayList<>();
//        tags.add("한국");
//        tags.add("투어");
//        TravelSearchCondition condition = TravelSearchCondition.builder()
//                .pageRequest(PageRequest.of(0, 5))
//                .tags(tags)
//                .build();
//
//        // when
//        Page<Travel> result = travelRepository.search(condition);
//
//        // then
//        assertThat(result.getTotalElements()).isEqualTo(1);
//    }

}