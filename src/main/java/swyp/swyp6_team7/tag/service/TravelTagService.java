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
@Transactional(readOnly = true)
@Service
public class TravelTagService {
    public static final int TRAVEL_TAG_MAX_COUNT = 5;

    private final TravelTagRepository travelTagRepository;
    private final TagService tagService;


    @Transactional
    public List<Tag> create(Travel travel, List<String> tags) {

        List<Tag> checkedTags = tags.stream()
                .distinct()
                .limit(TRAVEL_TAG_MAX_COUNT)
                .map(tagName -> tagService.findByName(tagName))
                .toList();

        return checkedTags.stream()
                .map(tag -> travelTagRepository.save(TravelTag.of(travel, tag)).getTag())
                .toList();
    }


    public List<String> getTagsByTravelNumber(int travelNumber) {
        return travelTagRepository.findTagsByTravelNumber(travelNumber)
                .stream()
                .map(tag -> tag.getTag().getName())
                .toList();
    }


    @Transactional
    public List<String> update(Travel travel, List<String> newTags) {

        travelTagRepository.deleteByTravel(travel);

        newTags.stream()
                .distinct()
                .limit(TRAVEL_TAG_MAX_COUNT)
                .map(tagName -> tagService.findByName(tagName))
                .map(tag -> travelTagRepository.save(TravelTag.of(travel, tag)).getTag())
                .toList();

        return travelTagRepository.findTagsByTravelNumber(travel.getNumber())
                .stream()
                .map(tag -> tag.getTag().getName())
                .toList();
    }

}
