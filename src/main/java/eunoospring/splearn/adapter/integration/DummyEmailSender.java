package eunoospring.splearn.adapter.integration;

import eunoospring.splearn.application.member.required.EmailSender;
import eunoospring.splearn.domain.shared.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender sending: " + email);
    }
}
