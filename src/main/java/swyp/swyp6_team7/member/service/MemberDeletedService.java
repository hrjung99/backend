package swyp.swyp6_team7.member.service;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.member.entity.*;
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
        DeletedUsers deletedUser = saveDeletedUser(user);

        // 사용자 정보 비식별화 처리
        anonymizeUser(user);

        // 사용자가 생성한 콘텐츠는 탈퇴한 사용자로 연결
        updateUserContentReferences(user.getUserNumber(), deletedUser);

        userRepository.save(user);
    }
    private void anonymizeUser(Users user) {
        user.setUserEmail("deleted@" + user.getUserNumber() + ".com");
        user.setUserName("deletedUser");
        user.setUserPw("");  // 비밀번호는 빈 값으로 처리
        user.setUserGender(Gender.NULL);  // 성별 NULL로 설정
        user.setUserAgeGroup(AgeGroup.UNKNOWN);  // 연령대 UNKNOWN으로 설정
        user.setUserStatus(UserStatus.DELETED);  // 삭제된 사용자 상태로 설정
    }

    private LocalDateTime calculateFinalDeletionDate(LocalDateTime deletedUserDeleteDate) {
        return deletedUserDeleteDate.plusMonths(3);  // 3개월 뒤로 설정
    }


    private DeletedUsers saveDeletedUser(Users user) {
        DeletedUsers deletedUser = new DeletedUsers();
        deletedUser.setUserNumber(user.getUserNumber());
        deletedUser.setDeletedUserEmail(user.getEmail());
        deletedUser.setDeletedUserLoginDate(user.getUserLoginDate());
        deletedUser.setDeletedUserDeleteDate(LocalDateTime.now()); // 현재 탈퇴 시간
        deletedUser.setFinalDeletionDate(calculateFinalDeletionDate(LocalDateTime.now())); // 3개월 뒤 삭제

        return deletedUsersRepository.save(deletedUser);
    }



    private void updateUserContentReferences(Integer userNumber, DeletedUsers deletedUser) {
        List<Travel> userTravels = travelRepository.findByUserNumber(userNumber);
        for (Travel travel : userTravels) {
            travel.setDeletedUser(deletedUser); // 콘텐츠를 탈퇴한 사용자와 연결
            travelRepository.save(travel);
        }
    }

    private void deleteUserFromRepository(Users user) {
        userRepository.delete(user);
    }

    @Scheduled(cron = "0 0 2 * * ?")  // 매일 새벽 2시에 실행
    @Transactional
    public void deleteExpiredUsers() {
        List<DeletedUsers> expiredUsers = deletedUsersRepository.findAllByFinalDeletionDateBefore(LocalDateTime.now());

        for (DeletedUsers deletedUser : expiredUsers) {
            // DeletedUsers와 관련된 모든 데이터 삭제
            userRepository.deleteById(deletedUser.getUserNumber());  // 사용자 정보 삭제
            deletedUsersRepository.delete(deletedUser);  // 탈퇴 정보 삭제
        }
    }

}
