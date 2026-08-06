package eunoospring.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.domain.member.DuplicationEmailException;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.domain.member.MemberInfoUpdateRequest;
import eunoospring.splearn.domain.member.MemberRegisterRequest;
import eunoospring.splearn.domain.member.MemberStatus;
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
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        em.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTVIE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        em.flush();
        em.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        em.flush();
        em.clear();

        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("eunoo12", "eunoo", "안녕하세요.");
        member = memberRegister.updateInfo(member.getId(), updateRequest);
        em.flush();
        em.clear();

        assertThat(member.getNickname()).isEqualTo(updateRequest.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(updateRequest.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(updateRequest.introduction());
    }

    @Test
    void updateInfoFail() {
        Member member = registerMember();

        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("eunoo12", "eunoo", "안녕하세요.");

        assertThatThrownBy(() -> memberRegister.updateInfo(member.getId(), updateRequest))
                .isInstanceOf(IllegalStateException.class);

    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();
        return member;
    }
}
