package eunoospring.splearn.application.course.required;

import eunoospring.splearn.domain.curriculum.InvalidCurriculumException;

public interface CurriculumValidator {
    void validate(Long courseId) throws InvalidCurriculumException;
}
