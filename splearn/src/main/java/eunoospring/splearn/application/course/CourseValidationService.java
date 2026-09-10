package eunoospring.splearn.application.course;

import eunoospring.splearn.application.course.provided.CourseCreateRequest;
import eunoospring.splearn.application.course.provided.CourseValidator;
import eunoospring.splearn.application.course.required.CourseRepository;
import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.support.ApplicationService;
import eunoospring.splearn.support.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseValidationService implements CourseValidator {

    private final CourseRepository courseRepository;

    @Override
    public void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException {
        instructor.ensureActive();

        List<String> errors = new ArrayList<>();

        checkTitleDuplication(instructor, createRequest.title(), errors);

        checkBannedWords(createRequest.title(), errors);
        checkBannedWords(createRequest.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void checkBannedWords(String text, List<String> errors) {

    }

    private void checkTitleDuplication(Instructor instructor, String title, List<String> errors) {

        if (courseRepository.findByInstructorAndTitle(instructor, title).isPresent()) {
            errors.add("이미 사용 중인 강의명입니다. (title="+ title + ")");
        }

    }
}
