package eunoospring.splearn.application.curriculum.provided;

import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.InvalidCurriculumException;

public interface CurriculumCoordinator {
    Curriculum create(Long courseId);

    Curriculum addSection(Long curriculumId, String title);

    Curriculum addSection(Long curriculumId, int sectionIndex, String title);

    Curriculum addLesson(Long curriculumId, int sectionIndex, String title);

    Curriculum updateSectionTitle(Long curriculumId, int sectionIndx, String title);

    Curriculum updateLessonTitle(Long curriculumId, int sectoinIndex, int lessonIndex, String title);

    Curriculum removeSection(Long curriculumId, int sectionIndex);

    Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex);

    Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex);

    Curriculum validate(Long curriculumId) throws InvalidCurriculumException;
}
