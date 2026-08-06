package eunoospring.splearn.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MemberProfileTest {
    @Test
    void profile() {
        new Profile("eunoo12");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("euno1234567890123456789012345678"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("안녕하세요")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("EUNOO")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        Profile profile = new Profile("eunoo");

        assertThat(profile.url()).isEqualTo("@eunoo");
    }

}