package eunoospring.splearn.application;

import eunoospring.splearn.application.provided.MemberFinder;
import eunoospring.splearn.application.provided.MemberRegister;
import eunoospring.splearn.application.required.EmailSender;
import eunoospring.splearn.application.required.MemberRepository;
import eunoospring.splearn.domain.DuplicationEmailException;
import eunoospring.splearn.domain.Email;
import eunoospring.splearn.domain.Member;
import eunoospring.splearn.domain.MemberRegisterRequest;
import eunoospring.splearn.domain.PasswordEncoder;
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
