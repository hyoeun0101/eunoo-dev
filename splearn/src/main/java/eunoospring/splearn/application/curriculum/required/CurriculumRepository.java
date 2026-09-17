package eunoospring.splearn.application.curriculum.required;

import eunoospring.splearn.domain.curriculum.Curriculum;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CurriculumRepository extends Repository<Curriculum, Long> {
    Curriculum save(Curriculum curriculum);

    Optional<Curriculum> findById(Long curriculumId);
}
