package eunoospring.splearn.domain.course;

import static org.instancio.Instancio.gen;
import static org.instancio.Select.field;
import static org.instancio.Select.fields;

import eunoospring.splearn.application.course.provided.CourseCreateRequest;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import java.time.LocalDateTime;
import org.instancio.Instancio;
import org.instancio.Select;

public class CourseFixture {
    static public Course createCourse() {
        return createCourse(InstructorFixture.createActiveInstructor());
    }

    static public Course createCourse(Instructor instructor) {
        return createCourse(instructor, null);
    }

    static public Course createCourse(Instructor instructor, String title) {

        CourseDetail detail = Instancio.of(CourseDetail.class)
                .ignore(field(CourseDetail::getId))
                .generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(500).nullable())
                .set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
                .create();

        title = title == null ? gen().string().minLength(2).maxLength(100).get() : title;

        return Instancio.of(Course.class)
                .ignore(field(Course::getId))
                .set(field(Course::getInstructor), instructor)
                .set(field(Course::getTitle), title)
                .set(field(Course::getStatus), CourseStatus.DRAFT)
                .set(field(Course::getDetail), detail)
                .create();
    }

    static public CourseCreateRequest createCourseCreateRequest(Long instructorId, String title) {

        title = title == null ? gen().string().minLength(2).maxLength(100).get() : title;

        return Instancio.of(CourseCreateRequest.class)
                .set(field(CourseCreateRequest::instructorId), instructorId)
                .set(field(CourseCreateRequest::title), title)
                .generate(field(CourseCreateRequest::description), gen -> gen.string().maxLength(500).nullable())
                .create();
    }


}
