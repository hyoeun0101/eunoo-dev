package eunoospring.splearn.application.enrollment.required;

import eunoospring.splearn.domain.enrollment.Enrollment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface EnrollmentRepository extends Repository<Enrollment, Long> {

    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(Long id);

    List<Enrollment> findByMemberId(Long memberId);

    Optional<Enrollment> findByMemberIdAndCourseId(Long memberId, Long courseId);

}
