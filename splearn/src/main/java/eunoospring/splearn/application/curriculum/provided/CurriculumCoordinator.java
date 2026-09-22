package eunoospring.splearn.application.curriculum.provided;

import eunoospring.splearn.application.course.required.CurriculumCreator;
import eunoospring.splearn.application.course.required.CurriculumValidator;
import eunoospring.splearn.domain.curriculum.Curriculum;

public interface CurriculumCoordinator extends CurriculumCreator, CurriculumValidator {
    Curriculum addSection(Long curriculumId, String title);

    Curriculum addSection(Long curriculumId, int sectionIndex, String title);

    Curriculum addLesson(Long curriculumId, int sectionIndex, String title);

    Curriculum updateSectionTitle(Long curriculumId, int sectionIndex, String title);

    Curriculum updateLessonTitle(Long curriculumId, int sectionIndex, int lessonIndex, String title);

    Curriculum removeSection(Long curriculumId, int sectionIndex);

    Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex);

    Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex);
}
