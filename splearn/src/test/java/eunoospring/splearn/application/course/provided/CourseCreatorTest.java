package eunoospring.splearn.application.course.provided;

import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.application.curriculum.provided.CurriculumFinder;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {
    final CourseCreator courseCreator;
    final CurriculumFinder curriculumFinder;

    @Test
    void create() {
        Instructor instructor = prepareActiveInstructor();

        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), "Spring"));

        assertThat(course.getInstructor().getId()).isEqualTo(instructor.getId());
        assertThat(course.getTitle()).isEqualTo("Spring");

        // Course 생성될 때, Curriculum도 생성됐는지 확인
        assertThat(curriculumFinder.findByCourse(course.getId())).isNotNull();
    }

    @Test
    void updateInfo() {
        Instructor instructor = prepareActiveInstructor();
        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), "Java"));

        course = courseCreator.updateInfo(course.getId(), CourseFixture.createCourseUpdateRequest("Spring"));

        assertThat(course.getTitle()).isEqualTo("Spring");
    }
}