package eunoospring.splearn.application.curriculum.required;

import eunoospring.splearn.domain.curriculum.Section;
import org.springframework.data.repository.Repository;

public interface SectionRepository extends Repository<Section, Long> {
    void delete(Section section);
}
