package eunoospring.jpabasic;

import eunoospring.jpabasic.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.aspectj.weaver.ast.Or;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("hello");

        EntityManager em = factory.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {


        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }
}
