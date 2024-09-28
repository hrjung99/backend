package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelRepository;
import swyp.swyp6_team7.tag.domain.TravelTag;
import swyp.swyp6_team7.travel.domain.TravelStatus;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TravelListService {

    private final TravelRepository travelRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TravelListResponseDto> getTravelListByUser(Integer userNumber) {
        // 사용자 번호를 통해 여행 게시글 조회
        List<Travel> travels = travelRepository.findByUserNumber(userNumber);

        // 사용자의 이름을 미리 조회하여 맵으로 저장
        String username = userRepository.findByUserNumber(userNumber)
                .map(users -> users.getUserName())
                .orElse("Unknown");

        // 여행 엔티티를 DTO로 변환하여 반환
        return travels.stream().map(travel -> {
            String dDay = TravelListResponseDto.formatDDay(travel.getDueDate()); // 디데이 형식으로 마감기한 포맷팅
            String postedAgo = TravelListResponseDto.formatPostedAgo(travel.getCreatedAt().toLocalDate()); // 작성일로부터 경과한 시간 포맷팅

            // 동반자 수 계산
            int currentApplicants = travel.getCompanions().size();

            // 태그 리스트 추출
            List<String> tags = travel.getTravelTags().stream()
                    .map(travelTag -> travelTag.getTag().getName())
                    .collect(Collectors.toList());

            // 세부내용 조회, 수정, 삭제 URL 생성
            String detailUrl = "/api/travel/detail" + travel.getNumber();
            String updateUrl = "/api/travel/" + travel.getNumber();
            String deleteUrl = "/api/travel/" + travel.getNumber();

            return new TravelListResponseDto(
                    travel.getNumber(),
                    travel.getTitle(),
                    travel.getLocation(),
                    username,
                    dDay,
                    postedAgo,
                    currentApplicants,
                    travel.getMaxPerson(),
                    travel.getStatus() == TravelStatus.CLOSED,
                    tags,
                    detailUrl,
                    updateUrl,
                    deleteUrl
            );
        }).collect(Collectors.toList());
    }
}
