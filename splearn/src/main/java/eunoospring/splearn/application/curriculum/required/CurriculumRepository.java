package eunoospring.splearn.application.curriculum.required;

import eunoospring.splearn.domain.curriculum.Curriculum;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface CurriculumRepository extends Repository<Curriculum, Long> {
    Curriculum save(Curriculum curriculum);

    Optional<Curriculum> findById(Long curriculumId);

    // fetch join 사용하기
    @EntityGraph(attributePaths = {"sections", "sections.lessons"})
    Optional<Curriculum> findWithSectionById(Long curriculumId);

    Optional<Curriculum> findByCourseId(Long courseId);
}
