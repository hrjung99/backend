package swyp.swyp6_team7.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.request.CommentCreateRequestDto;
import swyp.swyp6_team7.comment.dto.response.CommentDetailResponse;
import swyp.swyp6_team7.comment.repository.CommentRepository;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.service.TravelService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    final TravelService travelService;

    //Create
    @Transactional
    public Comment create(CommentCreateRequestDto request, int userNumber, String relatedType, int relatedNumber) {
        Comment savedComment = commentRepository.save(request.toCommentEntity(
                userNumber
                , request.getContent()
                , request.getParentNumber()
                , 0 //likes
                , LocalDateTime.now() // regDate
                , relatedType
                , relatedNumber
        ));
        return savedComment;
    }

    public CommentDetailResponse getCommentByNumber(int commentNumber) {
        Comment comment = commentRepository.findById(commentNumber)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found" + commentNumber));
        CommentDetailResponse detailResponse = new CommentDetailResponse(comment);
        return detailResponse;
    }


    //해당 게시글이 존재하는지 검증하는 메소드
    @Transactional
    public ResponseEntity<String> validateTravelNumber(String relatedType, int relatedNumber) {
        // 여행 게시글일 경우
        if (relatedType.equals("travel")) {
            try {
                TravelDetailResponse travelDetailResponse = travelService.getDetailsByNumber(relatedNumber);
                return ResponseEntity.ok("게시물 존재 유무 검증 성공.");
            } catch (IllegalArgumentException e) {
                // 검증 실패
                return ResponseEntity.badRequest().body("존재하지 않는 게시글입니다." + e.getMessage());
            }
            //커뮤니티 게시글일 경우
//        } else if (relatedType.equals("community")) {
//
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 게시물 종류 입니다.");
        }
    }



}
