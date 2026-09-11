package eunoospring.splearn.application.course.provided;

import eunoospring.splearn.domain.course.CourseUpdateInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.query.PreprocessedQuery;

public record CourseUpdateRequest(
        @Size(min = 2, max = 100) String title,
        @Nullable String description
) {
    public CourseUpdateInfo toInfo() {
        return new CourseUpdateInfo(this.title, this.description);
    }
}
