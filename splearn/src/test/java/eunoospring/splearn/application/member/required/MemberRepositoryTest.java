package eunoospring.splearn.application.member.required;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.domain.member.MemberStatus;
import eunoospring.splearn.domain.member.Profile;
import eunoospring.splearn.support.test.BaseRepositoryTest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@RequiredArgsConstructor
class MemberRepositoryTest extends BaseRepositoryTest {

    @Test
    void saveAndFind() {
        Member member = MemberFixture.createMember();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        var found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmailFail() {
        Member member1 = MemberFixture.createMember("eunoo@splearn.com");
        memberRepository.save(member1);
        Member member2 = MemberFixture.createMember("eunoo@splearn.com");

        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByProfile() {
        Member member = prepareActiveMember();
        member.updateInfo(MemberFixture.createMemberUpdateInfoRequest("eunoo11"));
        memberRepository.save(member);
        em.flush();
        em.clear();

        Optional<Member> found = memberRepository.findByProfile(new Profile("eunoo11"));

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(member);
        assertThat(memberRepository.findByProfile(new Profile("eunoo22"))).isEmpty();
    }

    @Test
    void duplicateProfileFail() {
        Member member1 = prepareActiveMember();
        member1.updateInfo(MemberFixture.createMemberUpdateInfoRequest("eunoo11"));
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member member2 = prepareActiveMember();
        member2.updateInfo(MemberFixture.createMemberUpdateInfoRequest("eunoo11"));
        memberRepository.save(member2);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class);
    }

}