package swyp.swyp6_team7.travel.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSearchCondition {

    private String keyword;
    private PageRequest pageRequest;
    //페이징 -> page, size
    //태그 -> and, 완전 일치
    //필터 -> 장소, 인원, 기간, 스타일
    //정렬 -> 최신순, 좋아요개수


    @Builder
    public TravelSearchCondition(
            String keyword,
            PageRequest pageRequest
    ) {
        this.keyword = keyword;
        this.pageRequest = pageRequest;
    }

}