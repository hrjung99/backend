package swyp.swyp6_team7.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.request.CommentCreateRequestDto;
import swyp.swyp6_team7.comment.dto.response.CommentDetailResponseDto;
import swyp.swyp6_team7.comment.dto.response.CommentListReponseDto;
import swyp.swyp6_team7.comment.repository.CommentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.service.TravelService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    final TravelService travelService;
    private final UserRepository userRepository;

    //Create
    @Transactional
    public Comment create(CommentCreateRequestDto request, int userNumber, String relatedType, int relatedNumber) {
        // 게시물 존재 여부 검증
        ResponseEntity<String> validationResponse = validateRelatedNumber(relatedType, relatedNumber);
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException(validationResponse.getBody());
        }

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

    //댓글 번호로 댓글 조회(response 용)
    public CommentDetailResponseDto getCommentByNumber(int commentNumber) {
        Comment comment = commentRepository.findById(commentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다." + commentNumber));
        CommentDetailResponseDto detailResponse = new CommentDetailResponseDto(comment);
        return detailResponse;
    }


    //해당 게시글이 존재하는지 검증하는 메소드
    @Transactional
    public ResponseEntity<String> validateRelatedNumber(String relatedType, int relatedNumber) {
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


    @Transactional
    public List<CommentListReponseDto> getListByrelatedNumber(String relatedType, int relatedNumber) {

        if (relatedType.equals("travel")) {
            List<Comment> comments = commentRepository.findByRelatedTypeAndRelatedNumber(relatedType, relatedNumber);
            List<CommentListReponseDto> response = new ArrayList<>();

            for (Comment comment : comments) {

                //작성자 조회
                Optional<Users> user = userRepository.findByUserNumber(comment.getUserNumber());
                String writer = user.map(Users::getUserName).orElse("unknown");

                //답글 수 계산
                long repliesCount = commentRepository.countByRelatedTypeAndRelatedNumberAndParentNumber(relatedType, relatedNumber, comment.getParentNumber());

                //DTO
                CommentListReponseDto dto = CommentListReponseDto.fromEntity(comment, writer, repliesCount);
                response.add(dto);

                return response;
            }
            return response; // for loop가 끝난 후 반환
        } else {
            throw new IllegalArgumentException("유효하지 않은 게시물 종류입니다: " + relatedType);
        }
    }







}