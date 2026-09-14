package eunoospring.splearn.application.instructor.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
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
class InstructorFinderTest extends BaseApplicationServiceTest {
    final InstructorFinder instructorFinder;
    final EntityManager em;

    @Test
    void find() {
        Instructor instructor = prepareInstructor();
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
        Member member = prepareActiveMember();
        Member member1 = member;
        member1 = member1 == null ? prepareActiveMember() : member1;

        Instructor instructor1 = instructorApplication.apply(InstructorFixture.createInstructorApplyReqeust(member1));
        instructor1 = instructorApplication.approve(instructor1.getId());
        Instructor instructor = instructor1;
        em.flush();
        em.clear();

        assertThat(instructorFinder.findByMember(member)).get()
                .extracting(Instructor::getId).isEqualTo(instructor.getId());
    }

    @Test
    void findByMemberNotApplied() {
        Member member = prepareActiveMember();
        em.flush();
        em.clear();

        assertThat(instructorFinder.findByMember(member.getId())).isEmpty();
    }
}
