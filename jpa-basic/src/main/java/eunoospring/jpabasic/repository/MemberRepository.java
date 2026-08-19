package eunoospring.jpabasic.repository;

import eunoospring.jpabasic.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Member mber) {
        em.persist(mber);
        return mber.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
