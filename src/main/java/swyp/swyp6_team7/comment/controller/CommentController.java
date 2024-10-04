package swyp.swyp6_team7.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.request.CommentCreateRequestDto;
import swyp.swyp6_team7.comment.dto.request.CommentUpdateRequestDto;
import swyp.swyp6_team7.comment.dto.response.CommentDetailResponseDto;
import swyp.swyp6_team7.comment.dto.response.CommentListReponseDto;
import swyp.swyp6_team7.comment.service.CommentService;
import swyp.swyp6_team7.likes.repository.CommentLikeRepository;
import swyp.swyp6_team7.likes.service.CommentLikeService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeService commentLikeService;

    //Create
    @PostMapping("/api/{relatedType}/{relatedNumber}/comments")
    public ResponseEntity<CommentDetailResponseDto> create(
            @RequestBody CommentCreateRequestDto request,
            @RequestHeader("Authorization") String token,
            @PathVariable String relatedType,
            @PathVariable int relatedNumber
    ) {

        int userNumber = jwtProvider.getUserNumber(token);
        Comment createdComment = commentService.create(request, userNumber, relatedType, relatedNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.getCommentByNumber(createdComment.getCommentNumber()));
    }

    // List Read
    @GetMapping("/api/{relatedType}/{relatedNumber}/comments")
    public ResponseEntity<List<CommentListReponseDto>> getComments(
            @RequestHeader("Authorization") String token,
            @PathVariable String relatedType,
            @PathVariable int relatedNumber) {
        int userNumber = jwtProvider.getUserNumber(token);
        List<CommentListReponseDto> comments = commentService.getListByrelatedNumber(relatedType, relatedNumber, userNumber);
        return ResponseEntity.ok(comments);
    }

    //Update
    @PutMapping("/api/comments/{commentNumber}")
    public ResponseEntity<CommentDetailResponseDto> update(
            @RequestBody CommentUpdateRequestDto request,
            @RequestHeader("Authorization") String token,
            @PathVariable int commentNumber

    ) {
        int userNumber = jwtProvider.getUserNumber(token);

        CommentDetailResponseDto updateResponse = commentService.update(request, userNumber, commentNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updateResponse);
    }

    //Update
    @DeleteMapping("/api/comments/{commentNumber}")
    public ResponseEntity delete(
            @RequestHeader("Authorization") String token,
            @PathVariable int commentNumber
    ) {
        int userNumber = jwtProvider.getUserNumber(token);

        commentService.delete(commentNumber, userNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //좋아요 동작

    //좋아요 증가
    @PostMapping("/api/comments/{commentNumber}/like")
    public ResponseEntity<List<CommentListReponseDto>> like(
            @PathVariable int commentNumber,
            @RequestHeader("Authorization") String token) {

        int userNumber = jwtProvider.getUserNumber(token);

        List<CommentListReponseDto> result = commentLikeService.like(commentNumber, userNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    //좋아요 취소
    @DeleteMapping("/api/comments/{commentNumber}/like")
    public ResponseEntity<List<CommentListReponseDto>> unlike(@PathVariable int commentNumber, @RequestHeader("Authorization") String token) {
        int userNumber = jwtProvider.getUserNumber(token);

        List<CommentListReponseDto> result = commentLikeService.unlike(commentNumber, userNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }
}

