package eunoospring.splearn.application.member.provided;

import eunoospring.splearn.domain.member.Member;
import jakarta.validation.Valid;

/**
 * 회원 인증
 */
public interface MemberAuthenticator {
    Member login(@Valid MemberLoginRequest request) throws LoginFailedException;
}
