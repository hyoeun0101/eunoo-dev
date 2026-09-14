package eunoospring.splearn.application.enrollment;

import eunoospring.splearn.application.course.provided.CourseFinder;
import eunoospring.splearn.application.enrollment.provided.EnrollRequest;
import eunoospring.splearn.application.enrollment.provided.Enroller;
import eunoospring.splearn.application.enrollment.provided.EnrollmentFinder;
import eunoospring.splearn.application.enrollment.required.EnrollmentRepository;
import eunoospring.splearn.application.member.provided.MemberFinder;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.support.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class EnrollmentModifyService implements Enroller {
    private final EnrollmentFinder enrollmentFinder;
    private final EnrollmentRepository enrollmentRepository;
    private final MemberFinder memberFinder;
    private final CourseFinder courseFinder;

    @Override
    public Enrollment enroll(EnrollRequest request) {
        Member member = memberFinder.find(request.memberId());
        Course course = courseFinder.find(request.courseId());

        checkDuplication(member, course);

        // member, course의 검증은 Enrollment 도메인 내에서 하고 있음.
        Enrollment enrollment = Enrollment.enroll(member, course);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment startStudying(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.startStudying();

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment complete(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.complete();

        return enrollmentRepository.save(enrollment);
    }

    private void checkDuplication(Member member, Course course) {
        // DB에서 unique로 검증하지만, DB 부하를 줄이기 위해 미리 코드레벨에서 검증하기.
        if (enrollmentRepository.findByMemberIdAndCourseId(member.getId(), course.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 수강 중인 강의입니다");
        }
    }
}
