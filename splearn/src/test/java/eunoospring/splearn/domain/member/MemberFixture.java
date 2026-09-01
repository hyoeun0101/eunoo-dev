package eunoospring.splearn.domain.member;

import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("eunoo@gmail.com");
    }

    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "eunoo", "verysecret");
    }

    public static MemberUpdateInfoRequest createMemberUpdateInfoRequest() {
        return createMemberUpdateInfoRequest("hello11");
    }

    public static MemberUpdateInfoRequest createMemberUpdateInfoRequest(String profileAddress) {
        return new MemberUpdateInfoRequest("hellooo", profileAddress, "안녕하세요.");
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

    public static Member createMember(Long id) {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
