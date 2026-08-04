package eunoospring.splearn.application.provided;

import static eunoospring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static eunoospring.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import eunoospring.splearn.application.MemberService;
import eunoospring.splearn.application.required.EmailSender;
import eunoospring.splearn.application.required.MemberRepository;
import eunoospring.splearn.domain.Email;
import eunoospring.splearn.domain.Member;
import eunoospring.splearn.domain.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class MemberRegisterManualTest {

    @Test
    void registerTestStub() {
        MemberService memberService = new MemberService(
                new MemberRepositoryStub(), new EmailSenderStub(), createPasswordEncoder()
        );

        Member member = memberService.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberService memberService = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, createPasswordEncoder()
        );

        Member member = memberService.register(createMemberRegisterRequest());

        assertThat(emailSenderMock.emails).hasSize(1);
        assertThat(emailSenderMock.emails.getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
        MemberService memberService = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, createPasswordEncoder()
        );

        Member member = memberService.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock, Mockito.times(1)).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(Email email) {
            return Optional.empty();
        }

    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {

        }

    }

    static class EmailSenderMock implements EmailSender {
        List<Email> emails = new ArrayList<Email>();

        @Override
        public void send(Email email, String subject, String body) {
            emails.add(email);
        }
    }

}