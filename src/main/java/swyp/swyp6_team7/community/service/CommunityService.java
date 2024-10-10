package swyp.swyp6_team7.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.category.repository.CategoryRepository;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.repository.CommentRepository;
import swyp.swyp6_team7.comment.service.CommentService;
import swyp.swyp6_team7.community.domain.Community;
import swyp.swyp6_team7.community.dto.request.CommunityCreateRequestDto;
import swyp.swyp6_team7.community.dto.request.CommunityUpdateRequestDto;
import swyp.swyp6_team7.community.dto.response.CommunityDetailResponseDto;
import swyp.swyp6_team7.community.repository.CommunityRepository;
import swyp.swyp6_team7.image.s3.S3Component;
import swyp.swyp6_team7.image.s3.S3Uploader;
import swyp.swyp6_team7.image.service.ImageService;
import swyp.swyp6_team7.likes.dto.response.LikeReadResponseDto;
import swyp.swyp6_team7.likes.repository.LikeRepository;
import swyp.swyp6_team7.likes.util.LikeStatus;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final S3Component s3Component; // S3Component 추가
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final CommentService commentService;


    //Create
    @Transactional
    public CommunityDetailResponseDto create(CommunityCreateRequestDto request, int userNumber, MultipartFile[] images) throws IOException {

        //게시물 등록
        Community savedPost = communityRepository.save(request.toCommunityEntity(
                userNumber,
                request.getCategoryNumber(),
                request.getTitle(),
                request.getContent(),
                LocalDateTime.now(), // 등록 일시
                0 // 조회수
        ));

        //이미지 등록
        imageService.uploadImage(images, "community", savedPost.getPostNumber());


        //등록된 게시물 상세 정보 가져와서 리턴
        return getDetailsByPostNumber(savedPost.getPostNumber(), userNumber);
    }

    //Detail Read
    public CommunityDetailResponseDto getDetailsByPostNumber(int postnumber, int userNumber) {
        //userNubmer : 조회 요청자의 회원 번호

        //존재하는 게시글인지 확인
        Community community = communityRepository.findByPostNumber(postnumber)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다." + postnumber));

        // 게시글 작성자 명 가져오기
        String postWriter = userRepository.findByUserNumber(community.getUserNumber()).get().getUserName();

        String CategoryName = categoryRepository.findByCategoryNumber(community.getCategoryNumber()).getCategoryName();

        // 댓글 수 가져오기
        long commentCount = commentRepository.countByRelatedTypeAndRelatedNumber("community", postnumber);

        //좋아요 상태 가져오기
        LikeReadResponseDto likeStatus = LikeStatus.getLikeStatus(likeRepository, "community", postnumber, userNumber);
        long likes = likeStatus.getTotalLikes(); //좋아요 수
        boolean liked = likeStatus.isLiked(); //좋아요 여부

        //이미지 url 가져오기
        String baseFolder = s3Component.getBaseFolder();  // S3Component에서 baseFolder 가져오기
        String[] postImageUrls = new String[3];

        for (int i = 1; i <= 3; i++) {
            String folderPath = baseFolder + "/community/" + postnumber + "/" + i;

            String imageUrl = s3Uploader.getSingleFileUrlFromS3(folderPath);
            postImageUrls[i - 1] = imageUrl != null ? imageUrl : ""; // 이미지가 있으면 URL, 없으면 빈 문자열
        }


        // 게시글 작성자 프로필 이미지 url 가져오기
        String profileImageUrl = s3Uploader.getSingleFileUrlFromS3(baseFolder + "/profile/" + community.getUserNumber());


        //데이터 Dto에 담기
        CommunityDetailResponseDto detailResponse = CommunityDetailResponseDto.builder()
                .postNumber(community.getPostNumber())   // 게시글 번호
                .userNumber(community.getUserNumber())   // 작성자 유저 번호
                .postWriter(postWriter)                 // 작성자명
                .categoryNumber(community.getCategoryNumber())  // 카테고리 번호
                .categoryName(CategoryName)             // 카테고리명
                .title(community.getTitle())            // 게시글 제목
                .content(community.getContent())        // 게시글 내용
                .regDate(community.getRegDate())        // 게시글 등록일시
                .commentCount(commentCount)             // 댓글 수
                .viewCount(community.getViewCount())    // 조회수
                .likeCount(likes)                       // 좋아요 수
                .liked(liked)                           // 좋아요 여부
                .postImageUrls(postImageUrls)           // 게시글 이미지 URL 배열
                .profileImageUrl(profileImageUrl)       // 작성자 프로필 이미지 URL
                .build();
        return detailResponse;
    }

    @Transactional
    public CommunityDetailResponseDto update(CommunityUpdateRequestDto request, int postNumber, int userNumber, MultipartFile[] images) throws IOException {
        // 수정할 게시글이 존재하는지 확인
        Community community = communityRepository.findByPostNumber(postNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + postNumber));
        // 본인 게시글인지 확인
        if (community.getUserNumber() != userNumber) {
            // 게시글 업데이트
            community.update(request.getCategoryNumber(), request.getTitle(), request.getContent());

            // 새 이미지 업로드
            imageService.uploadImage(images, "community", community.getPostNumber());
        }
        // 게시글 업데이트 후 상세 정보 반환
        return getDetailsByPostNumber(community.getPostNumber(), userNumber);
    }

    @Transactional
    //나중에 List 로 수정할 것
    public void delete(int postNumber, int userNumber) {

        // 삭제할 게시글이 존재하는지 확인
        Community community = communityRepository.findByPostNumber(postNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + postNumber));

        // 본인 게시글인지 확인
        if (community.getUserNumber() == userNumber) {

            //좋아요 기록 삭제
            likeRepository.deleteByRelatedTypeAndRelatedNumber("community", postNumber);

            //댓글 삭제
            List<Comment> comments = commentRepository.findByRelatedTypeAndRelatedNumber("community", postNumber);
            for (Comment comment : comments) {
                commentService.delete(comment.getCommentNumber(), userNumber);
            }
            // 이미지 삭제
            imageService.deleteImage("community", postNumber);


            // 게시글 삭제
            communityRepository.delete(community);
        } else {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }


    }
}
