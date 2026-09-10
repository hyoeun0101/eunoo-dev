package eunoospring.splearn.application.course.required;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.instructor.Instructor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CourseRepository extends Repository<Course, Long> {
    Course save(Course course);

    Optional<Course> findById(Long id);

    List<Course> findByTitleContaining(String keyword);

    default List<Course> findByInstructor(Instructor instructor) {
        return findByInstructorId(instructor.getId());
    }

    List<Course> findByInstructorId(Long instructorId);

    Optional<Course> findByInstructorAndTitle(Instructor instructor, String title);
}
