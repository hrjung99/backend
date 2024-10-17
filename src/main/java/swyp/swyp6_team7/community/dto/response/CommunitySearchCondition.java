package swyp.swyp6_team7.community.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import swyp.swyp6_team7.community.util.CommunitySearchSortingType;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommunitySearchCondition {

    private PageRequest pageRequest;
    private String keyword;
    private Integer categoryNumber;
    private CommunitySearchSortingType sortingType;

    @Builder
    public CommunitySearchCondition (
        PageRequest pageRequest,
        String keyword,
        Integer categoryNumber,
        String sortingType
    ) {
        this.pageRequest = pageRequest;
        this.keyword = keyword;
        this.categoryNumber = categoryNumber;
        this.sortingType = CommunitySearchSortingType.valueOf(sortingType);
    }
}
