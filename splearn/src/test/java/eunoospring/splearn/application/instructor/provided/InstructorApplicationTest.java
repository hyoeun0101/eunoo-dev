package eunoospring.splearn.application.instructor.provided;

import static eunoospring.splearn.domain.instructor.InstructorFixture.createInstructorApplyReqeust;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.instructor.InstructorStatus;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.ObjectReadContext.Base;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
@RequiredArgsConstructor
class InstructorApplicationTest extends BaseApplicationServiceTest {
    final InstructorApplication instructorApplication;
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

        // clear 했으므로 1차 캐시가 아니라 DB 에서 다시 읽어온다. 매핑이 틀리면 여기서 드러난다.
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

        //then
        Instructor found = instructorRepository.findById(instructor.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        Instructor instructor = prepareInstructor();
        instructorApplication.approve(instructor.getId());

        //when & then
        // PENDING 상태에서만 승인할 수 있다.
        assertThatThrownBy(() -> instructorApplication.approve(instructor.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Instructor instructor = prepareInstructor();

        instructorApplication.reject(instructor.getId());
        em.flush();
        em.clear();

        //then
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
