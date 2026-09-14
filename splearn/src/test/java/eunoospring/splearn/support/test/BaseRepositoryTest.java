package eunoospring.splearn.support.test;

import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class BaseRepositoryTest {
    @Autowired
    protected EntityManager em;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected InstructorRepository instructorRepository;
    @Autowired
    protected CourseRepository courseRepository;

    protected Member member;
    protected Instructor instructor;
    protected Course course;

    protected void preparePublishedCourse() {
        prepareActiveMember();
        preprareActiveInstructor(member);

        course = CourseFixture.createPublishedCourse(instructor);
        courseRepository.save(course);
    }

    protected Instructor preprareActiveInstructor(Member member) {
        instructor = InstructorFixture.createActiveInstructor(member);
        instructorRepository.save(instructor);
        return instructor;
    }

    protected Member prepareActiveMember() {
        member = MemberFixture.createActiveMember();
        memberRepository.save(member);
        return member;
    }
}
