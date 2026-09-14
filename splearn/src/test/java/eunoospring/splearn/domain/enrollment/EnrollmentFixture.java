package eunoospring.splearn.domain.enrollment;

import static org.instancio.Select.field;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import org.instancio.Instancio;

public class EnrollmentFixture {

    public static Enrollment createEnrollment() {
        return createEnrollment(null, null);
    }

    public static Enrollment createEnrollment(Member member, Course course) {
        return Enrollment.enroll(
                member == null ? MemberFixture.createActiveMember() : member,
                course == null ? CourseFixture.createPublishedCourse() : course
        );
    }
}
