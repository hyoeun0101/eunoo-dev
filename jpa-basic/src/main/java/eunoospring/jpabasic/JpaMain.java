package eunoospring.jpabasic;

import eunoospring.jpabasic.domain.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("hello");

        EntityManager em = factory.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Movie movie = new Movie();
            movie.setActor("reo");
            movie.setDirector("kimeun");
            movie.setName("타이타닉");
            movie.setPrice(1000);

            em.persist(movie);

            tx.commit();

        } catch (Exception e) {
            System.out.println("exception:: " + e.getMessage());
            tx.rollback();
        } finally {
            em.close();
        }
    }
}
