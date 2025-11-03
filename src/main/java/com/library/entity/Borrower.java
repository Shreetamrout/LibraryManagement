package com.library.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "borrowers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipType membershipType;

    @Column(nullable = false)
    private Integer maxBorrowLimit;

    @PrePersist
    public void setDefaultLimit() {
        if (maxBorrowLimit == null) {
            maxBorrowLimit = membershipType == MembershipType.PREMIUM ? 5 : 2;
        }
    }
}