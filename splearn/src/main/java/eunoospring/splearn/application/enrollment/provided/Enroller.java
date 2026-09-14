package eunoospring.splearn.application.enrollment.provided;

import eunoospring.splearn.domain.enrollment.Enrollment;
import jakarta.validation.Valid;

public interface Enroller {
    Enrollment enroll(@Valid EnrollRequest request);

    Enrollment startStudying(Long enrollmentId);

    Enrollment complete(Long enrollmentId);
}
