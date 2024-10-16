package swyp.swyp6_team7.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import swyp.swyp6_team7.community.domain.Community;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommunitySearchDto {

    private Community community;         //community 데이터
    private String userName;      //게시글 작성자명
    private String categoryName;    //카테고리명
    private long likeCount; //좋아요 수



    @QueryProjection
    public CommunitySearchDto(Community community, String userName, String categoryName, long likeCount) {

        this.community = community;
        this.userName = userName;
        this.categoryName = categoryName;
        this.likeCount = likeCount;


    }
}
