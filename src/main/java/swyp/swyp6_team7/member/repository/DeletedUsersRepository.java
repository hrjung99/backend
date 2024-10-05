package swyp.swyp6_team7.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.member.entity.DeletedUsers;

public interface DeletedUsersRepository extends JpaRepository<DeletedUsers, Long> {
}
