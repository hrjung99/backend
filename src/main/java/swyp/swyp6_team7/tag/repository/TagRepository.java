package swyp.swyp6_team7.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.tag.domain.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

}
