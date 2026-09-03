package eunoospring.splearn.application.instructor.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorStatus;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
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
class InstructorFinderTest {
    final InstructorFinder instructorFinder;

    final InstructorApplication instructorApplication;

    final MemberRegister memberRegister;

    final EntityManager em;

    @Test
    void find() {
        //given
        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(registerActiveMember().getId()));
        em.flush();
        em.clear();

        //when & then
        Instructor found = instructorFinder.find(instructor.getId());
        assertThat(found.getId()).isEqualTo(instructor.getId());
        assertThat(found.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void findFail() {
        assertThatThrownBy(() -> instructorFinder.find(9999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByMember() {
        //given
        Member member = registerActiveMember();
        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));
        em.flush();
        em.clear();

        //when & then
        // Member 를 받는 기본 메서드는 id 를 받는 쪽으로 위임한다.
        assertThat(instructorFinder.findByMember(member)).get()
                .extracting(Instructor::getId).isEqualTo(instructor.getId());
    }

    @Test
    void findByMemberNotApplied() {
        //given
        // 강사 신청을 하지 않은 회원이다.
        Member member = registerActiveMember();
        em.flush();
        em.clear();

        //when & then
        assertThat(instructorFinder.findByMember(member.getId())).isEmpty();
    }

    private Member registerActiveMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        return memberRegister.activate(member.getId());
    }

}
