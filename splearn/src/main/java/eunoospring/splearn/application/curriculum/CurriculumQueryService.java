package eunoospring.splearn.application.curriculum;

import eunoospring.splearn.application.curriculum.provided.CurriculumFinder;
import eunoospring.splearn.application.curriculum.required.CurriculumRepository;
import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.Lesson;
import eunoospring.splearn.support.ApplicationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CurriculumQueryService implements CurriculumFinder {
    private final CurriculumRepository curriculumRepository;

    @Override
    public Curriculum find(Long curriculumId) {
        return curriculumRepository.findById(curriculumId).orElseThrow(
                () -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. curriculumId=" + curriculumId)
        );
    }

    @Override
    public Curriculum findWithSections(Long curriculumId) {
        return curriculumRepository.findWithSectionById(curriculumId).orElseThrow(
                () -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. curriculumId=" + curriculumId)
        );
    }

    @Override
    public Curriculum findByCourse(Long courseId) {
        return curriculumRepository.findByCourseId(courseId).orElseThrow(
                () -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. courseId=" + courseId)
        );
    }

    @Override
    public Optional<Lesson> firstLession(Long curriculumId) {
        return this.findWithSections(curriculumId).firstLesson();
    }

    @Override
    public Optional<Lesson> nextLesson(Long curriculumId, Long lessonId) {
        return this.findWithSections(curriculumId).nextLesson(lessonId);
    }
}
