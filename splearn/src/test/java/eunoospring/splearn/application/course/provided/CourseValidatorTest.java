package eunoospring.splearn.application.course.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.in;

import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.exception.ValidationException;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseValidatorTest extends BaseApplicationServiceTest {
    final CourseValidator courseValidator;

    final CourseRepository courseRepository;


    @Test
    void titleDuplication() {
        Instructor instructor1 = prepareActiveInstructor();
        Instructor instructor2 = prepareActiveInstructor();

        courseRepository.save(CourseFixture.createCourse(instructor1, "Spring"));

        // instructor1, 중복되지 않은 제목 - OK
        courseValidator.validateForCreate(instructor1, new CourseCreateRequest(instructor1.getId(), "Java", null));

        // instructor1, 중복되는 제목 - FAIL
        assertThatThrownBy(() -> courseValidator.validateForCreate(instructor1,
                new CourseCreateRequest(instructor1.getId(), "Spring", null)))
                .isInstanceOfSatisfying(ValidationException.class, e -> {
                    assertThat(e.getErrors()).hasSize(1);
                });

        // instructor2, 1과 중복되는 제목 - OK
        courseValidator.validateForCreate(instructor2, new CourseCreateRequest(instructor2.getId(), "Spring", null));
    }


    @Test
    void titleDuplicationForUpdate() {
        Instructor instructor = prepareActiveInstructor();
        Course course1 = courseRepository.save(CourseFixture.createCourse(instructor, "Clean Spring 1"));

        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor, "Clean Spring 2"));

        //update
        // instructor1, 동일한 title - OK
        courseValidator.validateForUpdate(course1, CourseFixture.createCourseUpdateRequest("Clean Spring 1"));

        // instructor1, 중복 title - FAIL
        assertThatThrownBy(() ->
                courseValidator.validateForUpdate(course2, CourseFixture.createCourseUpdateRequest("Clean Spring 1")))
                .isInstanceOfSatisfying(ValidationException.class, e -> {
                    assertThat(e.getErrors()).hasSize(1);
                });

    }
}