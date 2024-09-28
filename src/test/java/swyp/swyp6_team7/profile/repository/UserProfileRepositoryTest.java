package swyp.swyp6_team7.profile.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import swyp.swyp6_team7.profile.entity.UserProfile;
import com.querydsl.jpa.impl.JPAQueryFactory;  // 추가

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    // QueryDSL 관련된 빈을 Mock 처리
    @MockBean
    private JPAQueryFactory jpaQueryFactory;

    @Test
    @DisplayName("userNumber로 UserProfile을 조회")
    void testFindByUserNumber() {
        // given: 저장된 UserProfile
        UserProfile userProfile = new UserProfile();
        userProfile.setUserNumber(1);
        userProfile.setProIntroduce("Test introduction");
        userProfileRepository.save(userProfile);

        // when: userNumber로 UserProfile 조회
        Optional<UserProfile> foundProfile = userProfileRepository.findByUserNumber(1);

        // then: 조회된 UserProfile이 존재하고 값이 일치하는지 확인
        assertThat(foundProfile).isPresent();
        assertThat(foundProfile.get().getUserNumber()).isEqualTo(1);
        assertThat(foundProfile.get().getProIntroduce()).isEqualTo("Test introduction");
    }

    @Test
    @DisplayName("존재하지 않는 userNumber로 UserProfile을 조회할 경우")
    void testFindByUserNumber_NotFound() {
        // when: 존재하지 않는 userNumber로 UserProfile 조회
        Optional<UserProfile> foundProfile = userProfileRepository.findByUserNumber(999);

        // then: 조회 결과가 비어있는지 확인
        assertThat(foundProfile).isNotPresent();
    }
}
