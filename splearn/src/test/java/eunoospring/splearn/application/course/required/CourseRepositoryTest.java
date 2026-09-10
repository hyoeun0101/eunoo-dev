package eunoospring.splearn.application.course.required;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.instructor.InstructorFixture;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
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
class CourseRepositoryTest {
    final CourseRepository courseRepository;

    final EntityManager em;

    final MemberRepository memberRepository;

    final InstructorRepository instructorRepository;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(MemberFixture.createActiveMember());
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));
    }

    @Test
    void saveAndFindId() {
        Course course = CourseFixture.createCourse(instructor);
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
                        CourseFixture.createCourse(instructor, "Hello Spring "),
                        CourseFixture.createCourse(instructor, "Clean Spring 2"),
                        CourseFixture.createCourse(instructor, "Clean Code 1"))
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
        Member member2 = memberRepository.save(MemberFixture.createActiveMember());
        Instructor instructor2 = instructorRepository.save(InstructorFixture.createActiveInstructor(member2));

        Course course1 = courseRepository.save(CourseFixture.createCourse(instructor));
        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor2));

        assertThat(courseRepository.findByInstructor(instructor)).singleElement().isEqualTo(course1);
        assertThat(courseRepository.findByInstructor(instructor2)).singleElement().isEqualTo(course2);
    }

    @Test
    void uniqueTitleAndInstructor() {
        Member member2 = memberRepository.save(MemberFixture.createActiveMember());
        Instructor instructor2 = instructorRepository.save(InstructorFixture.createActiveInstructor(member2));

        Course course = CourseFixture.createCourse(instructor, "Hell Spring");
        courseRepository.save(course);

        Course course2 = CourseFixture.createCourse(instructor2, "Hell Spring");
        Course course3 = CourseFixture.createCourse(instructor, "Hell Spring");

        assertThat(courseRepository.save(course2)).isEqualTo(course2);

        assertThatThrownBy(() -> courseRepository.save(course3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}