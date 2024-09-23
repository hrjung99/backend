package swyp.swyp6_team7.travel.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import swyp.swyp6_team7.travel.domain.GenderType;
import swyp.swyp6_team7.travel.domain.PeriodType;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSearchCondition {

    private PageRequest pageRequest;
    private String keyword;
    //private String locationFilter;
    private List<GenderType> genderFilter;
    private List<String> personRangeFilter;
    private List<PeriodType> periodFilter;
    private List<String> tags;

    //정렬 -> 추천순, 최신순, 등록일순, 정확도순

    @Builder
    public TravelSearchCondition(
            PageRequest pageRequest,
            String keyword,
//            List<String> locationTypes,
            List<String> genderTypes,
            List<String> personTypes,
            List<String> periodTypes,
            List<String> tags
    ) {
        this.pageRequest = pageRequest;
        this.keyword = keyword;
        //location
        this.genderFilter = getGenderFilter(genderTypes);
        this.personRangeFilter = personTypes;
        this.periodFilter = getPeriodFilter(periodTypes);
        this.tags = tags == null ? new ArrayList<>() : tags;
    }

    private List<GenderType> getGenderFilter(List<String> genderTypes) {
        if (genderTypes == null) {
            return new ArrayList<>();
        }
        return genderTypes.stream().distinct().map(GenderType::of).toList();
    }

    private List<PeriodType> getPeriodFilter(List<String> periodTypes) {
        if (periodTypes == null) {
            return new ArrayList<>();
        }
        return periodTypes.stream().distinct().map(PeriodType::of).toList();
    }

}
