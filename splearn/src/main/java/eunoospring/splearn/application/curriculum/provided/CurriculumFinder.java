package eunoospring.splearn.application.curriculum.provided;

import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.Lesson;

public interface CurriculumFinder {
    Curriculum find(Long curriculumId);

    Curriculum findByCourse(Long courseId);

    Lesson firstLession(Long curriculumId);

    Lesson nextLesson(Long curriculumId, Long lessonId);
}
