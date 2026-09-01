package eunoospring.splearn.application.member.provided;

import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberUpdateInfoRequest;
import jakarta.validation.Valid;

/**
 * 회원 등록 관련 기능을 제공한다
 */
public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long id);

    Member deactivate(Long id);

    Member updateInfo(Long id, @Valid MemberUpdateInfoRequest updateRequest);
}