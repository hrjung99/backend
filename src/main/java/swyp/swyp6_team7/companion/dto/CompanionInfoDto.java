package swyp.swyp6_team7.companion.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CompanionInfoDto {

    private Integer userNumber;
    private String userName;
    //TODO: UI에 따라 추후 수정


    @QueryProjection
    public CompanionInfoDto(Integer userNumber, String userName) {
        this.userNumber = userNumber;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "CompanionInfoDto{" +
                "userNumber=" + userNumber +
                ", userName='" + userName + '\'' +
                '}';
    }
}
