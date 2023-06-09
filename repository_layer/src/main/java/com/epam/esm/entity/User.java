package com.epam.esm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.envers.Audited;

/**
 * Entity class for "users" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "users")
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;
}
