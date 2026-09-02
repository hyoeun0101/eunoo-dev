package eunoospring.splearn.domain.instructor;

import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;

public class InstructorFixture {

    public static Instructor createInstructor() {
        return createInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createInstructor(Member member) {
        return Instructor.apply(member);
    }

    public static Instructor createActiveInstructor() {
        return createActiveInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createActiveInstructor(Member member) {
        Instructor instructor = Instructor.apply(member);
        instructor.approve();
        return instructor;
    }
}
