package swyp.swyp6_team7.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.enrollment.domain.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>, EnrollmentCustomRepository {

}
