package eunoospring.jpabasic.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import eunoospring.jpabasic.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository mberRepository;

    @Autowired
    EntityManager em;

    @Test
    void testMber() {
        Member member = new Member();
        member.setName("mberA");

        Long savedId = mberRepository.save(member);

        em.flush();
        em.clear();

        Member findMber = mberRepository.findOne(savedId);

        assertThat(findMber.getId()).isEqualTo(member.getId());
        assertThat(findMber.getName()).isEqualTo(member.getName());
//        assertThat(findMber).isEqualTo(member);
//        assertSame(findMber, member);
    }
}