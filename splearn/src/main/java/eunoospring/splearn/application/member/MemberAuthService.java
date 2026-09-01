package eunoospring.splearn.application.member;

import eunoospring.splearn.application.member.provided.LoginFailedException;
import eunoospring.splearn.application.member.provided.MemberAuthenticator;
import eunoospring.splearn.application.member.provided.MemberFinder;
import eunoospring.splearn.application.member.provided.MemberLoginRequest;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.PasswordEncoder;
import eunoospring.splearn.domain.shared.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberAuthService implements MemberAuthenticator {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginRequest request) throws LoginFailedException {
        Member member = memberRepository.findByEmail(new Email(request.email()))
                .orElseThrow(LoginFailedException::new);

        if (!member.verifyPassword(request.password(), passwordEncoder)) {
            throw new LoginFailedException();
        }

        return member;
    }
}

