package eunoospring.splearn.application.instructor;

import eunoospring.splearn.application.instructor.provided.DuplicateInstructorApplicationException;
import eunoospring.splearn.application.instructor.provided.InstructorApplication;
import eunoospring.splearn.application.instructor.provided.InstructorApplyRequest;
import eunoospring.splearn.application.instructor.provided.InstructorFinder;
import eunoospring.splearn.application.instructor.required.InstructorRepository;
import eunoospring.splearn.application.member.provided.MemberAuthenticator;
import eunoospring.splearn.application.member.provided.MemberFinder;
import eunoospring.splearn.domain.instructor.Instructor;
import eunoospring.splearn.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class InstructorModifyService implements InstructorApplication {
    private final InstructorRepository instructorRepository;

    private final InstructorFinder instructorFinder;

    private final MemberFinder memberFinder;
    private final MemberAuthenticator memberAuthenticator;

    @Override
    public Instructor apply(InstructorApplyRequest request) {
        Member member = memberFinder.find(request.memberId());

        checkDuplicateApplication(member);

        Instructor instructor = Instructor.apply(member);

        return instructorRepository.save(instructor);
    }

    private void checkDuplicateApplication(Member member) {
        if (instructorRepository.findByMemberId(member.getId()).isPresent()) {
            throw new DuplicateInstructorApplicationException("회원은 중복해서 강사 신청을 할 수 없습니다.");
        }
    }

    @Override
    public Instructor approve(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);

        instructor.approve();

        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor reject(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);

        instructor.reject();

        return instructorRepository.save(instructor);
    }
}
