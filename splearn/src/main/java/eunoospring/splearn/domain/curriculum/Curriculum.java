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
import org.springframework.util.Assert;

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
        return sections.get(sectionIndex).addLesson(title);
    }

    public Section updateSectionTitle(int sectionIndex, String title) {
        Section section = sections.get(sectionIndex);
        section.updateTitle(title);
        return section;
    }

    public void updateLessonTitle(int sectionIndex, int lessonIndex, String title) {
        Section section = sections.get(sectionIndex);
        section.updateLessonTitle(lessonIndex, title);
    }

    public void removeLesson(int sectionIndex, int lessonIndex) {
        sections.get(sectionIndex).removeLesson(lessonIndex);
    }

    public List<Lesson> allLessons() {
        return this.sections.stream().flatMap(section -> section.getLessons().stream())
                .toList();
    }

    public void removeSection(int sectionIndex) {

        Assert.state(this.sections.size() > 1, "마지막 남은 섹션은 삭제할 수 없습니다.");

        Section removed = this.sections.remove(sectionIndex);

        if (sectionIndex == 0) {
            Section next = this.sections.get(0);
            removed.moveAllLessonTo(next, 0);
        }
        else {
            Section previous = this.sections.get(sectionIndex - 1);
            removed.moveAllLessonTo(previous, previous.getLessons().size());
        }
    }

    public void moveLesson(int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex) {

        Section fromSection = this.sections.get(fromSectionIndex);
        Section toSection = this.sections.get(toSectionIndex);

        toSection.addLesson(toLessonIndex, fromSection.removeLesson(fromLessonIndex));
    }

    public void validate() {
        if (this.sections.isEmpty()) throw new InvalidCurriculumException("최소한 하나의 섹션이 필요합니다.");

        this.sections.forEach(section -> {
            if (section.getLessons().isEmpty()) throw new InvalidCurriculumException("수업이 없는 섹션은 허용되지 않습니다.");
        });
    }
}
