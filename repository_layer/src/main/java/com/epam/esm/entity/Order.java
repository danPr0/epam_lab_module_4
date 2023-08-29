package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

/**
 * Entity class for "orders" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Order extends Auditable implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_gift_certificates",
               joinColumns = @JoinColumn(name = "order_id"),
               inverseJoinColumns = @JoinColumn(name = "gc_id"))
    private List<GiftCertificate> giftCertificates;

    @Column(name = "cost")
    private Double cost;

    @Override
    public boolean equals(Object o) {

        if (this == o) {return true;}
        if (!(o instanceof Order order)) {return false;}
        return Objects.equals(id, order.id) && user.equals(order.user) &&
                giftCertificates.equals(order.giftCertificates) && cost.equals(order.cost);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public Order clone() {

        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
