package eunoospring.jpabasic.exam;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

/**
 * exam 패키지는 Spring이 관리하지 않는 순수 JPA 학습용 엔티티이므로(JpaBasicApplication의
 * @EntityScan 참고), Spring 컨테이너 없이 persistence.xml의 "hello" 퍼시스턴스 유닛을 직접 사용한다.
 */
class TeamMemberTest {

    @Test
    void dd() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            TeamMember teamMember = new TeamMember();
            teamMember.setUsername("memberA");
            em.persist(teamMember);
            em.flush();
            em.clear();

            TeamMember found = em.find(TeamMember.class, teamMember.getId());
            assertNotNull(found);
            assertEquals("memberA", found.getUsername());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

}
