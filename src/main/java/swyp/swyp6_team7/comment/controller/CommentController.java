package swyp.swyp6_team7.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.request.CommentCreateRequestDto;
import swyp.swyp6_team7.comment.dto.response.CommentDetailResponse;
import swyp.swyp6_team7.comment.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    //Create
    @PostMapping("/api/{relatedType}/{relatedNumber}/comments")
    public ResponseEntity<CommentDetailResponse> create(
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
}
