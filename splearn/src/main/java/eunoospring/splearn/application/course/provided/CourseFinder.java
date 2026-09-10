package eunoospring.splearn.application.course.provided;

import eunoospring.splearn.domain.course.Course;
import java.util.List;

/**
 * 강의 조회 관련 기
 */
public interface CourseFinder {
    Course find(Long courseId);

    List<Course> findByTitle(String keyword);

    List<Course> findByInstructor(Long instructorId);
}
