package eunoospring.splearn.application.course.provided;

import eunoospring.splearn.domain.course.Course;
import jakarta.validation.Valid;

/**
 * 강의 등록 관련 기능
 */
public interface CourseCreator {
    Course create(@Valid CourseCreateRequest request);

    Course updateInfo(Long courseId, @Valid CourseUpdateRequest request);

}

