package eunoospring.jpabasic.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * 주문가격
     */
    private int orderPrice;

    /**
     * 주문수량
     */
    private int count;

    public static OrderItem createOrderItem(Order order, Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setCount(count);

        return orderItem;

    }

    public void cancel() {
        item.addStock(this.count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}
