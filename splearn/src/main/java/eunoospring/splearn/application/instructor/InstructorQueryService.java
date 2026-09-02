package eunoospring.splearn.application.instructor;

import eunoospring.splearn.application.instructor.provided.InstructorFinder;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.domain.instructor.Instructor;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class InstructorQueryService implements InstructorFinder {
    private final InstructorRepository instructorRepository;

    @Override
    public Instructor find(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강사입니다: " + instructorId));
    }

    @Override
    public Optional<Instructor> findByMember(Long memberId) {
        return instructorRepository.findByMemberId(memberId);
    }
}
