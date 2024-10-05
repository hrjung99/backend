package swyp.swyp6_team7.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.request.CommentCreateRequestDto;
import swyp.swyp6_team7.comment.dto.request.CommentUpdateRequestDto;
import swyp.swyp6_team7.comment.dto.response.CommentDetailResponseDto;
import swyp.swyp6_team7.comment.dto.response.CommentListReponseDto;
import swyp.swyp6_team7.comment.service.CommentService;
import swyp.swyp6_team7.likes.service.CommentLikeService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final MemberService memberService;

    //Create
    @PostMapping("/api/{relatedType}/{relatedNumber}/comments")
    public ResponseEntity<CommentDetailResponseDto> create(
            @RequestBody CommentCreateRequestDto request,
            Principal principal,
            @PathVariable String relatedType,
            @PathVariable int relatedNumber
    ) {
        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();
        
        Comment createdComment = commentService.create(request, userNumber, relatedType, relatedNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.getCommentByNumber(createdComment.getCommentNumber()));
    }

    // List Read
    @GetMapping("/api/{relatedType}/{relatedNumber}/comments")
    public ResponseEntity<List<CommentListReponseDto>> getComments(
            Principal principal,
            @PathVariable String relatedType,
            @PathVariable int relatedNumber) {

        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        List<CommentListReponseDto> comments = commentService.getListByrelatedNumber(relatedType, relatedNumber, userNumber);
        return ResponseEntity.ok(comments);
    }

    //Update
    @PutMapping("/api/comments/{commentNumber}")
    public ResponseEntity<CommentDetailResponseDto> update(
            @RequestBody CommentUpdateRequestDto request, Principal principal,
            @PathVariable int commentNumber
    ) {
        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        CommentDetailResponseDto updateResponse = commentService.update(request, userNumber, commentNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updateResponse);
    }

    //Delete
    @DeleteMapping("/api/comments/{commentNumber}")
    public ResponseEntity<Void> delete(
            @PathVariable int commentNumber,
            Principal principal
    ) {
        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        commentService.delete(commentNumber, userNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //좋아요
    @PostMapping("/api/comments/{commentNumber}/like")
    public ResponseEntity<List<CommentListReponseDto>> toggleLike(
            @PathVariable int commentNumber,
            Principal principal) {

        // user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        List<CommentListReponseDto> result = commentLikeService.toggleLike(commentNumber, userNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}

