package swyp.swyp6_team7.companion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.companion.domain.Companion;

public interface CompanionRepository extends JpaRepository<Companion, Long>, CompanionCustomRepository {

}
