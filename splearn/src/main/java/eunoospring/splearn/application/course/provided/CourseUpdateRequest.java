package eunoospring.splearn.application.course.provided;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record CourseUpdateRequest(
        @Size(min = 2, max = 100) String title,
        @Nullable String description
) {
}
