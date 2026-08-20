package eunoospring.jpabasic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eunoospring.jpabasic.domain.Member;
import eunoospring.jpabasic.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    void 회원가입() {
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);

        assertThat(member).isEqualTo(memberRepository.findOne(savedId));

    }

    @Test
    void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("kim1");
        Member member2 = new Member();
        member2.setName("kim1");

        memberService.join(member1);

        //when & then
        assertThatThrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class);
    }

}