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
    private List<String> tags;

    @QueryProjection
    public TravelDetailDto(Travel travel, int hostNumber, String hostName, List<String> tags) {
        this.travel = travel;
        this.hostNumber = hostNumber;
        this.hostName = hostName;
        this.tags = tags;
    }
    
}
