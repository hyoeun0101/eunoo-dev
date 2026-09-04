package eunoospring.splearn.application.member;

import eunoospring.splearn.application.member.provided.LoginFailedException;
import eunoospring.splearn.application.member.provided.MemberAuthenticator;
import eunoospring.splearn.application.member.provided.MemberLoginRequest;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.PasswordEncoder;
import eunoospring.splearn.domain.shared.Email;
import eunoospring.splearn.support.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class MemberAuthService implements MemberAuthenticator {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginRequest request) throws LoginFailedException {
        Member member = memberRepository.findByEmail(new Email(request.email()))
                .orElseThrow(LoginFailedException::new);

        if (!member.isActive()) {
            throw new LoginFailedException();
        }
        if (!member.verifyPassword(request.password(), passwordEncoder)) {
            throw new LoginFailedException();
        }

        return member;
    }
}

