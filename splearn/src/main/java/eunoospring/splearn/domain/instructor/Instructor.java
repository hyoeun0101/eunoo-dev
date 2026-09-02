package eunoospring.splearn.domain.instructor;


import eunoospring.splearn.domain.AbstractEntity;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
//@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InstructorStatus status;

    public static Instructor apply(Member member) {
        Assert.state(member.getStatus() == MemberStatus.ACTIVE, "등록 완료 상태가 아닌 회원은 강사 신청 불가능합니다.");

        Instructor instructor = new Instructor();
        instructor.member = member;
        instructor.status = InstructorStatus.PENDING;

        return instructor;
    }

    public void approve() {
        Assert.state(status == InstructorStatus.PENDING, "PENDING 상태가 아닙니다.");
        status = InstructorStatus.ACTIVE;
    }

    public void reject() {
        Assert.state(status == InstructorStatus.PENDING, "PENDING 상태가 아닙니다.");
        status = InstructorStatus.REJECTED;
    }

    public boolean isActive() {
        return status == InstructorStatus.ACTIVE;
    }

    public void ensureActive() {
        Assert.state(isActive(), "ACTIVE 상태가 아닙니다.");
    }
}
