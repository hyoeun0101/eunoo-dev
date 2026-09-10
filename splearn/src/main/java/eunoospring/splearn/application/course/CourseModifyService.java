package eunoospring.splearn.application.course;

import eunoospring.splearn.application.course.provided.CourseCreateRequest;
import eunoospring.splearn.application.course.provided.CourseCreator;
import eunoospring.splearn.application.course.provided.CoursePublisher;
import eunoospring.splearn.application.course.provided.CourseUpdateRequest;
import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.application.instructor.provided.InstructorFinder;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.support.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator {
    private final CourseRepository courseRepository;
    private final InstructorFinder instructorFinder;

    @Override
    public Course create(CourseCreateRequest request) {
        // instructor 찾기.
        Instructor instructor = instructorFinder.find(request.instructorId());
        if (instructor == null) {
            throw new IllegalArgumentException("");
        }
        //validate
        courseRepository.find
        //save
        return null;
    }

    @Override
    public Course updateInfo(Long courseId, CourseUpdateRequest request) {
        return null;
    }
}
