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
class CurriculumCoordinatorTest extends BaseApplicationServiceTest {

    private final CurriculumCoordinator curriculumCoordinator;
    
    @Test
    void createCurriculum() {
        Curriculum curriculum = curriculumCoordinator.create(preparePublishedCourse().getId());

        assertThat(curriculum.getId()).isNotNull();
    }
    
    @Test
    void addSection() {
        Curriculum curriculum = curriculumCoordinator.create(preparePublishedCourse().getId());
        curriculum = curriculumCoordinator.addSection(curriculum.getId(), "S1");

        assertThat(curriculum.getSections()).hasSize(1);
    }

}