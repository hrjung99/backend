package swyp.swyp6_team7.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.likes.domain.Like;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    // 좋아요 누름 여부 확인
    boolean existsByRelatedTypeAndRelatedNumberAndUserNumber(String relatedType, int relatedNumber, int userNumber);

    // 좋아요 수 조회
    long countByRelatedTypeAndRelatedNumber(String relatedType, int relatedNumber);

    //삭제
    void deleteByRelatedTypeAndRelatedNumber(String relatedType, int relatedNumber);

    //좋아요 행 조회
    Optional<Like> findByRelatedTypeAndRelatedNumberAndUserNumber(String relatedType, int relatedNumber, int userNumber);
}
