package swyp.swyp6_team7.travel.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSearchCondition {

    private String keyword;
    private PageRequest pageRequest;
    private List<String> tags;
    //태그 -> and, 완전 일치
    //장소 -> 국내, 해외
    //인원 -> 
    //기간 -> 일주일 이하, 1~4주, 한달 이상
    //정렬 -> 추천순, 최신순, 등록일순, 정확도순


    @Builder
    public TravelSearchCondition(
            String keyword,
            PageRequest pageRequest,
            List<String> tags
    ) {
        this.keyword = keyword;
        this.pageRequest = pageRequest;
        this.tags = tags;
    }

}
