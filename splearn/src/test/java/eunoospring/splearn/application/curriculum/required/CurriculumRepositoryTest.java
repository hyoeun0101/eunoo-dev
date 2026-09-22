package eunoospring.splearn.application.curriculum.required;

import static eunoospring.splearn.domain.curriculum.LessonContent.lesson;
import static eunoospring.splearn.domain.curriculum.SectionContent.section;
import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.Lesson;
import eunoospring.splearn.domain.curriculum.Section;
import eunoospring.splearn.domain.curriculum.SectionContent;
import eunoospring.splearn.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
class CurriculumRepositoryTest extends BaseRepositoryTest {

    final private CurriculumRepository curriculumRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Test
    void saveAndFindById() {
        Long curriculumId = saveCurriculum().getId();

        // em.flush()할 때 insert section, insert lesson 실행됨.
        em.flush();
        em.clear();

        Statistics statistics = prepareStatistics();

        // Lazy 로딩으로 인해 select curriculum만 실행
        Curriculum found = curriculumRepository.findById(curriculumId).orElseThrow();

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1); // select curriculum
        assertThat(found.getId()).isEqualTo(curriculumId);
        assertThat(SectionContent.from(found)).containsExactly(
                section("S1", lesson("L1"), lesson("L2")),
                section("S2", lesson("L3"))
        );

        // 여기서 select section, select lesson 실행됨.
        found.allLessons();

        // select section, select lesson => N+1문제 발생.
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(4);


    }

    @Test
    void saveAndFindWithSectionsById() {
        Long curriculumId = saveCurriculum().getId();

        // em.flush()할 때 insert section, insert lesson 실행됨.
        em.flush();
        em.clear();

        Statistics statistics = prepareStatistics();

        // select curriculum left join section, lesson으로 한번에 조회
        Curriculum found = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();

        System.out.println("found="+found.getSections());

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1); // select curriculum
        assertThat(found.getId()).isEqualTo(curriculumId);
        assertThat(SectionContent.from(found)).containsExactly(
                section("S1", lesson("L1"), lesson("L2")),
                section("S2", lesson("L3"))
        );

        // 여기서 select section, select lesson 실행됨.
        found.allLessons();

        // select section, select lesson => N+1문제 발생.
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    private Curriculum saveCurriculum() {
        Curriculum curriculum = new Curriculum(this.prepareCourse());
        System.out.println("=========prepare=========");

        curriculum = curriculumRepository.save(curriculum);
        curriculum.addSection("S1");
        curriculum.addLesson(0, "L1");
        curriculum.addLesson(0, "L2");

        curriculum.addSection("S2");
        curriculum.addLesson(1, "L3");
        System.out.println("=====prepare=====");
        return curriculum;
    }

    @Test
    void removeLesson() {
        Long curriculumId = saveCurriculum().getId();
        Curriculum curriculum = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();

        Lesson lesson = curriculum.removeLesson(0, 0);
        lessonRepository.delete(lesson);

        curriculumRepository.save(curriculum);

        em.flush();
        em.clear();

        curriculum = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();
        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S1", lesson("L2")),
                section("S2", lesson("L3"))
        );
    }

    @Test
    void removeSection() {
        Long curriculumId = saveCurriculum().getId();
        Curriculum curriculum = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();

        Section section = curriculum.removeSection(0);
        sectionRepository.delete(section);

        em.flush();
        em.clear();

        curriculum = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();
        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S2", lesson("L1"), lesson("L2"), lesson("L3"))
        );
    }

    @Test
    void moveLesson() {
        Long curriculumId = saveCurriculum().getId();
        Curriculum curriculum = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();

        curriculum.moveLesson(0 ,0 ,1, 1);

        em.flush();
        em.clear();

        curriculum = curriculumRepository.findWithSectionById(curriculumId).orElseThrow();
        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S1", lesson("L2")),
                section("S2", lesson("L3"), lesson("L1"))
        );
    }

}