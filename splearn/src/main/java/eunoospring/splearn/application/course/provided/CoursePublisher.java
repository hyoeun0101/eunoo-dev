package eunoospring.splearn.application.course.provided;

import eunoospring.splearn.domain.course.Course;

/**
 * 강의 공개 관련 기
 */
public interface CoursePublisher {
    Course submitForReview(Long courseId);

    Course publish(Long courseId);

    Course archive(Long courseId);
}
