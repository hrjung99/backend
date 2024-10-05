package swyp.swyp6_team7.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.image.domain.Image;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // 프로필 이미지 조회 1:1
    Optional<Image> findByRelatedTypeAndRelatedNumber(String relatedType, int relatedNumber);
    Optional<Image> findByImageNumber(Long imageNumber);

//    // 게시물 이미지 조회 - 1:다
//    List<Image> findAllByRelatedTypeAndRelatedNumber(String relatedType, int relatedNumber);

}
