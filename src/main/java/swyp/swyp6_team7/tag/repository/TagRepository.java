package swyp.swyp6_team7.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.tag.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag getByName(String name);

    boolean existsByName(String name);

}
