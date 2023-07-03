package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.Objects;

/**
 * Entity class for "gift_certificates" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tag extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<GiftCertificate> giftCertificates;

    public Tag(Long id, String name) {

        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {return true;}
        if (!(o instanceof Tag tag)) {return false;}
        return Objects.equals(id, tag.id) && name.equals(tag.name) &&
                Objects.equals(giftCertificates, tag.giftCertificates);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, giftCertificates);
    }
}
