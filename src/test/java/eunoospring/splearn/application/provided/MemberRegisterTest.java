package eunoospring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.domain.DuplicationEmailException;
import eunoospring.splearn.domain.Member;
import eunoospring.splearn.domain.MemberFixture;
import eunoospring.splearn.domain.MemberRegisterRequest;
import eunoospring.splearn.domain.MemberStatus;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
public class MemberRegisterTest {

    @Autowired
    MemberRegister memberRegister;

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicationEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicationEmailException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        MemberRegisterRequest request = new MemberRegisterRequest("eunoo@gmail.com", "1111", "");

        assertThatThrownBy(() -> memberRegister.register(request))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
