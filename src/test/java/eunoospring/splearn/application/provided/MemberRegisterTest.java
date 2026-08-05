package eunoospring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.domain.DuplicationEmailException;
import eunoospring.splearn.domain.Member;
import eunoospring.splearn.domain.MemberFixture;
import eunoospring.splearn.domain.MemberRegisterRequest;
import eunoospring.splearn.domain.MemberStatus;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("ImplicitSubclassInspection")
@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
record MemberRegisterTest(MemberRegister memberRegister, EntityManager em) {

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
        failValidation(new MemberRegisterRequest("eunoo", "eunoo", "verysecret"));
        failValidation(new MemberRegisterRequest("eunoo@gmail.com", "ee", "verysecret"));
        failValidation(new MemberRegisterRequest("eunoo@gmail.com", "eunoo", "1234"));
    }

    private void failValidation(MemberRegisterRequest request) {
        assertThatThrownBy(() -> memberRegister.register(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();

        member = memberRegister.activate(member.getId());
        em.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTVIE);
    }
}
