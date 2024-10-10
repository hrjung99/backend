package swyp.swyp6_team7.image.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swyp.swyp6_team7.community.domain.Community;
import swyp.swyp6_team7.community.repository.CommunityRepository;
import swyp.swyp6_team7.image.s3.FileFolder;
import swyp.swyp6_team7.image.s3.S3Component;
import swyp.swyp6_team7.profile.entity.UserProfile;
import swyp.swyp6_team7.profile.repository.UserProfileRepository;

import java.util.Optional;


@Component
public class FileFolderHandler {
    private final String baseFolder;
    private final UserProfileRepository userProfileRepository;
    private final CommunityRepository communityRepository;


    @Autowired
    public FileFolderHandler(S3Component s3Component, UserProfileRepository userProfileRepository, CommunityRepository communityRepository) {
        //베이스 폴더 가져오기
        this.baseFolder = s3Component.getBaseFolder();
        //userProfileRepository 가져오기 (relatedType, relatedNumber 검증을 위함
        //relatedType이 profile일때 relatedNumber는 userNumber
        this.userProfileRepository = userProfileRepository;
        this.communityRepository = communityRepository;
    }

    // 동적으로 path 생성 메소드 {baseFolder}/{rleatedType}/{id}/{file_name}
    public String generateS3Path(String relatedType, int relatedNumber, String storageName, int order) {
        FileFolder folderType = FileFolder.from(relatedType);

        //profile 인 경우
        if (folderType == FileFolder.PROFILE) {
            //해당 프로필이 존재하는지 확인
            Optional<UserProfile> userProfile = userProfileRepository.findByUserNumber(relatedNumber);
            if (userProfile.isPresent()) {
                //존재하면 소문자로 만든 파일 경로 리턴
                return baseFolder + folderType.name().toLowerCase() + "/" + relatedNumber + "/" + storageName;
            } else {
                //존재 하지 않으면 예외 처리
                throw new IllegalArgumentException("프로필이 유효하지 않습니다.: 유저 번호 : " + relatedNumber);
            }
        }
        //커뮤니티 인경우
        else if (folderType == FileFolder.COMMUNITY) {

            //게시글이 존재하는지 확인.
            Optional<Community> post = communityRepository.findByPostNumber(relatedNumber);
            if (post.isPresent()) {
                //존재하면 소문자로 만든 파일 경로 리턴
                return baseFolder + folderType.name().toLowerCase() + "/" + relatedNumber + "/" + (order > 0 ? order + "/" : "") + storageName;
            } else {
                //존재 하지 않으면 예외 처리
                throw new IllegalArgumentException("커뮤니티 게시물이 유효하지 않습니다.: 게시물 번호 : " + relatedNumber);
            }

        }
        //유효하지 않은 relatedType일 경우 예외처리
        else {
            throw new IllegalArgumentException("커뮤니티 게시물이 유효하지 않는 타입입니다.: " + relatedType);
        }
    }
}
