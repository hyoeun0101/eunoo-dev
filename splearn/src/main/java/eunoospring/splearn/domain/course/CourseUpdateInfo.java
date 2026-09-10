package eunoospring.splearn.domain.course;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record CourseUpdateInfo(
        @Size(min = 2, max = 100) String title,
        @Nullable String description
) {
}
