package eunoospring.splearn.application.enrollment;

import eunoospring.splearn.application.enrollment.provided.EnrollmentFinder;
import eunoospring.splearn.application.enrollment.required.EnrollmentRepository;
import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.support.ApplicationService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class EnrollmentQueryService implements EnrollmentFinder {
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment find(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강을 찾을 수 없습니다: " + enrollmentId));
    }

    @Override
    public List<Enrollment> findByMember(Long memberId) {
        return enrollmentRepository.findByMemberId(memberId);
    }

    @Override
    public Optional<Enrollment> findByMemberAndCourse(Long memberId, Long courseId) {
        return enrollmentRepository.findByMemberIdAndCourseId(memberId, courseId);
    }
}
