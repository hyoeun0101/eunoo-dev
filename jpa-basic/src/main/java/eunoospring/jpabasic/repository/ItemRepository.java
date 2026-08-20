package eunoospring.jpabasic.repository;

import eunoospring.jpabasic.domain.Item;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long save(Item item) {
        em.persist(item);
        return item.getId();
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }
}
