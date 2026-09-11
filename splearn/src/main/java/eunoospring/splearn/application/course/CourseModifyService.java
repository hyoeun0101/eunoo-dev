package eunoospring.splearn.application.course;

import eunoospring.splearn.application.course.provided.CourseCreateRequest;
import eunoospring.splearn.application.course.provided.CourseCreator;
import eunoospring.splearn.application.course.provided.CourseFinder;
import eunoospring.splearn.application.course.provided.CoursePublisher;
import eunoospring.splearn.application.course.provided.CourseUpdateRequest;
import eunoospring.splearn.application.course.provided.CourseValidator;
import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.application.instructor.provided.InstructorFinder;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.support.ValidatedApplicationService;
import eunoospring.splearn.support.exception.ValidationException;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator, CoursePublisher {
    private final CourseRepository courseRepository;
    private final InstructorFinder instructorFinder;
    private final CourseValidator courseValidator;
    private final CourseFinder courseFinder;

    @Override
    public Course create(CourseCreateRequest request) throws ValidationException {
        Instructor instructor = instructorFinder.find(request.instructorId());

        courseValidator.validateForCreate(instructor, request);

        Course course = new Course(instructor, request.title(), request.description());
        return courseRepository.save(course);
    }

    @Override
    public Course updateInfo(Long courseId, CourseUpdateRequest request) throws ValidationException {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForUpdate(course, request);

        course.updateInfo(request.toInfo());

        return courseRepository.save(course);
    }

    @Override
    public Course submitForReview(Long courseId) {

        Course course = courseFinder.find(courseId);

        courseValidator.validateForReview(course);
        return null;
    }

    @Override
    public Course publish(Long courseId) {
        return null;
    }

    @Override
    public Course archive(Long courseId) {
        return null;
    }
}
