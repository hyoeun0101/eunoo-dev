package eunoospring.splearn.application.course.provided;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;

public record CourseCreateRequest(
        @Nonnull Long instructorId,
        @Size(min = 2, max = 100) String title,
        @Size(max = 500) String description
) {
}
