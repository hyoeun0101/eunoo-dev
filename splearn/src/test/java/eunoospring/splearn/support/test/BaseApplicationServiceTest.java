package eunoospring.splearn.support.test;

import eunoospring.splearn.application.course.provided.CourseCreator;
import eunoospring.splearn.application.instructor.provided.InstructorApplication;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.support.ApplicationServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationServiceTest
public class BaseApplicationServiceTest {
    @Autowired
    CourseCreator courseCreator;

    @Autowired
    MemberRegister memberRegister;

    @Autowired
    InstructorApplication instructorApplication;

    protected Member member;
    protected Instructor instructor;
    protected Course course;

    protected Member prepareActiveMember() {
        member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        member = memberRegister.activate(member.getId());
        return member;
    }

    protected Instructor prepareInstructor() {
        prepareActiveMember();
        this.instructor = instructorApplication.apply(InstructorFixture.createInstructorApplyReqeust(member));
        return instructor;
    }

    protected Instructor prepareActiveInstructor() {
        prepareInstructor();
        instructorApplication.approve(instructor.getId());
        return instructor;
    }


    protected void prepareCourse() {
        prepareActiveInstructor();
        course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), null));
        course.updateInfo(CourseFixture.createCourseUpdateRequest(null).toInfo());
    }
}
