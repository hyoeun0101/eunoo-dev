package eunoospring.splearn.application.instructor.required;

import eunoospring.splearn.domain.instructor.Instructor;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface InstructorRepository extends Repository<Instructor, Long> {
    Instructor save(Instructor instructor);

    Optional<Instructor> findById(Long id);

    Optional<Instructor> findByMemberId(Long memberId);
}
