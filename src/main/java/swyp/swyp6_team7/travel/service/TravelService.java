package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.repository.LocationRepository;
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

import java.time.LocalDateTime;
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
    private final LocationRepository locationRepository;

    @Transactional
    public Travel create(TravelCreateRequest request, String email) {

        Users user = memberService.findByEmail(email);
        Location location = locationRepository.findByLocationName(request.getLocationName())
                .orElseThrow(() -> new IllegalArgumentException("city not found: " + request.getLocationName()));

        Travel savedTravel = travelRepository.save(request.toTravelEntity(user.getUserNumber(), location));
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

        //bookmark 개수
        int bookmarkCount = bookmarkRepository.countByTravelNumber(travelNumber);

        TravelDetailResponse detailResponse = new TravelDetailResponse(travelDetail, enrollmentCount, bookmarkCount);

        //로그인 요청자 주최 여부, 신청 확인
        if (travelDetail.getHostNumber() == requestUserNumber) {
            detailResponse.setHostUserCheckTrue();
        } else {
            Enrollment enrollmented = enrollmentRepository
                    .findOneByUserNumberAndTravelNumber(requestUserNumber, travelNumber);
            //log.info("enrollmented = " + enrollmented);
            detailResponse.setEnrollmentNumber(enrollmented);
        }

        return detailResponse;
    }

    @Transactional
    public void update(int travelNumber, TravelUpdateRequest travelUpdate) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        authorizeTravelOwner(travel);

        Location location = locationRepository.findByLocationName(travelUpdate.getLocationName())
                .orElseThrow(() -> new IllegalArgumentException("city not found: " + travelUpdate.getLocationName()));

        Travel updatedTravel = travel.update(travelUpdate, location);
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
        Integer requestUserNumber = MemberAuthorizeUtil.getLoginUserNumber();
        Page<TravelSearchDto> result = travelRepository.search(condition, requestUserNumber);
        for (TravelSearchDto travelSearchDto : result) {
            log.info("service: " + travelSearchDto.toString());
        }
        return result;
    }


    public LocalDateTime getEnrollmentsLastViewedAt(int travelNumber) {
        return travelRepository.getEnrollmentsLastViewedAtByNumber(travelNumber);
    }

    @Transactional
    public void updateEnrollmentLastViewedAt(int travelNumber, LocalDateTime lastViewedAt) {
        travelRepository.updateEnrollmentsLastViewedAtByNumber(travelNumber, lastViewedAt);
    }
}
