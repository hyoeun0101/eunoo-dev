package eunoospring.splearn.domain.curriculum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseFixture;
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
        Section section2 = curriculum.addSection("Section 2");

        Lesson lesson = curriculum.addLesson(0, "Lesson 1");

        assertThat(section1);
    }

}