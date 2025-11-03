package com.library.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fine_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal finePerDay;
}