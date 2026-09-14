package eunoospring.splearn.domain.enrollment;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;

class EnrollmentTest {
    @Test
    void enroll() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createPublishedCourse();

        Enrollment enrollment = Enrollment.enroll(member, course);

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enrollment.getMember()).isEqualTo(member);
        assertThat(enrollment.getCourse()).isEqualTo(course);
        assertThat(enrollment.getEnrolledAt()).isNotNull();
    }

    @Test
    void enrollFailNotPublishedCourse() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createCourse();

        assertThatThrownBy(() -> Enrollment.enroll(member, course))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void startStudying() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();

        enrollment.startStudying();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);

        assertThatThrownBy(enrollment::startStudying)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void complete() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.startStudying();

        enrollment.complete();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(enrollment.getCompletedAt()).isNotNull();
        assertThatThrownBy(enrollment::complete)
                .isInstanceOf(IllegalStateException.class);

    }


}