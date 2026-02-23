package com.autoflex.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "raw_materials")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double stockQuantity;
}
