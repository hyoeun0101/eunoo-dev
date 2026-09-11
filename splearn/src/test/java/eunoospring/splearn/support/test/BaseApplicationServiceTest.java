package eunoospring.splearn.support.test;

import eunoospring.splearn.application.instructor.provided.InstructorApplication;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.support.ApplicationServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationServiceTest
public class BaseApplicationServiceTest {
    @Autowired
    MemberRegister memberRegister;

    @Autowired
    InstructorApplication instructorApplication;

    protected Member member;
    protected                                                                                                                                         Instructor instructor;

    protected Instructor prepareInstructor() {
        prepareActiveMember();
        this.instructor = instructorApplication.apply(InstructorFixture.createInstructorApplyReqeust(member));
        return instructor;
    }

    protected Instructor prepareActiveInstructor() {
        this.prepareInstructor();
        instructorApplication.approve(instructor.getId());
        return instructor;
    }

    protected Member prepareActiveMember() {
        this.member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        memberRegister.activate(member.getId());
        return member;
    }
}
