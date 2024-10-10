//package swyp.swyp6_team7.community.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import swyp.swyp6_team7.community.domain.Community;
//import swyp.swyp6_team7.community.dto.request.CommunityCreateRequestDto;
//import swyp.swyp6_team7.community.dto.response.CommunityDetailResponseDto;
//import swyp.swyp6_team7.community.repository.CommunityRepository;
//
//import java.time.LocalDateTime;
//
//@Slf4j
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//@Service
//public class CommunityService {
//    private final CommunityRepository communityRepository;
//
//    //Create
//    @Transactional
//    public Community create(CommunityCreateRequestDto request, int userNumber) {
//
//        //게시물 등록
//        Community savedPost = communityRepository.save(request.toCommunityEntity(
//                userNumber,
//                request.getCategoryNumber(),
//                request.getTitle(),
//                request.getContent(),
//                LocalDateTime.now(), // 등록 일시
//                0 // 조회수
//        ));
//        return savedPost;
//    }
//
//    //Detail Read
//    public CommunityDetailResponseDto getDetailsByPostNumber(int postnumber, int userNumber) {
//        //userNubmer : 조회 요청자의 회원 번호
//
//        //존재하는 게시글인지 확인
//        Community community = communityRepository.findByPostNumber(postnumber)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다." + postnumber));
//
//        // 댓글 수 가져오기
//        int commentCount = 0;
//
//        //좋아요 수 가져오기
//        int likeCount = 0;
//
//        //이미지 url 가져오기
//        String [] postImageUrls = ;
//
//        // 게시글 작성지 프로필 이미지 url 가져오기
//        String profileImageUrl = ;
//
//
//        //데이터 가져오기
//        CommunityDetailResponseDto detailResponse = new CommunityDetailResponseDto(community, commentCount, likeCount, postImageUrls, profileImageUrl);
//
//    }
//
//}
