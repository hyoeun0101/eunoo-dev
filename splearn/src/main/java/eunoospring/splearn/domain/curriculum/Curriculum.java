package eunoospring.splearn.domain.curriculum;

import eunoospring.splearn.domain.AbstractEntity;
import eunoospring.splearn.domain.course.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum extends AbstractEntity {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Course course;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Curriculum(Course course) {
        this.course = Objects.requireNonNull(course);
    }

    public Section addSection(String title) {
        Section section = new Section(this, title);
        sections.add(section);
        return section;
    }

    public Section addSection(int index, String title) {
        Section section = new Section(this, title);
        sections.add(index, section);
        return section;
    }

    public Lesson addLesson(int sectionIndex, String title) {


        return null;
    }
}
