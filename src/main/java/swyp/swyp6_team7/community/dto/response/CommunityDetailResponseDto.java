package swyp.swyp6_team7.community.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.community.domain.Community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommunityDetailResponseDto {

    private int postNumber;         //게시글 번호
    private int userNumber;         //게시글 작성자 유저 번호
    private int categoryNumber;    //카테고리명

    private String title;           //게시글 제목
    private String content;         //게시글 내용
    private String regDate;         //게시글 등록 일시

    private int commentCount;       //해당 게시글의 댓글 수
    private int likeCount;          //해당 게시글의 좋아요 수
    private int viewCount;          //해당 게시글의 조회수

    private String[] postImageUrls;  //해당 게시글의 이미지 url들 (최대 3개)
    private String profileImageUrl;     //게시글 작성자의 프로필 이미지

    //날짜 포맷 변경
    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
    }

    @Builder
    public CommunityDetailResponseDto(
            int postNumber, int userNumber, int categoryNumber, String title, String content,
            LocalDateTime regDate, int commentCount, int likeCount, int viewCount,
            String[] postImageUrls, String profileImageUrl
    ) {
        this.postNumber = postNumber;
        this.userNumber = userNumber;
        this.categoryNumber = categoryNumber;
        this.title = title;
        this.content = content;
        this.regDate = formatDate(regDate);
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.postImageUrls = postImageUrls;
        this.profileImageUrl = profileImageUrl;
    }

    public static CommunityDetailResponseDto fromEntity(Community community, int commentCount, int likeCount, String[] postImageUrls, String profileImageUrl) {
        return CommunityDetailResponseDto.builder()
                .postNumber(community.getPostNumber())
                .userNumber(community.getUserNumber())
                .categoryNumber(community.getCategoryNumber())
                .title(community.getTitle())
                .content(community.getContent())
                .regDate(community.getRegDate())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .viewCount(community.getViewCount())
                .postImageUrls(postImageUrls)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}

