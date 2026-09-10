package eunoospring.splearn.domain.course;

import eunoospring.splearn.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDetail extends AbstractEntity {

    @Column(length = 500)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    private LocalDateTime archivedAt;

    CourseDetail(String description) {
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    void publish() {
        this.publishedAt = LocalDateTime.now();
    }

    void archive() {
        this.archivedAt = LocalDateTime.now();
    }

    void updateInfo(CourseUpdateInfo updateInfo) {
        this.description = updateInfo.description();
    }
}
