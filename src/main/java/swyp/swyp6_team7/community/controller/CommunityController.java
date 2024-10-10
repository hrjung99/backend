package swyp.swyp6_team7.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import swyp.swyp6_team7.community.dto.request.CommunityCreateRequestDto;
import swyp.swyp6_team7.community.dto.request.CommunityUpdateRequestDto;
import swyp.swyp6_team7.community.dto.response.CommunityDetailResponseDto;
import swyp.swyp6_team7.community.repository.CommunityCustomRepository;

import swyp.swyp6_team7.community.service.CommunityService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community")
public class CommunityController {
    private final CommunityService communityService;
    private final MemberService memberService;
    private final CommunityCustomRepository communityCustomRepository;


    //C
    @PostMapping
    public ResponseEntity<CommunityDetailResponseDto> create(
            @RequestBody CommunityCreateRequestDto request, Principal principal,
            @RequestParam("files") MultipartFile[] images) throws IOException {

        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        // 게시물 등록 동작 후 상세 정보 가져오기
        CommunityDetailResponseDto detailResponse = communityService.create(request, userNumber, images);

        return ResponseEntity.ok(detailResponse).badRequest().body(null);
    }

    //게시물 목록
//    @GetMapping("/list")
//    public ResponseEntity<List<CommunityListResponseDto>> getList (Principal principal) {
//
//        return
//    }

    @GetMapping("/{postNumber}")
    public ResponseEntity<CommunityDetailResponseDto> getDetail(
            @PathVariable int postNumber, Principal principal
    ) {
        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        //조회수 +1 동작
        communityCustomRepository.incrementViewCount(postNumber);

        //게시물 상세보기 데이터 가져오기
        CommunityDetailResponseDto detailResponse = communityService.getDetailsByPostNumber(postNumber, userNumber);

//        //댓글 리스트 가져오기
//        List<CommentListReponseDto> commentList = commentService.getList("community",detailResponse.getPostNumber(), userNumber);

        return ResponseEntity.ok(detailResponse).badRequest().body(null);
    }

    @PutMapping("/{postNumber}")
    public ResponseEntity<CommunityDetailResponseDto> update(
            @RequestBody CommunityUpdateRequestDto request, Principal principal,
            @RequestParam("files") MultipartFile[] images,
            @PathVariable int postNumber) throws IOException {

        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        // 게시물 수정 동작 후 상세 정보 가져오기
        CommunityDetailResponseDto detailResponse = communityService.update(request, postNumber, userNumber, images);

        return ResponseEntity.ok(detailResponse).badRequest().body(null);
    }

    @DeleteMapping("/{postNumber}")
    public ResponseEntity<Void> delete(@PathVariable int postNumber, Principal principal) throws IOException {

        //user number 가져오기
        String userEmail = principal.getName();
        Users user = memberService.findByEmail(userEmail);
        int userNumber = user.getUserNumber();

        try {
            // 게시물 삭제 동작
            communityService.delete(postNumber, userNumber);
            return ResponseEntity.noContent().build(); // 삭제 성공 시 No Content 응답
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            log.error("게시물 삭제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null); // 잘못된 요청
        } catch (Exception e) {
            // 다른 예외 처리
            log.error("게시물 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 서버 오류
        }
    }
}
