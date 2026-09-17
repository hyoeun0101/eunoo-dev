package eunoospring.splearn.application.curriculum.required;

import static eunoospring.splearn.domain.curriculum.LessonContent.lesson;
import static eunoospring.splearn.domain.curriculum.SectionContent.section;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import eunoospring.splearn.application.curriculum.provided.CurriculumFinder;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.CurriculumFixture;
import eunoospring.splearn.domain.curriculum.SectionContent;
import eunoospring.splearn.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
@RequiredArgsConstructor
class CurriculumRepositoryTest extends BaseRepositoryTest {

    final private CurriculumRepository curriculumRepository;

    @Test
    void saveAndFindById() {
        Long curriculumId = saveCurriculum().getId();

        em.flush();
        em.clear();

        Statistics statistics = prepareStatistics();

        Curriculum found = curriculumRepository.findById(curriculumId).orElseThrow();

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
        assertThat(found.getId()).isEqualTo(curriculumId);
        assertThat(SectionContent.from(found)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"), lesson("L3"))
        );

        found.allLessons();
        // lazy 로딩... N+1 문제...
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(4);


    }

    private Curriculum saveCurriculum() {
        Curriculum curriculum = new Curriculum(this.prepareCourse());

        curriculum = curriculumRepository.save(curriculum);
        curriculum.addSection("S0");
        curriculum.addLesson(0, "L0");
        curriculum.addLesson(0, "L1");

        curriculum.addSection("S1");
        curriculum.addLesson(1, "L2");
        curriculum.addLesson(1, "L3");
        return curriculum;
    }

    @Test
    void saveAndFindWithSectionsById() {

    }

    /**
     * 쿼리 최적화할 때, 쿼리 실행 횟수
     * N+1 해결할때.
     * @return
     */
    private Statistics prepareStatistics() {
        Statistics statistics = em.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
        return statistics;
    }
}