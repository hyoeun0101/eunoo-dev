package eunoospring.splearn.application.course.required;

import static eunoospring.splearn.domain.course.CourseFixture.createCourse;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.support.test.BaseRepositoryTest;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@RequiredArgsConstructor
class CourseRepositoryTest extends BaseRepositoryTest {

    Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = prepareActiveInstructor();
    }

    @Test
    void saveAndFindId() {
        Course course = createCourse(instructor);

        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();

        em.flush();
        em.clear();

        Course found = courseRepository.findById(course.getId()).orElseThrow();

        assertThat(found).isEqualTo(course);
    }

    @Test
    void findByTitleContaining() {
        List<Long> courseIds = Stream.of(
                        createCourse(instructor, "Hello Spring "),
                        createCourse(instructor, "Clean Spring 2"),
                        createCourse(instructor, "Clean Code 1"))
                .map(course -> courseRepository.save(course).getId())
                .toList();

        assertThat(courseRepository.findByTitleContaining("Spring").stream().map(Course::getId))
                .isEqualTo(List.of(courseIds.get(0), courseIds.get(1)));

        assertThat(courseRepository.findByTitleContaining("Clean").stream().map(Course::getId))
                .isEqualTo(List.of(courseIds.get(1), courseIds.get(2)));

        assertThat(courseRepository.findByTitleContaining("Code").stream().map(Course::getId))
                .isEqualTo(List.of(courseIds.get(2)));

        assertThat(courseRepository.findByTitleContaining("JAVA").stream().map(Course::getId))
                .isEqualTo(Collections.emptyList());
    }

    @Test
    void findByInstructor() {
        Course course1 = preparePublishedCourse();
        Course course2 = preparePublishedCourse();

        assertThat(courseRepository.findByInstructor(course1.getInstructor())).singleElement().isEqualTo(course1);
        assertThat(courseRepository.findByInstructor(course2.getInstructor())).singleElement().isEqualTo(course2);
    }

    @Test
    void uniqueTitleAndInstructor() {
        Instructor instructor1 = prepareActiveInstructor();
        Course course1 = prepareCourse(instructor1, "Hello Spring");

        // 동일 instructor + 동일 title => Fail
        assertThatThrownBy(() -> courseRepository.save(createCourse(instructor1, "Hello Spring")))
                .isInstanceOf(DataIntegrityViolationException.class);

        // 타 instructor + 동일 title => Ok
        Instructor instructor2 = prepareActiveInstructor();

        courseRepository.save(CourseFixture.createCourse(instructor2, "Hello Spring"));
    }

}