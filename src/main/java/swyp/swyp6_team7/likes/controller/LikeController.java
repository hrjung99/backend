package swyp.swyp6_team7.likes.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.comment.dto.response.CommentListReponseDto;
import swyp.swyp6_team7.likes.service.LikeService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final MemberService memberService;
    private final LikeService likeService;

    //좋아요
    @PostMapping("/api/comments/{commentNumber}/like")
    public ResponseEntity<List<CommentListReponseDto>> toggleLike(
            @PathVariable int commentNumber,
            Principal principal) {

        // user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        List<CommentListReponseDto> result = likeService.toggleLike("comment", commentNumber, userNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
