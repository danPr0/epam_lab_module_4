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
public class GiftCertificate extends Auditable {

    @Id
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

    @Column(name = "active")
    private boolean isActive;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable (name="gift_certificates_tags",
                joinColumns=@JoinColumn (name="gc_id"),
                inverseJoinColumns=@JoinColumn(name="tag_id"))
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof GiftCertificate that)) {
            return false;
        }
        return Double.compare(that.price, price) == 0 && duration == that.duration && id.equals(that.id) &&
                name.equals(that.name) && description.equals(that.description) && createdDate.equals(that.createdDate) &&
                lastModifiedDate.equals(that.lastModifiedDate) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, price, duration, createdDate, lastModifiedDate, tags);
    }
}
