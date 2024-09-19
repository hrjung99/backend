package swyp.swyp6_team7.travel.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TravelSearchCondition {

    private String keyword;
    private PageRequest pageRequest;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    //장소 -> 국내, 해외
    //인원 -> 
    //기간 -> 일주일 이하, 1~4주, 한달 이상
    //정렬 -> 추천순, 최신순, 등록일순, 정확도순

}
