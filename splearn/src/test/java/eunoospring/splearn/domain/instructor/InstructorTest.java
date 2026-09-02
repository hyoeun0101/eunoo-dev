package eunoospring.splearn.domain.instructor;

import static eunoospring.splearn.domain.instructor.InstructorFixture.createActiveInstructor;
import static eunoospring.splearn.domain.instructor.InstructorFixture.createInstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;

class InstructorTest {

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
        Instructor instructor = createInstructor();

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        Instructor instructor = createActiveInstructor();

        assertThatThrownBy(instructor::approve).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Instructor instructor = createInstructor();

        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFail() {
        Instructor instructor = createActiveInstructor();

        assertThatThrownBy(instructor::reject).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive() {
        Instructor instructor = createInstructor();

        assertThat(instructor.isActive()).isFalse();

        instructor.approve();

        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive() {
        Instructor instructor = createInstructor();

        assertThatThrownBy(instructor::ensureActive).isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }
}