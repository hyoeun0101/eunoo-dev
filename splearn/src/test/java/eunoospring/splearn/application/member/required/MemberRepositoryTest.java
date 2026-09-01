package eunoospring.splearn.application.member.required;

import static eunoospring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static eunoospring.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberUpdateInfoRequest;
import eunoospring.splearn.domain.member.MemberStatus;
import eunoospring.splearn.domain.member.Profile;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void createMember() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        em.flush();
        em.clear();

        var found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmailFail() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void updateProfile() {
        Member member = createActivatedMember("eunoo@gmail.com");
        member.updateInfo(new MemberUpdateInfoRequest("eunoo12", "eunoo", "안녕하세요."));
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getNickname()).isEqualTo("eunoo12");
        assertThat(found.getDetail().getProfile().address()).isEqualTo("eunoo");
        assertThat(found.getDetail().getIntroduction()).isEqualTo("안녕하세요.");
    }

    @Test
    void findByProfile() {
        Member member = createActivatedMember("eunoo@gmail.com");
        member.updateInfo(new MemberUpdateInfoRequest("eunoo12", "eunoo", "안녕하세요."));
        memberRepository.save(member);

        em.flush();
        em.clear();

        assertThat(memberRepository.findByProfile(new Profile("eunoo"))).isPresent();
        assertThat(memberRepository.findByProfile(new Profile("jun"))).isEmpty();
    }

    @Test
    void duplicateProfileFail() {
        Member member = createActivatedMember("eunoo@gmail.com");
        member.updateInfo(new MemberUpdateInfoRequest("eunoo12", "eunoo", "안녕하세요."));
        memberRepository.save(member);

        em.flush();

        Member member2 = createActivatedMember("jun@gmail.com");
        member2.updateInfo(new MemberUpdateInfoRequest("jun12", "eunoo", "안녕하세요."));

        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Member createActivatedMember(String email) {
        Member member = Member.register(createMemberRegisterRequest(email).toInfo(), createPasswordEncoder());
        member.activate();
        return member;
    }
}