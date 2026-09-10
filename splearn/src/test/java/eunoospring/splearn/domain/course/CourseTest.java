package eunoospring.splearn.domain.course;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {

    Course course;

    @BeforeEach
    void setUp() {
        course = CourseFixture.createCourse();
        course.updateInfo(new CourseUpdateInfo(course.getTitle(), "Description"));
    }
    @Test
    void create() {
        Instructor instructor = InstructorFixture.createActiveInstructor();

        String title = "Clean Spring 2";
        String description = "description";
        Course course = new Course(instructor, title, description);

        assertThat(course.getInstructor()).isEqualTo(instructor);
        assertThat(course.getTitle()).isEqualTo(title);
        assertThat(course.getDetail().getDescription()).isEqualTo(description);
        assertThat(course.getStatus()).isEqualTo(CourseStatus.DRAFT);
    }

    @Test
    void createFailInstructorNotActive() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(() -> new Course(instructor, "Clean Spring 2", "description"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void submitForReview() {
        course.submitForReview();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);

        assertThatThrownBy(() -> course.submitForReview())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void submitForReviewFail() {
        Instructor instructor = InstructorFixture.createActiveInstructor();
        Course course = new Course(instructor, "Clean Code 2", "");

        assertThatThrownBy(course::submitForReview)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void publish() {
        course.submitForReview();

        course.publish();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
        assertThat(course.getDetail().getPublishedAt()).isNotNull();

        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void archive() {
        course.submitForReview();
        course.publish();

        course.archive();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
        assertThat(course.getDetail().getArchivedAt()).isNotNull();

        assertThatThrownBy(() -> course.archive())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateInfo() {

        CourseUpdateInfo courseUpdateInfo = new CourseUpdateInfo("Clean Spring 3", "clean spring 3입니다.");

        course.updateInfo(courseUpdateInfo);

        assertThat(course.getTitle()).isEqualTo(courseUpdateInfo.title());
        assertThat(course.getDetail().getDescription()).isEqualTo(courseUpdateInfo.description());

    }

}