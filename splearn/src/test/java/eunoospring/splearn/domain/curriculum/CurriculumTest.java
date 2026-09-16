package eunoospring.splearn.domain.curriculum;

import static eunoospring.splearn.domain.curriculum.LessonContent.lesson;
import static eunoospring.splearn.domain.curriculum.SectionContent.section;
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
        Section section2 = curriculum.addSection("Section 2");

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
        curriculum.addSection("S1");
        curriculum.addSection("S2");

        curriculum.addLesson(0, "L1");
        curriculum.addLesson(0, "L2");
        curriculum.addLesson(1, "L3");

        assertThat(curriculum.allLessons()).extracting(Lesson::getTitle)
                .containsExactly("L1", "L2", "L3");

        curriculum.removeLesson(0, 0);

        assertThat(curriculum.allLessons()).extracting(Lesson::getTitle)
                .containsExactly("L2", "L3");

        curriculum.removeLesson(1, 0);

        assertThat(curriculum.allLessons()).extracting(Lesson::getTitle)
                .containsExactly("L2");
    }

    @Test
    void removeSection() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        curriculum.addSection("S0");
        curriculum.addSection("S1");
        curriculum.addSection("S2");

        curriculum.addLesson(0, "L0");
        curriculum.addLesson(0, "L1");
        curriculum.addLesson(1, "L2");
        curriculum.addLesson(1, "L3");
        curriculum.addLesson(2, "L4");
        curriculum.addLesson(2, "L5");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"), lesson("L3")),
                section("S2", lesson("L4"), lesson("L5"))
        );

        curriculum.removeSection(2);

        // 삭제한 section의 lesson은 앞의 section의 뒤로 이동.
        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"), lesson("L3"), lesson("L4"), lesson("L5"))
        );

        // 삭제한 section이 첫 번째이면, lesson은 다음의 section의 앞으로 이동.
        curriculum.removeSection(0);

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S1", lesson("L0"), lesson("L1"), lesson("L2"),
                        lesson("L3"), lesson("L4"), lesson("L5"))
        );

        // 섹션이 하나면 삭제할 수 없다.
        assertThatThrownBy(() -> curriculum.removeSection(0)).isInstanceOf(IllegalStateException.class);
    }


    @Test
    void moveLesson() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        curriculum.addSection("S0");
        curriculum.addSection("S1");
        curriculum.addSection("S2");

        curriculum.addLesson(0, "L0");
        curriculum.addLesson(0, "L1");
        curriculum.addLesson(0, "L2");
        curriculum.addLesson(1, "L3");
        curriculum.addLesson(1, "L4");
        curriculum.addLesson(2, "L5");
        curriculum.addLesson(2, "L6");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1"), lesson("L2")),
                section("S1", lesson("L3"), lesson("L4")),
                section("S2", lesson("L5"), lesson("L6"))
        );

        curriculum.moveLesson(0, 0, 0, 1);

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L1"), lesson("L0"), lesson("L2")),
                section("S1", lesson("L3"), lesson("L4")),
                section("S2", lesson("L5"), lesson("L6"))
        );
    }

    @Test
    void validate() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        // 최소한 하나의 섹션은 필요하다
        assertThatThrownBy(curriculum::validate).isInstanceOf(InvalidCurriculumException.class);

        curriculum.addSection("S0");
        curriculum.addLesson(0, "L0");

        curriculum.validate();

        curriculum.addSection("S1");
        curriculum.addLesson(1, "L1");
        
        curriculum.validate();

        // lesson이 없는 섹션은 검증 실패
        curriculum.removeLesson(1, 0);
        assertThatThrownBy(curriculum::validate).isInstanceOf(InvalidCurriculumException.class);
    }


}