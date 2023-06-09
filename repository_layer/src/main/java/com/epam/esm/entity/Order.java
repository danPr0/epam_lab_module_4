package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity class for "orders" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "orders")
@IdClass(Order.OrderIdClass.class)
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {

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

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderIdClass implements Serializable {

        private User            user;
        private GiftCertificate giftCertificate;
    }
}
