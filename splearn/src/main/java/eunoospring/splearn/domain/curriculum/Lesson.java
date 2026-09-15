package eunoospring.splearn.domain.curriculum;

import eunoospring.splearn.domain.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson extends AbstractEntity {

    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Section section;

    public Lesson(Section section, String title) {
         this.section = section;
        this.title = Objects.requireNonNull(title );
    }
}
