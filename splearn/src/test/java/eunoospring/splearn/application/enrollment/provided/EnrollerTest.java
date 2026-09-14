package eunoospring.splearn.application.enrollment.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.domain.enrollment.EnrollmentStatus;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class EnrollerTest extends BaseApplicationServiceTest {

    @Test
    void enroll() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        Enrollment enrollment = enroller.enroll(new EnrollRequest(member.getId(), course.getId()));

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enrollment.getMember()).isEqualTo(member);
        assertThat(enrollment.getCourse()).isEqualTo(course);
    }

    @Test
    void enrollFailDuplication() {
        Enrollment enrollment = prepareEnrollment();

        assertThatThrownBy(() -> enroller.enroll(
                new EnrollRequest(enrollment.getMember().getId(), enrollment.getCourse().getId())
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 수강 중인 강의입니다");
    }

    @Test
    void enrollFailNullIds() {
        Enrollment enrollment = prepareEnrollment();
        assertThatThrownBy(() -> enroller.enroll(
                new EnrollRequest(null, null)
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void startStudying() {
        Enrollment enrollment = prepareEnrollment();

        Enrollment studying = enroller.startStudying(enrollment.getId());

        assertThat(studying.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);
    }

    @Test
    void complete() {
        Enrollment enrollment = prepareEnrollment();
        enrollment = enroller.startStudying(enrollment.getId());

        Enrollment complete = enroller.complete(enrollment.getId());

        assertThat(complete.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(complete.getCompletedAt()).isNotNull();
    }

}