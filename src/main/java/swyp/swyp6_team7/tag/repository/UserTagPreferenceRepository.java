package swyp.swyp6_team7.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.swyp6_team7.tag.domain.UserTagPreference;

import java.util.List;

public interface UserTagPreferenceRepository extends JpaRepository<UserTagPreference, Integer> {

    @Query("select t.tag.name from UserTagPreference t where t.user.userNumber = :userNumber")
    List<String> findPreferenceTagsByUserNumber(@Param("userNumber") int userNumber);

}
