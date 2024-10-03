package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.member.util.MemberAuthorizeUtil;
import swyp.swyp6_team7.tag.service.TravelTagService;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.TravelDetailDto;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.dto.request.TravelUpdateRequest;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.dto.response.TravelSearchDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelService {

    private final TravelRepository travelRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TravelTagService travelTagService;
    private final MemberService memberService;

    @Transactional
    public Travel create(TravelCreateRequest request, String email) {

        Users user = memberService.findByEmail(email);

        Travel savedTravel = travelRepository.save(request.toTravelEntity(user.getUserNumber()));
        List<String> tags = travelTagService.create(savedTravel, request.getTags()).stream()
                .map(tag -> tag.getName())
                .toList();

        return savedTravel;
    }

    public TravelDetailResponse getDetailsByNumber(int travelNumber) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        if (travel.getStatus() == TravelStatus.DRAFT) {
            authorizeTravelOwner(travel);
        } else if (travel.getStatus() == TravelStatus.DELETED) {
            throw new IllegalArgumentException("Deleted Travel.");
        }

        Integer requestUserNumber = MemberAuthorizeUtil.getLoginUserNumber();
        TravelDetailDto travelDetail = travelRepository.getDetailsByNumber(travelNumber, requestUserNumber);
        //enrollment 개수
        int enrollmentCount = enrollmentRepository.countByTravelNumber(travelNumber);
        log.info("enrollmentCount: " + enrollmentCount);
        //bookmark 개수
        int bookmarkCount = bookmarkRepository.countByTravelNumber(travelNumber);
        TravelDetailResponse detailResponse = new TravelDetailResponse(travelDetail, enrollmentCount, bookmarkCount);

//        String requestUserName = SecurityContextHolder.getContext().getAuthentication().getName();
//        int requestUserNumber = memberService.findByEmail(requestUserName).getUserNumber();
        if (travelDetail.getHostNumber() == requestUserNumber) {
            detailResponse.setHostUserCheckTrue();
        } else {
            boolean existEnrollment = enrollmentRepository
                    .existsByUserNumberAndTravelNumber(requestUserNumber, travelNumber);
            detailResponse.setEnrollAvailable(existEnrollment);
        }

        return detailResponse;
    }

    @Transactional
    public void update(int travelNumber, TravelUpdateRequest travelUpdate) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        authorizeTravelOwner(travel);

        Travel updatedTravel = travel.update(travelUpdate);
        List<String> updatedTags = travelTagService.update(updatedTravel, travelUpdate.getTags());
    }

    @Transactional
    public void delete(int travelNumber) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        authorizeTravelOwner(travel);
        travel.delete();
    }

    private void authorizeTravelOwner(Travel travel) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        int userNumber = memberService.findByEmail(userName).getUserNumber();
        if (travel.getUserNumber() != userNumber) {
            throw new IllegalArgumentException("Forbidden Travel");
        }
    }


    public Page<TravelSearchDto> search(TravelSearchCondition condition) {
        Page<TravelSearchDto> result = travelRepository.search(condition);
        for (TravelSearchDto travelSearchDto : result) {
            log.info("service: " + travelSearchDto.toString());
        }
        return result;
    }
}
