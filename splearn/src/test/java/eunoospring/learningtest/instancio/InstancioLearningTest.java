package eunoospring.learningtest.instancio;

import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

public class InstancioLearningTest {
    
    @Test
    void user() {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .generate(Select.field(User::getEmail), gen -> gen.net().email())
                .set(Select.field(User::getStatus), UserStatus.PENDING)
                .create();

        System.out.println(user);
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNotEmpty();
        assertThat(user.getEmail()).isNotEmpty();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void userModel() {
        Model<User> model = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .generate(Select.field(User::getEmail), gen -> gen.net().email())
                .set(Select.field(User::getStatus), UserStatus.PENDING)
                .toModel();

        for (int i = 0; i < 100; i++) {
            User user = Instancio.of(model).create();

            System.out.println(user);
            assertThat(user.getId()).isNull();
            assertThat(user.getName()).isNotEmpty();
            assertThat(user.getEmail()).isNotEmpty();
            assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        }
    }

    @Test
    void userRegisterRequest() {
        UserRegisterRequest request = Instancio.of(UserRegisterRequest.class).create();

        System.out.println(request);
        assertThat(request.email()).isNotEmpty();
        assertThat(request.nickname()).isNotEmpty();
        assertThat(request.password()).hasSizeBetween(8,100);

    }
}
