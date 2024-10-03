package swyp.swyp6_team7.travel.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.List;

@Getter
public class TravelDetailDto {

    private Travel travel;
    private int hostNumber;
    private String hostName;
    private int companionCount;
    private List<String> tags;
    private boolean bookmarked;

    @QueryProjection
    public TravelDetailDto(
            Travel travel, int hostNumber, String hostName,
            int companionCount, List<String> tags, boolean isBookmarked
    ) {
        this.travel = travel;
        this.hostNumber = hostNumber;
        this.hostName = hostName;
        this.companionCount = companionCount;
        this.tags = tags;
        this.bookmarked = isBookmarked;
    }

}
