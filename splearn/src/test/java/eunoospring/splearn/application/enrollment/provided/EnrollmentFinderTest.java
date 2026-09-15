package eunoospring.splearn.application.enrollment.provided;

import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class EnrollmentFinderTest extends BaseApplicationServiceTest {
    final EnrollmentFinder enrollmentFinder;

    @Test
    void find() {
        Enrollment enrollment = prepareEnrollment();

        assertThat(enrollmentFinder.find(enrollment.getId())).isEqualTo(enrollment);
    }

    @Test
    void findByMember() {
        Enrollment enrollment = prepareEnrollment();

        assertThat(enrollmentFinder.findByMember(enrollment.getMember().getId())).containsExactly(enrollment);
    }

    @Test
    void findByMemberAndCourse() {
        Enrollment enrollment = prepareEnrollment();

        Long memberId = enrollment.getMember().getId();
        Long courseId = enrollment.getCourse().getId();
        assertThat(enrollmentFinder.findByMemberAndCourse(memberId, courseId)).isPresent();
    }



}