package eunoospring.splearn;

import eunoospring.splearn.application.member.required.EmailSender;
import eunoospring.splearn.domain.shared.Email;
import java.util.ArrayList;
import java.util.List;

/**
 * 발송 요청을 기록만 하는 EmailSender 테스트 대역(stub).
 *
 * 실제로 메일을 보내지 않고 "누구에게 보내라고 했는지"를 모아둔다.
 * 덕분에 응용 서비스가 메일 발송이라는 협력을 실제로 수행했는지 검증할 수 있다.
 */
public class RecordingEmailSender implements EmailSender {

    private final List<Email> sentEmails = new ArrayList<>();

    @Override
    public void send(Email email, String subject, String body) {
        sentEmails.add(email);
    }

    public List<Email> getSentEmails() {
        return sentEmails;
    }

    /**
     * 스프링 컨텍스트는 테스트 클래스 사이에서 재사용되고 이 빈은 싱글턴이라
     * 기록이 계속 쌓인다. 각 테스트가 깨끗한 상태에서 시작하도록 비워준다.
     */
    public void clear() {
        sentEmails.clear();
    }
}
