package eunoospring.splearn.application.enrollment.provided;

import eunoospring.splearn.domain.enrollment.Enrollment;

public interface Enroller {
    Enrollment enroll(Long memberId, Long courseId);

    Enrollment startStudying(Long enrollmentId);

    Enrollment complete(Long enrollmentId);
}
