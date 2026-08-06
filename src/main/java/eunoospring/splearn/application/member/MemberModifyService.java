package eunoospring.splearn.application.member;

import eunoospring.splearn.application.member.provided.MemberFinder;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.application.member.required.EmailSender;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.member.DuplicationEmailException;
import eunoospring.splearn.domain.shared.Email;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberRegisterRequest;
import eunoospring.splearn.domain.member.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        checkDuplicationEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    private void checkDuplicationEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicationEmailException("이미 사용중인 이메일입니다: " + registerRequest.email());
        }
    }

    @Override
    public Member activate(Long id) {
        Member member = memberFinder.find(id);

        member.activate();

        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }
}
