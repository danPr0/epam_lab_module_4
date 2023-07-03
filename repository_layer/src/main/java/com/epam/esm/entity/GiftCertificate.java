package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

/**
 * Entity class for "gift_certificates" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "gift_certificates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class GiftCertificate extends Auditable implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "duration")
    private int duration;

    @ManyToMany
    @JoinTable (name="gift_certificates_tags",
                joinColumns=@JoinColumn (name="gc_id"),
                inverseJoinColumns=@JoinColumn(name="tag_id"))
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {

        if (this == o) {return true;}
        if (!(o instanceof GiftCertificate that)) {return false;}
        return Double.compare(that.price, price) == 0 && duration == that.duration && Objects.equals(id, that.id) &&
                name.equals(that.name) && description.equals(that.description) && tags.equals(that.tags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, price, duration, createdDate, lastModifiedDate, tags);
    }

    @Override
    public GiftCertificate clone() {

        try {
            return (GiftCertificate) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
