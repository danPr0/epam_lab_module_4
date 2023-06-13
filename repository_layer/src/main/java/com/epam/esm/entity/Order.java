package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.time.Instant;

/**
 * Entity class for "orders" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "orders")
@IdClass(Order.OrderIdClass.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Order extends Auditable {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @OneToOne
    @JoinColumn(name = "gc_id")
    private GiftCertificate giftCertificate;

    @Column(name = "cost")
    private Double cost;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderIdClass implements Serializable {

        private User            user;
        private GiftCertificate giftCertificate;
    }
}
