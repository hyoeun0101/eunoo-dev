package eunoospring.splearn.domain.instructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;

class InstructorTest {

    private static Instructor applyInstructor() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        return instructor;
    }

    @Test
    void apply() {
        Member member = MemberFixture.createActiveMember();

        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.getMember()).isEqualTo(member);
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void applyFailMemberNotActive() {
        Member member = MemberFixture.createMember();

        assertThatThrownBy(() -> Instructor.apply(member))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void approve() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.approve();

        assertThatThrownBy(instructor::approve).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFail() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.approve();

        assertThatThrownBy(instructor::reject).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive() {
        Instructor instructor = applyInstructor();

        assertThat(instructor.isActive()).isFalse();
        instructor.approve();
        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive() {
        Instructor instructor = applyInstructor();

        assertThatThrownBy(instructor::ensureActive).isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }
}