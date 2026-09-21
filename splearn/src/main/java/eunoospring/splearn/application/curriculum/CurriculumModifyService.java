package eunoospring.splearn.application.curriculum;

import eunoospring.splearn.application.course.provided.CourseFinder;
import eunoospring.splearn.application.curriculum.provided.CurriculumCoordinator;
import eunoospring.splearn.application.curriculum.provided.CurriculumFinder;
import eunoospring.splearn.application.curriculum.required.CurriculumRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.curriculum.Curriculum;
import eunoospring.splearn.domain.curriculum.InvalidCurriculumException;
import eunoospring.splearn.domain.curriculum.Section;
import eunoospring.splearn.support.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CurriculumModifyService implements CurriculumCoordinator {
    private final CurriculumFinder curriculumFinder;
    private final CurriculumRepository curriculumRepository;
    private final CourseFinder courseFinder;
    @Override
    public Curriculum create(Long courseId) {
        Course course = courseFinder.find(courseId);

        Curriculum curriculum = new Curriculum(course);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum addSection(Long curriculumId, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.addSection(title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum addSection(Long curriculumId, int sectionIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.addSection(sectionIndex, title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum addLesson(Long curriculumId, int sectionIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.addLesson(sectionIndex, title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum updateSectionTitle(Long curriculumId, int sectionIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.updateSectionTitle(sectionIndex, title);
        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum updateLessonTitle(Long curriculumId, int sectionIndex, int lessonIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.updateLessonTitle(sectionIndex, lessonIndex, title);
        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum removeSection(Long curriculumId, int sectionIndex) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.removeSection(sectionIndex);
        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.removeLesson(sectionIndex, lessonIndex);
        return curriculumRepository.save(curriculum);

    }

    @Override
    public Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex,
                                 int toLessonIndex) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.moveLesson(fromSectionIndex, fromLessonIndex, toSectionIndex, toLessonIndex);
        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum validate(Long curriculumId) throws InvalidCurriculumException {
        Curriculum curriculum = curriculumFinder.find(curriculumId);
        curriculum.validate();
        return curriculum;
    }

    @Override
    public Long createCurriculum(Course course) {
        Curriculum curriculum = new Curriculum(course);
        return curriculumRepository.save(curriculum).getId();
    }
}
