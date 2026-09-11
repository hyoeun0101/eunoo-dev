package eunoospring.splearn.application.course.provided;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.support.exception.ValidationException;

public interface CourseValidator {
    void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException;

    void validateForUpdate(Course course, CourseUpdateRequest request);

    void validateForReview(Course course);

    void validateForPublish(Course course);

    void validateForArchive(Course course);

}
