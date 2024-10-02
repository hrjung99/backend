package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.travel.dto.TravelRecommendDto;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;
import swyp.swyp6_team7.travel.util.TravelRecommendComparator;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelHomeService {

    private final TravelRepository travelRepository;
    private final MemberService memberService;

    public Page<TravelRecentDto> getTravelsSortedByCreatedAt(PageRequest pageRequest) {
        return travelRepository.findAllSortedByCreatedAt(pageRequest);
    }

    public Page<TravelRecommendDto> getRecommendTravelsByUser(PageRequest pageRequest, Principal principal) {

        List<String> preferredTags = memberService.findPreferredTagsByEmail(principal.getName());
        log.info("preferredTags: " + preferredTags);

        Page<TravelRecommendDto> result = travelRepository.findAllByPreferredTags(pageRequest, preferredTags);
        for (TravelRecommendDto dto : result.getContent()) {
            log.info(String.format("TravelRecommendDto = %d, %s", dto.getTravelNumber(), dto.getTags().toString()));
        }

        List<TravelRecommendDto> travels = new ArrayList<>(result.getContent());
        Collections.sort(travels, new TravelRecommendComparator());

        return new PageImpl(travels, pageRequest, result.getTotalElements());
    }
}
