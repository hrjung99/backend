package swyp.swyp6_team7.enrollment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.enrollment.domain.QEnrollment;
import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;
import swyp.swyp6_team7.enrollment.dto.QEnrollmentResponse;
import swyp.swyp6_team7.member.entity.QUsers;

import java.util.List;


@Repository
public class EnrollmentCustomRepositoryImpl implements EnrollmentCustomRepository{

    private final JPAQueryFactory queryFactory;

    public EnrollmentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QEnrollment enrollment = QEnrollment.enrollment;
    QUsers users = QUsers.users;


    @Override
    public List<EnrollmentResponse> findEnrollmentsByTravelNumber(int travelNumber) {
        return queryFactory
                .select(new QEnrollmentResponse(
                        enrollment.number,
                        users.userName,
                        users.userAgeGroup,
                        enrollment.createdAt,
                        enrollment.message,
                        enrollment.status
                ))
                .from(enrollment)
                .leftJoin(users).on(enrollment.userNumber.eq(users.userNumber))
                .where(enrollment.travelNumber.eq(travelNumber))
                .orderBy(enrollment.createdAt.desc())
                .fetch();
    }

}
