package eunoospring.splearn;

import eunoospring.splearn.application.required.EmailSender;
import eunoospring.splearn.domain.MemberFixture;
import eunoospring.splearn.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> System.out.println(">> Sending email: " + email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
