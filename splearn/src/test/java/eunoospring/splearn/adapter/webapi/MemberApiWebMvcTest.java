package eunoospring.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.application.member.provided.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {
    private final MockMvcTester mvcTester;
    private final ObjectMapper objectMapper;

    @MockitoBean
    private MemberRegister memberRegister;

    @Test
    void register() {
        Member member = MemberFixture.createMember(1L);
        Mockito.when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();

        assertThat(mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").convertTo(Long.class).isEqualTo(1L);

        Mockito.verify(memberRegister).register(request);
    }

    @Test
    void registerFail() {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email");

        assertThat(mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

}