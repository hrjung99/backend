package swyp.swyp6_team7.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.tag.domain.Tag;
import swyp.swyp6_team7.tag.domain.TravelTag;
import swyp.swyp6_team7.tag.repository.TravelTagRepository;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TravelTagService {
    public static final int TRAVEL_TAG_MAX_COUNT = 5;

    private final TravelTagRepository travelTagRepository;
    private final TagService tagService;


    @Transactional
    public List<Tag> save(Travel travel, List<String> tags) {

        List<Tag> checkedTags = tags.stream()
                .distinct()
                .limit(TRAVEL_TAG_MAX_COUNT)
                .map(tag -> tagService.getByName(tag))
                .toList();

        return checkedTags.stream()
                .map(tag -> travelTagRepository.save(TravelTag.of(travel, tag)).getTag())
                .toList();
    }

}
