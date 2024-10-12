package swyp.swyp6_team7.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.member.entity.DeletedUsers;

import java.time.LocalDateTime;
import java.util.List;

public interface DeletedUsersRepository extends JpaRepository<DeletedUsers, Long> {
    List<DeletedUsers> findAllByFinalDeletionDateBefore(LocalDateTime now);

}
