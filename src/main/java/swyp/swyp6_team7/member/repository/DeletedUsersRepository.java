package swyp.swyp6_team7.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.member.entity.DeletedUsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeletedUsersRepository extends JpaRepository<DeletedUsers, Long> {
    List<DeletedUsers> findAllByFinalDeletionDateBefore(LocalDate now);
    Optional<DeletedUsers> findByDeletedUserEmail(String email);
    Optional<List<DeletedUsers>> findAllByDeletedUserEmail(String email);

}
