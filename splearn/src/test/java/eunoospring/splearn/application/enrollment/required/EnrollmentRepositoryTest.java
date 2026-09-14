package eunoospring.splearn.application.enrollment.required;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.support.test.BaseRepositoryTest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
@RequiredArgsConstructor
class EnrollmentRepositoryTest extends BaseRepositoryTest {

    @Test
    void saveAndFindId() {
        preparePublishedCourse();

        Enrollment enrollment = enrollmentRepository.save(Enrollment.enroll(member, course));

        em.flush();
        em.clear();

        assertThat(enrollment.getId()).isNotNull();

        Enrollment found = enrollmentRepository.findById(enrollment.getId()).orElseThrow();

        assertThat(found).isEqualTo(enrollment);
    }

    @Test
    void findByMemberId() {
        Member member1 = prepareActiveMember();
        Member member2 = prepareActiveMember();

        Course course1 = preparePublishedCourse();
        Course course2 = preparePublishedCourse();

        Enrollment enrollment1_1 = prepareEnrollment(member1, course1);
        Enrollment enrollment1_2 = prepareEnrollment(member1, course2);
        Enrollment enrollment2_1 = prepareEnrollment(member2, course2);

        List<Enrollment> enrollments1 = enrollmentRepository.findByMemberId(member1.getId());
        List<Enrollment> enrollments2 = enrollmentRepository.findByMemberId(member2.getId());
        List<Enrollment> enrollments3 = enrollmentRepository.findByMemberId(prepareActiveMember().getId());

        assertThat(enrollments1).hasSize(2).containsExactly(enrollment1_1, enrollment1_2);
        assertThat(enrollments2).hasSize(1).containsExactly(enrollment2_1);
        assertThat(enrollments3).hasSize(0);

    }

    @Test
    void findByMemberIdAndCourseId() {
        Member member1 = prepareActiveMember();
        Member member2 = prepareActiveMember();

        Course course1 = preparePublishedCourse();
        Course course2 = preparePublishedCourse();

        Enrollment enrollment1 = prepareEnrollment(member1, course1);
        Enrollment enrollment2 = prepareEnrollment(member2, course2);

        assertThat(enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course1.getId()).orElseThrow())
                .isEqualTo(enrollment1);

        assertThat(enrollmentRepository.findByMemberIdAndCourseId(member2.getId(), course2.getId()).orElseThrow())
                .isEqualTo(enrollment2);

        assertThat(enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course2.getId()).isPresent()).isFalse();
    }


}