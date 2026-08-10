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

    public static Member register(MemberRegisterRequest request, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(request.email());
        member.nickname = requireNonNull(request.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(request.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다");

        this.status = MemberStatus.ACTVIE;
        this.detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTVIE, "ACTIVE 상태가 아닙니다");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.decactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordHash);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTVIE;
    }

    public void updateInfo(MemberInfoUpdateRequest request) {
        state(status == MemberStatus.ACTVIE, "ACTIVE 상태가 아닙니다");

        this.nickname = Objects.requireNonNull(request.nickname());
        this.detail.updateInfo(request);
    }
}