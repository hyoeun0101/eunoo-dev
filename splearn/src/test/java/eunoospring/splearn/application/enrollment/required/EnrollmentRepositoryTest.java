package eunoospring.splearn.application.enrollment.required;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.domain.enrollment.EnrollmentStatus;
import eunoospring.splearn.support.test.BaseRepositoryTest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
@RequiredArgsConstructor
class EnrollmentRepositoryTest extends BaseRepositoryTest {
    final EnrollmentRepository enrollmentRepository;

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

    }

    @Test
    void findByMemberIdAndCourseId() {

    }


}