package swyp.swyp6_team7.member.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.member.entity.DeletedUsers;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.DeletedUsersRepository;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberDeletedService {
    private final DeletedUsersRepository deletedUsersRepository;
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;

    public MemberDeletedService(DeletedUsersRepository deletedUsersRepository,
                                TravelRepository travelRepository,
                                UserRepository userRepository) {
        this.deletedUsersRepository = deletedUsersRepository;
        this.travelRepository = travelRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteUserData(Users user) {
        // 탈퇴 회원 정보 저장 (비식별화 처리)
        saveDeletedUser(user);

        // 사용자가 생성한 콘텐츠의 참조 업데이트 (탈퇴 회원 테이블로 연결)
        updateUserContentReferences(user.getUserNumber());

        // 기존 회원 정보 삭제
        deleteUserFromRepository(user);
    }

    private void saveDeletedUser(Users user) {
        DeletedUsers deletedUser = new DeletedUsers();
        deletedUser.setUserNumber(user.getUserNumber());
        deletedUser.setDeletedUserEmail(user.getEmail()); // 이메일을 비식별화하여 설정
        deletedUser.setDeletedUserRegDate(user.getUserRegDate());
        deletedUser.setDeletedUserLoginDate(user.getUserLoginDate());
        deletedUser.setDeletedUserDeleteDate(LocalDateTime.now());

        deletedUsersRepository.save(deletedUser);
    }

    private void updateUserContentReferences(Integer userNumber) {
        List<Travel> userTravels = travelRepository.findByUserNumber(userNumber);
        for (Travel travel : userTravels) {
            travel.setUserNumber(null); // 또는 탈퇴 회원의 ID로 설정
        }
    }

    private void deleteUserFromRepository(Users user) {
        userRepository.delete(user);
    }
}
