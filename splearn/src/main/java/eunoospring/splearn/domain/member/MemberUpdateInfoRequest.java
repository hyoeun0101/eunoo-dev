package eunoospring.splearn.domain.member;

import jakarta.validation.constraints.Size;

public record MemberUpdateInfoRequest(
        @Size(min = 5, max = 20) String nickname,
        @Size(max = 15) String profileAddress,
        String introduction) {
}
