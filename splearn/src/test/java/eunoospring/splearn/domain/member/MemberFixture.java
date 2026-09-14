package eunoospring.splearn.domain.member;

import static org.instancio.Select.field;

import eunoospring.splearn.application.member.provided.MemberRegisterRequest;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return Instancio.of(MemberRegisterRequest.class)
                .set(field(MemberRegisterRequest::email), email)
                .create();
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest(Instancio.gen().net().email().get());
    }

    public static MemberUpdateInfoRequest createMemberUpdateInfoRequest(String profileAddress) {
        return Instancio.of(MemberUpdateInfoRequest.class)
                .generate(field(MemberUpdateInfoRequest::nickname), gen -> gen.string().minLength(5).maxLength(20))
                .set(field(MemberUpdateInfoRequest::profileAddress), profileAddress)
                .generate(field(MemberUpdateInfoRequest::introduction), gen -> gen.string().maxLength(100).nullable())
                .create();
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static Member createMember() {
        return Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
    }

    public static Member createMember(Long id) {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createMember(String email) {
        return Member.register(createMemberRegisterRequest(email).toInfo(), createPasswordEncoder());
    }

    public static Member createActiveMember() {
        Member member = createMember();
        member.activate();
        return member;
    }

}
