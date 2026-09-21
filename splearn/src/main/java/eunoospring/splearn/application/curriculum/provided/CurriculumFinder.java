package eunoospring.splearn.application.curriculum.provided;

import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.Lesson;
import java.util.Optional;

public interface CurriculumFinder {
    Curriculum find(Long curriculumId);

    Curriculum findWithSections(Long curriculumId);

    Curriculum findByCourse(Long courseId);

    Optional<Lesson> firstLession(Long curriculumId);

    Optional<Lesson> nextLesson(Long curriculumId, Long lessonId);
}
