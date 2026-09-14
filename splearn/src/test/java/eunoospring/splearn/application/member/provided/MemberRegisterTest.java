//package eunoospring.splearn.application.member.provided;
//
//import static eunoospring.splearn.domain.member.MemberFixture.createMemberUpdateInfoRequest;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import eunoospring.splearn.RecordingEmailSender;
//import eunoospring.splearn.domain.member.Member;
//import eunoospring.splearn.domain.member.MemberFixture;
//import eunoospring.splearn.domain.member.MemberStatus;
//import eunoospring.splearn.domain.member.exception.DuplicationEmailException;
//import eunoospring.splearn.domain.member.exception.DuplicationProfileException;
//import eunoospring.splearn.support.ApplicationServiceTest;
//import eunoospring.splearn.support.test.BaseApplicationServiceTest;
//import jakarta.persistence.EntityManager;
//import jakarta.validation.ConstraintViolationException;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//@ApplicationServiceTest
//@RequiredArgsConstructor
//class MemberRegisterTest extends BaseApplicationServiceTest {
//
//    final EntityManager em;
//
//    final RecordingEmailSender emailSender;
//
//    @BeforeEach
//    void clearSentEmails() {
//        emailSender.clear();
//    }
//
//    @Test
//    void register() {
//        var request = MemberFixture.createMemberRegisterRequest();
//
//        Member member = memberRegister.register(request);
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//        assertThat(emailSender.getSentEmails()).containsExactly(member.getEmail());
//    }
//
//    @Test
//    void duplicationEmailFail() {
//        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
//        memberRegister.register(request);
//
//        assertThatThrownBy(() -> memberRegister.register(request))
//                .isInstanceOf(DuplicationEmailException.class);
//    }
//
//    @Test
//    void memberRegisterRequestFail() {
//        failValidation(new MemberRegisterRequest("eunoo", "eunoo", "verysecret"));
//        failValidation(new MemberRegisterRequest("eunoo@gmail.com", "ee", "verysecret"));
//        failValidation(new MemberRegisterRequest("eunoo@gmail.com", "eunoo", "1234"));
//    }
//
//    private void failValidation(MemberRegisterRequest request) {
//        assertThatThrownBy(() -> memberRegister.register(request))
//                .isInstanceOf(ConstraintViolationException.class);
//    }
//
//    @Test
//    void activate() {
//        Member member = prepareMember();
//
//        member = memberRegister.activate(member.getId());
//        em.flush();
//        em.clear();
//
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
//        assertThat(member.getDetail().getActivatedAt()).isNotNull();
//    }
//
//    @Test
//    void deactivate() {
//        Member member = prepareActiveMember();
//        em.flush();
//        em.clear();
//
//        member = memberRegister.deactivate(member.getId());
//        em.flush();
//        em.clear();
//
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
//        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
//    }
//
//    @Test
//    void updateInfo() {
//
//        Member member = prepareActiveMember();
//        em.flush();
//        em.clear();
//
//        var request = MemberFixture.createMemberUpdateInfoRequest("eunoo@profile");
//        member = memberRegister.updateInfo(member.getId(), request);
//        em.flush();
//
//        assertThat(member.getNickname()).isEqualTo(request.nickname());
//        assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
//        assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
//    }
//
//    @Test
//    void updateInfoFailProfileDuplication() {
//        Member member1 = prepareActiveMember();
//        memberRegister.updateInfo(member1.getId(), createMemberUpdateInfoRequest());
//        em.flush();
//        em.clear();
//
//        Member member2 = registerMember("jun@gmail.com");
//        memberRegister.activate(member2.getId());
//        em.flush();
//        em.clear();
//
//        //when
//        // 중복 profile 사용할 수 없음.
//        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), createMemberUpdateInfoRequest()))
//                .isInstanceOf(DuplicationProfileException.class);
//
//        // 기존 프로필 수정 가능
//        memberRegister.updateInfo(member1.getId(), createMemberUpdateInfoRequest());
//
//        // 기존 프로필 제거 가능
//        memberRegister.updateInfo(member1.getId(), createMemberUpdateInfoRequest(""));
//        memberRegister.updateInfo(member2.getId(), createMemberUpdateInfoRequest(""));
//        em.flush();
//    }
//
//    private Member registerMember(String email) {
//        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
//        em.flush();
//        em.clear();
//        return member;
//    }
//
//}
