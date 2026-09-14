package eunoospring.splearn.support.test;

import eunoospring.splearn.application.course.provided.CourseCreator;
import eunoospring.splearn.application.course.provided.CoursePublisher;
import eunoospring.splearn.application.enrollment.provided.EnrollRequest;
import eunoospring.splearn.application.enrollment.provided.Enroller;
import eunoospring.splearn.application.instructor.provided.InstructorApplication;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.support.ApplicationServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationServiceTest
public class BaseApplicationServiceTest {
    @Autowired
    protected MemberRegister memberRegister;

    @Autowired
    protected InstructorApplication instructorApplication;

    @Autowired
    protected CourseCreator courseCreator;

    @Autowired
    protected CoursePublisher coursePublisher;

    @Autowired
    protected Enroller enroller;

    protected Member prepareMember() {
        return memberRegister.register(MemberFixture.createMemberRegisterRequest());
    }

    protected Member prepareActiveMember() {
        Member member = prepareMember();
        return memberRegister.activate(member.getId());
    }

    protected Instructor prepareInstructor() {
        Member member = prepareActiveMember();

        return instructorApplication.apply(InstructorFixture.createInstructorApplyReqeust(member));
    }

    protected Instructor prepareActiveInstructor() {
        Member member = prepareActiveMember();

        Instructor instructor = instructorApplication.apply(InstructorFixture.createInstructorApplyReqeust(member));
        return instructorApplication.approve(instructor.getId());
    }

    protected Course prepareCourse() {
        Instructor instructor = prepareActiveInstructor();
        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), null));
        return courseCreator.updateInfo(course.getId(), CourseFixture.createCourseUpdateRequest(null));
    }

    protected Course preparePublishedCourse() {
        Course course = prepareCourse();
        coursePublisher.submitForReview(course.getId());
        return coursePublisher.publish(course.getId());
    }

    protected Enrollment prepareEnrollment() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        return enroller.enroll(new EnrollRequest(member.getId(), course.getId()));
    }
}
