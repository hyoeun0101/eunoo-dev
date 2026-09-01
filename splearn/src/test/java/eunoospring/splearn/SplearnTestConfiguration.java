package eunoospring.splearn;

import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.domain.member.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {

    /**
     * 반환 타입을 RecordingEmailSender 로 선언해야 테스트에서 그 타입으로 주입받아
     * 발송 기록을 확인할 수 있다. (EmailSender 로 선언하면 빈 타입이 인터페이스가 되어 버린다)
     */
    @Bean
    public RecordingEmailSender emailSender() {
        return new RecordingEmailSender();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
