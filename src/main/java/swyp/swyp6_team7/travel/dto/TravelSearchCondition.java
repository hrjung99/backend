package swyp.swyp6_team7.travel.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSearchCondition {

    private PageRequest pageRequest;
    private String keyword;
    private List<String> tags;

    //장소 -> 국내, 해외
    //인원 -> maxPerson
    //성별 -> GenderType
    //기간 -> PeriodType
    //정렬 -> 추천순, 최신순, 등록일순, 정확도순


    @Builder
    public TravelSearchCondition(PageRequest pageRequest, String keyword, List<String> tags) {
        this.pageRequest = pageRequest;
        this.keyword = keyword;
        this.tags = tags;
    }

}
