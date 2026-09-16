package eunoospring.splearn.domain.curriculum;


import eunoospring.splearn.domain.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Section extends AbstractEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curriculum curriculum;

    private String title;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Lesson> lessons = new ArrayList<>();


    Section(Curriculum curriculum, String title) {
        this.curriculum = curriculum;
        this.title = Objects.requireNonNull(title);
    }

    Lesson addLesson(String title) {
        Lesson lesson = new Lesson(this, title);
        lessons.add(lesson);

        return lesson;
    }

    void updateTitle(String title) {
        this.title = title;
    }

    void updateLessonTitle(int lessonIndex, String title) {
        this.lessons.get(lessonIndex).updateTitle(title);
    }

    public Lesson removeLesson(int lessonIndex) {
        return lessons.remove(lessonIndex);
    }

    public void moveAllLessonTo(Section target, int insertIndex) {
        while(!this.lessons.isEmpty()) {
            target.addLesson(insertIndex++, this.lessons.getFirst());
            this.lessons.removeFirst();
        }
    }

    public void addLesson(int index, Lesson lesson) {
        lesson.moveTo(this);
        lessons.add(index, lesson);
    }
}
