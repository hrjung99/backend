package swyp.swyp6_team7.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import swyp.swyp6_team7.config.DataConfig;
import swyp.swyp6_team7.member.entity.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(DataConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // given
        Users user = new Users();
        user.setUserEmail("test@example.com");
        user.setUserPw("password");
        user.setUserName("testuser");
        user.setUserGender(Gender.M);
        user.setUserAgeGroup(AgeGroup.TWENTY);
        user.setUserStatus(UserStatus.ABLE);
        user.setRole(UserRole.USER);
        userRepository.save(user);

        // when
        Optional<Users> foundUser = userRepository.findByUserEmail("test@example.com");

        // then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUserName());
    }
}
