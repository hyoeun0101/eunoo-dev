package eunoospring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("회원을 생성하면 회원의 상태는 PENDING이다.")
    void createMember() {
        var member = new Member("eunoo@gmail.com", "eunoo", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }


}