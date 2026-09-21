package eunoospring.splearn.application.curriculum.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class CurriculumFinderTest extends BaseApplicationServiceTest {
    private final CurriculumFinder curriculumFinder;
    private final CurriculumCoordinator curriculumCoordinator;
    @Test
    void find() {
        Curriculum curriculum = prepareCurriculum();

        Curriculum found = curriculumFinder.find(curriculum.getId());

        assertThat(found).isEqualTo(curriculum);
    }

    @Test
    void findWithSection() {
        Curriculum curriculum = prepareCurriculum();

        curriculumCoordinator.addSection(curriculum.getId(), "S1");

        Curriculum found = curriculumFinder.findWithSections(curriculum.getId());

        assertThat(found).isEqualTo(curriculum);
        assertThat(found.getSections().get(0).getTitle()).isEqualTo(curriculum.getSections().get(0).getTitle());
    }

    private Curriculum prepareCurriculum() {
        Course course = preparePublishedCourse();
        Curriculum curriculum = curriculumCoordinator.create(course.getId());
        return curriculum;
    }






}