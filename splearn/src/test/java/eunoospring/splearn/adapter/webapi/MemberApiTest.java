package eunoospring.splearn.adapter.webapi;

import static eunoospring.splearn.AssertThatUtils.equalsTo;
import static eunoospring.splearn.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import eunoospring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import eunoospring.splearn.application.member.provided.MemberRegister;
import eunoospring.splearn.application.member.required.MemberRepository;
import eunoospring.splearn.domain.member.Member;
import eunoospring.splearn.domain.member.MemberFixture;
import eunoospring.splearn.application.member.provided.MemberRegisterRequest;
import eunoospring.splearn.domain.member.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class MemberApiTest {

    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void register() {
        //given
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();

        //when
        MvcTestResult result = requestRegister(request);

        //then
        MemberRegisterResponse response = assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsTo(request.email()))
                .convertTo(MemberRegisterResponse.class)
                .actual();

        Member member = memberRepository.findById(response.memberId()).orElseThrow();
        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() {
        //given
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        //when
        MvcTestResult result = requestRegister(MemberFixture.createMemberRegisterRequest());

        //then
        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }

    private MvcTestResult requestRegister(MemberRegisterRequest request) {
        return mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();
    }

}