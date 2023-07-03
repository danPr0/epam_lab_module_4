package com.epam.esm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "providers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Provider {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
