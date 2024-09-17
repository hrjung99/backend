package swyp.swyp6_team7.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.member.entity.SocialUsers;

import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUsers, Integer> {
    // socialNumber로 SocialUsers 찾기
    Optional<SocialUsers> findBySocialNumber(Integer socialNumber);
}
