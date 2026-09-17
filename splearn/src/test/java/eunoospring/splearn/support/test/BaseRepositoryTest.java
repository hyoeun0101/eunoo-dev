package eunoospring.splearn.support.test;

import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.application.enrollment.required.EnrollmentRepository;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.enrollment.Enrollment;
import eunoospring.splearn.domain.enrollment.EnrollmentFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.support.ResourceTransactionManager;

@DataJpaTest
public class BaseRepositoryTest {

    @Autowired
    protected EnrollmentRepository enrollmentRepository;

    @Autowired
    protected EntityManager em;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected InstructorRepository instructorRepository;

    @Autowired
    protected CourseRepository courseRepository;

    protected Member prepareActiveMember() {
        Member member = MemberFixture.createActiveMember();
        return memberRepository.save(member);
    }

    protected Instructor prepareActiveInstructor() {
        Member member = prepareActiveMember();
        return instructorRepository.save(InstructorFixture.createActiveInstructor(member));
    }

    protected Course prepareCourse() {
        Instructor instructor = prepareActiveInstructor();
        return prepareCourse(instructor, null);
    }

    protected Course prepareCourse(Instructor instructor, String title) {
        return courseRepository.save(CourseFixture.createCourse(instructor, title));
    }

    protected Course preparePublishedCourse() {
        Instructor instructor = prepareActiveInstructor();

        return courseRepository.save(CourseFixture.createPublishedCourse(instructor));
    }

    protected Enrollment prepareEnrollment(Member member, Course course) {
        Enrollment enrollment = EnrollmentFixture.createEnrollment(member, course);
        return enrollmentRepository.save(enrollment);
    }
}
