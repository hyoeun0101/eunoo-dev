package eunoospring.splearn.application.member.provided;

import static eunoospring.splearn.domain.member.MemberFixture.createMemberUpdateInfoRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.splearn.RecordingEmailSender;
import eunoospring.splearn.SplearnTestConfiguration;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.domain.member.MemberStatus;
import eunoospring.splearn.domain.member.exception.DuplicationEmailException;
import eunoospring.splearn.domain.member.exception.DuplicationProfileException;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("ImplicitSubclassInspection")
@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
record MemberRegisterTest(MemberRegister memberRegister, EntityManager em, RecordingEmailSender emailSender) {

    @BeforeEach
    void clearSentEmails() {
        emailSender.clear();
    }

    @Test
    void register() {
        //given
        var request = MemberFixture.createMemberRegisterRequest();

        //when
        // IDENTITY여서 save 이후 바로 insert 실행됨. isnert 후 id 셋팅, member는 managed 상태.
        Member member = memberRegister.register(request);

        //then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(emailSender.getSentEmails()).containsExactly(member.getEmail());
    }

    @Test
    void duplicationEmailFail() {
        //given
        // 곧바로 insert 실행
        memberRegister.register(MemberFixture.createMemberRegisterRequest());


        //when & then
        // 두 번째 register() 는 내부에서 memberRepository.findByEmail(...) 을 실행한다.
        // 내부의 findByEmail => JPQL 쿼리를 날리기 직전에 자동으로 flush.
        // -> 그래서 이 테스트에서는 em.flush() 를 손으로 부를 필요가 없다.
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
        //given
        // clear해서 member는 준영속 상태.
        Member member = registerMember();

        //when
        // 캐시가 비어 있으므로, 내부의 find에서 select문 실행. member는 managed상태. 아직 update는 미실행
        member = memberRegister.activate(member.getId());
        em.flush(); // update문 실행

        //then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        //given
        // clear해서 member는 준영속 상태.
        Member member = registerMember();
        // 캐시가 비어 있으므로, 내부의 find에서 select문 실행. member는 managed상태. 아직 update는 미실행
        memberRegister.activate(member.getId());
        em.flush();
        em.clear(); // 캐시 비우기.

        //when
        // 캐시가 비어있으므로, find에서 select문 실행. 아직 update문는 미실행
        member = memberRegister.deactivate(member.getId());
        em.flush(); //update문 실행

        //then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        //given
        var request = createMemberUpdateInfoRequest();

        Member member = registerMember();
        memberRegister.activate(member.getId());
        em.flush();
        em.clear();

        //when
        member = memberRegister.updateInfo(member.getId(), request);
        em.flush(); //update문 실행

        //then
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
    }

    @Test
    void updateInfoFail() {
        //given
        Member member1 = registerMember();
        memberRegister.activate(member1.getId());
        memberRegister.updateInfo(member1.getId(), createMemberUpdateInfoRequest());
        em.flush();
        em.clear();

        Member member2 = registerMember("jun@gmail.com");
        memberRegister.activate(member2.getId());
        em.flush();
        em.clear();

        //when
        // 중복 profile 사용할 수 없음.
        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), createMemberUpdateInfoRequest()))
                .isInstanceOf(DuplicationProfileException.class);

        // 기존 프로필 수정 가능
        memberRegister.updateInfo(member1.getId(), createMemberUpdateInfoRequest());

        // 기존 프로필 제거 가능
        memberRegister.updateInfo(member1.getId(), createMemberUpdateInfoRequest(""));
        memberRegister.updateInfo(member2.getId(), createMemberUpdateInfoRequest(""));
        em.flush();
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        em.flush();
        em.clear();
        return member;
    }

}
