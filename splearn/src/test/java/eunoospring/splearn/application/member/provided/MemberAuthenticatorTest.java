package eunoospring.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import tools.jackson.core.ObjectReadContext.Base;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberAuthenticatorTest extends BaseApplicationServiceTest {
    final MemberRegister memberRegister;

    final MemberAuthenticator memberAuthenticator;

    @Test
    void login() {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(request).activate();



        var member = memberAuthenticator.login(new MemberLoginRequest(request.email(), request.password()));
    }

    @Test
    void loginFailNotActive() {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(request);

        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(request.email(), request.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailNotExist() {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(request).activate();

        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest("notexist@splearn.com", request.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailWrongPassword() {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(request).activate();

        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(request.email(), "wrongpassword")))
                .isInstanceOf(LoginFailedException.class);

    }

}