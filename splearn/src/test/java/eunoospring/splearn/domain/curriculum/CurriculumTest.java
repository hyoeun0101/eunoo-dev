package eunoospring.splearn.domain.curriculum;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

class CurriculumTest {

    @Test
    void create() {
        Course course = CourseFixture.createCourse();

        Curriculum curriculum = new Curriculum(course);

        assertThat(curriculum.getCourse()).isEqualTo(course);

        assertThatThrownBy(() -> new Curriculum(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addSection() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();

        curriculum.addSection("Section 1");

        assertThat(curriculum.getSections()).hasSize(1);
        assertThat(curriculum.getSections()).extracting(Section::getTitle).containsExactly("Section 1");
        assertThatThrownBy(() -> curriculum.addSection(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addSectionWithIndex() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();

        Section section1 = curriculum.addSection("Section 1");
        Section section2 = curriculum.addSection("Section 2");

        Section section1_1 = curriculum.addSection(1, "Section 1_1");

        assertThat(curriculum.getSections()).containsExactly(section1, section1_1, section2);

        assertThatThrownBy(() -> curriculum.addSection(5, "Section 5"))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void addLesson() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section section1 = curriculum.addSection("Section 1");
        Section section2 = curriculum.addSection("Section 2") ;

        Lesson lesson1 = curriculum.addLesson(0, "Lesson 1");
        Lesson lesson2 = curriculum.addLesson(0, "Lesson 2");

        assertThat(section1.getLessons()).containsExactly(lesson1, lesson2);

        Lesson lesson3 = curriculum.addLesson(1, "Lesson 3");

        assertThat(section2.getLessons()).containsExactly(lesson3);
    }

    @Test
    void updateSectionTitle() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section section1 = curriculum.addSection("Section 1");
        Section section2 = curriculum.addSection("Section 2");

        curriculum.updateSectionTitle(0, "S1");
        curriculum.updateSectionTitle(1, "S2");

        assertThat(section1.getTitle()).isEqualTo("S1");
        assertThat(section2.getTitle()).isEqualTo("S2");
        assertThat(curriculum.getSections()).extracting(Section::getTitle).containsExactly("S1", "S2");
    }

    @Test
    void updateLessonTitle() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        curriculum.addSection("Section 1");
        curriculum.addSection("Section 2");

        curriculum.addLesson(0, "Lesson 1_1");
        curriculum.addLesson(0, "Lesson 1_2");
        curriculum.addLesson(1, "Lesson 2_1");

        curriculum.updateLessonTitle(0, 0, "L1_1");
        curriculum.updateLessonTitle(1, 0, "L2_1");

        List<Lesson> lessons = curriculum.getSections().stream().flatMap(section -> section.getLessons().stream())
                .toList();
        assertThat(lessons).extracting(Lesson::getTitle).containsExactly("L1_1", "Lesson 1_2", "L2_1");
    }

    @Test
    void removeLesson() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        curriculum.addSection("Section 1");
        curriculum.addSection("Section 2");

        curriculum.addLesson(0, "Lesson 1_1");
        curriculum.addLesson(0, "Lesson 1_2");
        curriculum.addLesson(1, "Lesson 2_1");

        curriculum.removeLesson(0, 0);
    }

}