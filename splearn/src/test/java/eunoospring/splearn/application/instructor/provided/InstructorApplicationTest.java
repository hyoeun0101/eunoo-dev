package eunoospring.splearn.application.instructor.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorStatus;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
@RequiredArgsConstructor
class InstructorApplicationTest {
    final InstructorApplication instructorApplication;

    final InstructorRepository instructorRepository;

    final MemberRegister memberRegister;

    final EntityManager em;

    @Test
    void apply() {
        Member member = registerActiveMember();

        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));
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
    void applyFailMemberNotActive() {
        //given
        // activate 하지 않아 PENDING 상태다.
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        //when & then
        assertThatThrownBy(() -> instructorApplication.apply(new InstructorApplyRequest(member.getId())))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void applyRequestFail() {
        // 포트에 @Valid, 서비스에 @Validated 가 붙어 있어 서비스 진입 전에 걸러진다.
        assertThatThrownBy(() -> instructorApplication.apply(new InstructorApplyRequest(null)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void applyFailMemberNotFound() {
        assertThatThrownBy(() -> instructorApplication.apply(new InstructorApplyRequest(9999L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void duplicateApply() {
        //given
        Member member = registerActiveMember();
        instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        //when & then
        // 한 회원은 한 번만 신청할 수 있다. instructor.member_id 의 유니크 제약으로 막힌다.
        // IDENTITY 전략이라 save 시점에 곧바로 insert 가 나가므로 em.flush() 없이도 여기서 예외가 난다.
        assertThatThrownBy(() -> instructorApplication.apply(new InstructorApplyRequest(member.getId())))
                .isInstanceOf(DuplicateInstructorApplicationException.class);
    }

    @Test
    void approve() {
        //given
        Instructor instructor = applyInstructor();

        //when
        instructorApplication.approve(instructor.getId());
        em.flush();
        em.clear();

        //then
        Instructor found = instructorRepository.findById(instructor.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        //given
        Instructor instructor = applyInstructor();
        instructorApplication.approve(instructor.getId());

        //when & then
        // PENDING 상태에서만 승인할 수 있다.
        assertThatThrownBy(() -> instructorApplication.approve(instructor.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        //given
        Instructor instructor = applyInstructor();

        //when
        instructorApplication.reject(instructor.getId());
        em.flush();
        em.clear();

        //then
        Instructor found = instructorRepository.findById(instructor.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFail() {
        //given
        Instructor instructor = applyInstructor();
        instructorApplication.approve(instructor.getId());

        //when & then
        // PENDING 상태에서만 거절할 수 있다.
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

    private Member registerActiveMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        return memberRegister.activate(member.getId());
    }

    private Instructor applyInstructor() {
        return instructorApplication.apply(new InstructorApplyRequest(registerActiveMember().getId()));
    }

}
