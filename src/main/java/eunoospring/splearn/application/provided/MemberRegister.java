package eunoospring.splearn.application.provided;

import eunoospring.splearn.domain.Member;
import eunoospring.splearn.domain.MemberRegisterRequest;

/**
 * 회원 등록 관련 기능을 제공한다
 */
public interface MemberRegister {
    Member register(MemberRegisterRequest registerRequest);
}
