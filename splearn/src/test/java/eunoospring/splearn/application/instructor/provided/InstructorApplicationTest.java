package eunoospring.splearn.application.instructor.provided;

import static eunoospring.splearn.domain.instructor.InstructorFixture.createInstructorApplyReqeust;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.instructor.InstructorStatus;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
@RequiredArgsConstructor
class InstructorApplicationTest extends BaseApplicationServiceTest {
    final InstructorRepository instructorRepository;

    final EntityManager em;

    @Test
    void apply() {
        Member member = prepareActiveMember();

        Instructor instructor = instructorApplication.apply(createInstructorApplyReqeust(member));
        em.flush();
        em.clear();

        assertThat(instructor.getId()).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        Instructor found = instructorRepository.findById(instructor.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.PENDING);
        assertThat(found.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void duplicateApply() {
        Instructor instructor = prepareActiveInstructor();

        assertThatThrownBy(() ->
                instructorApplication.apply(InstructorFixture.createInstructorApplyReqeust(instructor.getMember())))
                .isInstanceOf(DuplicateInstructorApplicationException.class);
    }

    @Test
    void approve() {
        Instructor instructor = prepareInstructor();

        instructorApplication.approve(instructor.getId());
        em.flush();
        em.clear();

        Instructor found = instructorRepository.findById(instructor.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        Instructor instructor = prepareInstructor();
        instructorApplication.approve(instructor.getId());

        assertThatThrownBy(() -> instructorApplication.approve(instructor.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Instructor instructor = prepareInstructor();

        instructorApplication.reject(instructor.getId());
        em.flush();
        em.clear();

        Instructor found = instructorRepository.findById(instructor.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFail() {
        Instructor instructor = prepareActiveInstructor();

        assertThatThrownBy(() -> instructorApplication.reject(instructor.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void instructorNotFound() {
        assertThatThrownBy(() -> instructorApplication.approve(9999L))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> instructorApplication.reject(9999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
