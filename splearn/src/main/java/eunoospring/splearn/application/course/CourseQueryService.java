package eunoospring.splearn.application.course;

import eunoospring.splearn.application.course.provided.CourseFinder;
import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.support.ApplicationService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseQueryService implements CourseFinder {
    private final CourseRepository courseRepository;

    @Override
    public Course find(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다: " + courseId));
    }

    @Override
    public List<Course> findByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }

    @Override
    public List<Course> findByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }
}
