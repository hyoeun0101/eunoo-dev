package eunoospring.splearn.domain.course;

import static org.springframework.util.Assert.state;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import eunoospring.splearn.domain.AbstractEntity;
import eunoospring.splearn.domain.instructor.Instructor;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "UK_COURSE_INSTRUCTOR_TITLE",
        columnNames = {"instructor_id", "title"}))
@Getter
@ToString(callSuper = true, exclude = {"instructor"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends AbstractEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Instructor instructor;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private CourseDetail detail;

    public Course(Instructor instructor, String title, @Nullable String description) {
        instructor.ensureActive();

        this.instructor = instructor;
        this.title = title;
        this.status = CourseStatus.DRAFT;

        this.detail = new CourseDetail(description);
    }

    public void submitForReview() {
        state(status == CourseStatus.DRAFT, "DRAFT 상태가 아닙니다.");
        state(StringUtils.hasText(detail.getDescription()), "강의 소개가 등록되지 않았습니다.");

        this.status = CourseStatus.IN_REVIEW;
    }

    public void publish() {
        state(status == CourseStatus.IN_REVIEW, "IN_REVIEW 상태가 아닙니다.");

        this.status = CourseStatus.PUBLISHED;
        this.detail.publish();
    }

    public void archive() {
        state(status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아닙니다.");

        this.status = CourseStatus.ARCHIVED;
        this.detail.archive();
    }

    public boolean isPublished() {
        return status == CourseStatus.PUBLISHED;
    }

    public void ensurePublished() {
        state(status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아닙니다.");
    }

    public void updateInfo(CourseUpdateInfo updateInfo) {
        this.title = updateInfo.title();
        this.detail.updateInfo(updateInfo);

    }
}
