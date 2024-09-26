package swyp.swyp6_team7.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.profile.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUserNumber(Integer userNumber);
}
