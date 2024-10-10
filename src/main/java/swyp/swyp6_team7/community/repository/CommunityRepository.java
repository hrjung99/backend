package swyp.swyp6_team7.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.community.domain.Community;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {

    //게시글 목록 조회
    List<Community> findAll();

    //게시물 상세 조회
    Optional<Community> findByPostNumber(int postNumber);

}
