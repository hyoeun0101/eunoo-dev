package eunoospring.splearn.application.instructor.provided;

import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.member.Member;
import java.util.Optional;

/**
 * 강사 조회
 */
public interface InstructorFinder {
    Instructor find(Long instructorId);

    default Optional<Instructor> findByMember(Member member) {
        return findByMember(member.getId());
    }

    Optional<Instructor> findByMember(Long memberId);
}
