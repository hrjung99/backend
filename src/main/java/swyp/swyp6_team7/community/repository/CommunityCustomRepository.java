package swyp.swyp6_team7.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swyp.swyp6_team7.community.dto.response.CommunitySearchCondition;
import swyp.swyp6_team7.community.dto.response.CommunitySearchDto;

public interface CommunityCustomRepository {

    void incrementViewCount(int postNumber);

    Page<CommunitySearchDto> search(PageRequest pageRequest, CommunitySearchCondition searchCondition);
}
