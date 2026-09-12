package eunoospring.splearn.application.course.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.course.CourseUpdateInfo;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {
    final CourseCreator courseCreator;

    @Test
    void create() {
        prepareActiveInstructor();

        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), "Spring"));

        assertThat(course.getInstructor().getId()).isEqualTo(instructor.getId());
        assertThat(course.getTitle()).isEqualTo("Spring");
    }

    @Test
    void updateInfo() {
        prepareActiveInstructor();
        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), "Java"));

        course = courseCreator.updateInfo(course.getId(), CourseFixture.createCourseUpdateRequest("Spring"));

        assertThat(course.getTitle()).isEqualTo("Spring");
    }

}