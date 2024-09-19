package swyp.swyp6_team7.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.member.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUserEmail(String email);
}
