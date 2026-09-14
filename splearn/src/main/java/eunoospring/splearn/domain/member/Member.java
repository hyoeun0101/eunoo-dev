package eunoospring.splearn.domain.member;


import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import eunoospring.splearn.domain.AbstractEntity;
import eunoospring.splearn.domain.shared.Email;
import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private MemberDetail detail;

    /**
     * MemberRegisterRequest 대신 MemberRegisterInfo로 변환한 이유:
     * 도메인 계층의 독립성을 지키기 위해.
     * request는 API의 요청을 받는 부분이고, info는 도메인 계층에서 필요한 데이터만 받음.
     */
    public static Member register(MemberRegisterInfo registerInfo, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(registerInfo.email());
        member.nickname = requireNonNull(registerInfo.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerInfo.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다");

        this.status = MemberStatus.ACTIVE;
        this.detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordHash);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

    public void updateInfo(MemberUpdateInfoRequest request) {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다");

        this.nickname = Objects.requireNonNull(request.nickname());
        this.detail.updateInfo(request);
    }

    public void ensureActive() {
        state(isActive(), "회원의 상태가 ACTIVE가 아닙니다.");
    }
}